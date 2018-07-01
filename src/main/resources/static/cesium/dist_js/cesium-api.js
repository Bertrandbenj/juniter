
angular.module('cesium.settings.services', ['ngApi', 'cesium.config'])

.factory('csSettings', ['$rootScope', '$q', 'Api', 'localStorage', '$translate', 'csConfig', function($rootScope, $q, Api, localStorage, $translate, csConfig) {
  'ngInject';

  // Define app locales
  var locales = [
    {id:'en',    label:'English'},
    {id:'en-GB', label:'English (UK)'},
    {id:'fr-FR', label:'Français'},
    {id:'nl-NL', label:'Nederlands'},
    {id:'es-ES', label:'Spanish'}
  ];
  var fallbackLocale = csConfig.fallbackLanguage ? fixLocale(csConfig.fallbackLanguage) : 'en';

  // Convert browser locale to app locale (fix #140)
  function fixLocale (locale) {
    if (!locale) return fallbackLocale;

    // exists in app locales: use it
    if (_.findWhere(locales, {id: locale})) return locale;

    // not exists: reiterate with the root(e.g. 'fr-XX' -> 'fr')
    var localeParts = locale.split('-');
    if (localeParts.length > 1) {
      return fixLocale(localeParts[0]);
    }

    // If another locale exists with the same root: use it
    var similarLocale = _.find(locales, function(l) {
      return String.prototype.startsWith.call(l.id, locale);
    });
    if (similarLocale) return similarLocale.id;

    return fallbackLocale;
  }

  // Convert browser locale to app locale (fix #140)
  function fixLocaleWithLog (locale) {
    var fixedLocale = fixLocale(locale);
    if (locale != fixedLocale) {
      console.debug('[settings] Fix locale [{0}] -> [{1}]'.format(locale, fixedLocale));
    }
    return fixedLocale;
  }

  var
  constants = {
    STORAGE_KEY: 'CESIUM_SETTINGS',
    KEEP_AUTH_IDLE_SESSION: 9999
  },
  defaultSettings = angular.merge({
    timeout : 4000,
    cacheTimeMs: 60000, /*1 min*/
    useRelative: false,
    timeWarningExpireMembership: 2592000 * 2 /*=2 mois*/,
    timeWarningExpire: 2592000 * 3 /*=3 mois*/,
    useLocalStorage: true, // override to false if no device
    walletHistoryTimeSecond: 30 * 24 * 60 * 60 /*30 days*/,
    walletHistorySliceSecond: 5 * 24 * 60 * 60 /*download using 5 days slice*/,
    rememberMe: true,
    keepAuthIdle: 10 * 60, // 10min - override to false if no device
    showUDHistory: true,
    httpsMode: false,
    expertMode: false,
    decimalCount: 4,
    uiEffects: true,
    minVersion: '1.1.0',
    newIssueUrl: "https://git.duniter.org/clients/cesium/cesium/issues/new",
    userForumUrl: "https://forum.monnaie-libre.fr",
    latestReleaseUrl: "https://api.github.com/repos/duniter/cesium/releases/latest",
    duniterLatestReleaseUrl: "https://api.github.com/repos/duniter/duniter/releases/latest",
    helptip: {
      enable: true,
      installDocUrl: "https://duniter.org/en/wiki/duniter/install/",
      currency: 0,
      network: 0,
      wotLookup: 0,
      wot: 0,
      wotCerts: 0,
      wallet: 0,
      walletCerts: 0,
      header: 0,
      settings: 0
    },
    currency: {
      allRules: false,
      allWotRules: false
    },
    wallet: {
      showPubkey: true,
      alertIfUnusedWallet: true,
      notificationReadTime: 0
    },
    locale: {
      id: fixLocaleWithLog(csConfig.defaultLanguage || $translate.use()) // use config locale if set, or browser default
    }
  }, csConfig),

  data = {},
  previousData,
  started = false,
  startPromise,
  api = new Api(this, "csSettings");

  var
  reset = function() {
    _.keys(data).forEach(function(key){
      delete data[key];
    });

    applyData(defaultSettings);

    return api.data.raisePromise.reset(data)
      .then(store);
  },

  getByPath = function(path, defaultValue) {
    var obj = data;
    _.each(path.split('.'), function(key) {
      obj = obj[key];
      if (angular.isUndefined(obj)) {
        obj = defaultValue;
        return; // stop
      }
    });

    return obj;
  },

  emitChangedEvent = function() {
    var hasChanged = previousData && !angular.equals(previousData, data);
    previousData = angular.copy(data);
    if (hasChanged) {
      api.data.raise.changed(data);
    }
  },

  store = function() {
    if (!started) {
      console.debug('[setting] Waiting start finished...');
      return (startPromise || start()).then(store);
    }

    var promise;
    if (data.useLocalStorage) {
      // When node is temporary (fallback node): keep previous node address - issue #476
      if (data.node.temporary === true) {
        promise = localStorage.getObject(constants.STORAGE_KEY)
          .then(function(previousSettings) {
            var savedData = angular.copy(data);
            savedData.node = previousSettings && previousSettings.node || {};
            delete savedData.temporary; // never store temporary flag
            return localStorage.setObject(constants.STORAGE_KEY, savedData);
          });
      }
      else {
        promise = localStorage.setObject(constants.STORAGE_KEY, data);
      }
    }
    else {
      promise  = localStorage.setObject(constants.STORAGE_KEY, null);
    }

    return promise
      .then(function() {
        if (data.useLocalStorage) {
          console.debug('[setting] Saved locally');
        }

        // Emit event on store
        return api.data.raisePromise.store(data);
      })

      // Emit event on store
      .then(emitChangedEvent);
  },

  applyData = function(newData) {
    var localeChanged = false;
    if (newData.locale && newData.locale.id) {
      // Fix previously stored locale (could use bad format)
      newData.locale.id = fixLocale(newData.locale.id);
      localeChanged = !data.locale || newData.locale.id !== data.locale.id || newData.locale.id !== $translate.use();
    }

    // Apply stored settings
    angular.merge(data, newData);

    // Always force the usage of default settings
    // This is a workaround for DEV (TODO: implement edition in settings ?)
    data.timeWarningExpire = defaultSettings.timeWarningExpire;
    data.timeWarningExpireMembership = defaultSettings.timeWarningExpireMembership;
    data.cacheTimeMs = defaultSettings.cacheTimeMs;
    data.timeout = defaultSettings.timeout;
    data.minVersion = defaultSettings.minVersion;
    data.latestReleaseUrl = defaultSettings.latestReleaseUrl;
    data.duniterLatestReleaseUrl = defaultSettings.duniterLatestReleaseUrl;
    data.newIssueUrl = defaultSettings.newIssueUrl;
    data.userForumUrl = defaultSettings.userForumUrl;

    // Apply the new locale (only if need)
    if (localeChanged) {
      $translate.use(fixLocale(data.locale.id)); // will produce an event cached by onLocaleChange();
    }

  },

  restore = function() {
    var now = new Date().getTime();
    return localStorage.getObject(constants.STORAGE_KEY)
        .then(function(storedData) {
          // No settings stored
          if (!storedData) {
            console.debug("[settings] No settings in local storage. Using defaults.");
            applyData(defaultSettings);
            emitChangedEvent();
            return;
          }

          // Workaround, to turn on 'rememberMe', but only once (at version 0.13.2)
          if (!storedData.rememberMe && (!storedData.login || !storedData.login.method)) {
            storedData.rememberMe = true;
          }

          // Apply stored data
          applyData(storedData);

          console.debug('[settings] Loaded from local storage in '+(new Date().getTime()-now)+'ms');
          emitChangedEvent();
        });
  },

  getLicenseUrl = function() {
    var locale = data.locale && data.locale.id || csConfig.defaultLanguage || 'en';
    return (csConfig.license) ?
      (csConfig.license[locale] ? csConfig.license[locale] : csConfig.license[csConfig.defaultLanguage || 'en'] || csConfig.license) : undefined;
  },

    // Detect locale sucessuf changes, then apply to vendor libs
  onLocaleChange = function() {
    var locale = $translate.use();
    console.debug('[settings] Locale ['+locale+']');

    // config moment lib
    try {
      moment.locale(locale.substr(0,2));
    }
    catch(err) {
      moment.locale('en');
      console.warn('[settings] Unknown local for moment lib. Using default [en]');
    }

    // config numeral lib
    try {
      numeral.language(locale.substr(0,2));
    }
    catch(err) {
      numeral.language('en');
      console.warn('[settings] Unknown local for numeral lib. Using default [en]');
    }

    // Emit event
    api.locale.raise.changed(locale);
  },


  ready = function() {
    if (started) return $q.when();
    return startPromise || start();
  },

  start = function() {
    console.debug('[settings] Starting...');

    startPromise = localStorage.ready()

      // Restore
      .then(restore)

      // Emit ready event
      .then(function() {
        console.debug('[settings] Started');
        started = true;
        startPromise = null;
        // Emit event (used by plugins)
        api.data.raise.ready(data);
      });

    return startPromise;
  };

  $rootScope.$on('$translateChangeSuccess', onLocaleChange);

  api.registerEvent('data', 'reset');
  api.registerEvent('data', 'changed');
  api.registerEvent('data', 'store');
  api.registerEvent('data', 'ready');
  api.registerEvent('locale', 'changed');

  // Apply default settings. This is required on some browser (web or mobile - see #361)
  applyData(defaultSettings);

  // Default action
  //start();

  return {
    ready: ready,
    start: start,
    data: data,
    getByPath: getByPath,
    reset: reset,
    store: store,
    restore: restore,
    getLicenseUrl: getLicenseUrl,
    defaultSettings: defaultSettings,
    // api extension
    api: api,
    locales: locales,
    constants: constants
  };
}]);


angular.module('cesium.network.services', ['ngApi', 'cesium.bma.services', 'cesium.http.services'])

.factory('csNetwork', ['$rootScope', '$q', '$interval', '$timeout', '$window', 'csConfig', 'BMA', 'csHttp', 'Api', function($rootScope, $q, $interval, $timeout, $window, csConfig, BMA, csHttp, Api) {
  'ngInject';

  factory = function(id) {

    var
      interval,
      constants = {
        UNKNOWN_BUID: -1
      },
      isHttpsMode = $window.location.protocol === 'https:',
      api = new Api(this, "csNetwork-" + id),

      data = {
        bma: null,
        websockets: [],
        loading: true,
        peers: [],
        filter: {
          member: true,
          mirror: true,
          endpointFilter: null,
          online: false,
          ssl: undefined,
          tor: undefined
        },
        sort:{
          type: null,
          asc: true
        },
        expertMode: false,
        knownBlocks: [],
        mainBlock: null,
        uidsByPubkeys: null,
        searchingPeersOnNetwork: false,
        difficulties: null,
        ws2pHeads: null,
        timeout: csConfig.timeout
      },

      // Return the block uid
      buid = function(block) {
        return block && [block.number, block.hash].join('-');
      },

      resetData = function() {
        data.bma = null;
        data.websockets = [];
        data.peers.splice(0);
        data.filter = {
          member: true,
          mirror: true,
          endpointFilter: null,
          online: true
        };
        data.sort = {
          type: null,
          asc: true
        };
        data.expertMode = false;
        data.memberPeersCount = 0;
        data.knownBlocks = [];
        data.mainBlock = null;
        data.uidsByPubkeys = {};
        data.loading = true;
        data.searchingPeersOnNetwork = false;
        data.difficulties = null;
        data.ws2pHeads = null;
        data.timeout = csConfig.timeout;
      },

      hasPeers = function() {
        return data.peers && data.peers.length > 0;
      },

      getPeers = function() {
        return data.peers;
      },

      isBusy = function() {
        return data.loading;
      },

      getKnownBlocks = function() {
        return data.knownBlocks;
      },

      // Load WS2P heads
      loadW2spHeads = function() {
        return data.bma.network.ws2p.heads()
          .then(function (res) {
            data.ws2pHeads = res.heads ? res.heads.reduce(function (res, hit) {
              if (hit.message && hit.sig) {
                try {
                  var head = new Ws2pMessage(hit.message);
                  res[[head.pubkey, head.ws2pid].join('-')] = head;
                }
                catch(err) {
                  // just log, then ignore
                  console.error('[network] Ignoring WS2P head.', err && err.message || err);
                }
              }
              return res;
            }, {}) : {};
          })
          .catch(function(err) {
            // When too many request, retry in 3s
            if (err && err.ucode == BMA.errorCodes.HTTP_LIMITATION) {
              return $timeout(function() {
                return loadW2spHeads();
              }, 3000);
            }
            console.error(err); // can occur on duniter v1.6
            data.ws2pHeads = {};
          });
      },

      // Load personal difficulties
      loadDifficulties = function() {
        return data.bma.blockchain.stats.difficulties()
          .then(function (res) {
            data.difficulties = res.levels ? res.levels.reduce(function (res, hit) {
              if (hit.uid && hit.level) res[hit.uid] = hit.level;
              return res;
            }, {}) : {};
          })
          .catch(function(err) {
            // When too many request, retry in 3s
            if (err && err.ucode == BMA.errorCodes.HTTP_LIMITATION) {
              return $timeout(function() {
                return loadDifficulties();
              }, 3000);
            }
            console.error(err);
            data.difficulties = {};
          });
      },

      loadPeers = function() {
        data.peers = [];
        data.searchingPeersOnNetwork = true;
        data.loading = true;
        data.bma = data.bma || BMA;
        var newPeers = [];

        if (interval) {
          $interval.cancel(interval);
        }

        interval = $interval(function() {
          // not same job instance
          if (newPeers.length) {
            flushNewPeersAndSort(newPeers);
          }
          else if (data.loading && !data.searchingPeersOnNetwork) {
            data.loading = false;
            $interval.cancel(interval);
            // The peer lookup end, we can make a clean final report
            sortPeers(true/*update main buid*/);

            console.debug('[network] Finish: {0} peers found.'.format(data.peers.length));
          }
        }, 1000);

        var initJobs = [
          // Load uids
          data.bma.wot.member.uids()
            .then(function(uids) {
              data.uidsByPubkeys = uids;
            })
            .catch(function(err) {
              data.uidsByPubkeys = {};
              //throw err;
            }),

          // Load WS2P heads
          loadW2spHeads()
        ];

        // Get difficulties (expert mode only)
        if (data.expertMode) {
          initJobs.push(loadDifficulties());
        }

        return $q.all(initJobs)
          .then(function(){
            // online nodes
            if (data.filter.online) {
              /*return data.bma.network.peering.peers({leaves: true})
                .then(function(res){
                  return $q.all(res.leaves.map(function(leaf) {
                    return data.bma.network.peering.peers({ leaf: leaf })
                      .then(function(subres){
                        return addOrRefreshPeerFromJson(subres.leaf.value, newPeers);
                      });
                  }));
                });*/
              return data.bma.network.peers()
                .then(function(res){
                  var jobs = [];
                  _.forEach(res.peers, function(json) {
                    if (json.status == 'UP') {
                      jobs.push(addOrRefreshPeerFromJson(json, newPeers));

                      // Mark WS2P
                      _.forEach(json.endpoints||[], function(ep) {
                        if (ep.startsWith('WS2P')) {
                          var key = json.pubkey + '-' + ep.split(' ')[1];
                          if (data.ws2pHeads[key]) {
                            data.ws2pHeads[key].hasEndpoint = true;
                          }
                        }
                      });
                    }
                  });

                  // Add private WS2P endpoints
                  var privateWs2pHeads = _.values(data.ws2pHeads);
                  if (privateWs2pHeads && privateWs2pHeads.length) {
                    var privateEPCount = 0;
                    //console.debug("[http] Found WS2P endpoints without endpoint:", data.ws2pHeads);
                    _.forEach(privateWs2pHeads, function(head) {
                      if (!head.hasEndPoint) {
                        var peer = new Peer({
                          buid: head.buid,
                          currentNumber: head.buid && head.buid.split('-')[0],
                          pubkey: head.pubkey,
                          version: head.version,
                          powPrefix: head.powPrefix,
                          online: true,
                          uid: data.uidsByPubkeys[head.pubkey],
                          bma: {
                            useWs2p: true,
                            private: true,
                            ws2pid: head.ws2pid
                          },
                          endpoints: [
                            // fake endpoint
                            'WS2P ' + head.ws2pid
                          ]
                        });
                        peer.id = peer.keyID();
                        if (peer.uid && data.expertMode && data.difficulties) {
                          peer.difficulty = data.difficulties[peer.uid];
                        }
                        if (applyPeerFilter(peer)) {
                          newPeers.push(peer);
                          privateEPCount++;
                        }
                      }
                    });

                    if (privateEPCount) {
                      console.debug("[http] Found {0} WS2P endpoints without endpoint (private ?)".format(privateEPCount));
                    }
                  }

                  if (jobs.length) return $q.all(jobs);
                })
                .catch(function(err) {
                  // Log and continue
                  console.error(err);
                });
            }

            // offline nodes
            return data.bma.network.peers()
              .then(function(res){
                var jobs = [];
                _.forEach(res.peers, function(json) {
                  if (json.status !== 'UP') {
                    jobs.push(addOrRefreshPeerFromJson(json, newPeers));
                  }
                });
                if (jobs.length) return $q.all(jobs);
              });
          })

          .then(function(){
            data.searchingPeersOnNetwork = false;
          })
          .catch(function(err){
            console.error(err);
            data.searchingPeersOnNetwork = false;
          });
      },

      /**
       * Apply filter on a peer. (peer uid should have been filled BEFORE)
       */
      applyPeerFilter = function(peer) {
        // no filter
        if (!data.filter) return true;

        // Filter member and mirror
        if ((data.filter.member && !data.filter.mirror && !peer.uid) ||
            (data.filter.mirror && !data.filter.member && peer.uid)) {
          return false;
        }

        // Filter on endpoints
        if (data.filter.endpointFilter && !peer.hasEndpoint(data.filter.endpointFilter)) {
          return false;
        }

        // Filter on status
        if (!data.filter.online && peer.status == 'UP') {
          return false;
        }

        // Filter on ssl
        if (angular.isDefined(data.filter.ssl) && peer.isSsl() != data.filter.ssl) {
          return false;
        }

        // Filter on tor
        if (angular.isDefined(data.filter.tor) && peer.isTor() != data.filter.tor) {
          return false;
        }

        return true;
      },

      addOrRefreshPeerFromJson = function(json, list) {
        list = list || data.newPeers;

        var peers = createPeerEntities(json);
        var hasUpdates = false;

        var jobs = peers.reduce(function(jobs, peer) {
            var existingPeer = _.findWhere(data.peers, {id: peer.id});
            var existingMainBuid = existingPeer ? existingPeer.buid : null;
            var existingOnline = existingPeer ? existingPeer.online : false;

            return jobs.concat(
              refreshPeer(peer)
                .then(function (refreshedPeer) {
                  if (existingPeer) {
                    // remove existing peers, when reject or offline
                    if (!refreshedPeer || (refreshedPeer.online !== data.filter.online && data.filter.online !== 'all')) {
                      console.debug('[network] Peer [{0}] removed (cause: {1})'.format(peer.server, !refreshedPeer ? 'filtered' : (refreshedPeer.online ? 'UP': 'DOWN')));
                      data.peers.splice(data.peers.indexOf(existingPeer), 1);
                      hasUpdates = true;
                    }
                    else if (refreshedPeer.buid !== existingMainBuid){
                      console.debug('[network] {0} endpoint [{1}] new current block'.format(
                        refreshedPeer.bma.useBma ? 'BMA' : 'WS2P',
                        refreshedPeer.server));
                      hasUpdates = true;
                    }
                    else if (existingOnline !== refreshedPeer.online){
                      console.debug('[network] {0} endpoint [{1}] is now {2}'.format(
                        refreshedPeer.bma.useBma ? 'BMA' : 'WS2P',
                        refreshedPeer.server,
                        refreshedPeer.online ? 'UP' : 'DOWN'));
                      hasUpdates = true;
                    }
                    else {
                      console.debug("[network] {0} endpoint [{1}] unchanged".format(
                        refreshedPeer.bma.useBma ? 'BMA' : 'WS2P',
                        refreshedPeer.server));
                    }
                  }
                  else if (refreshedPeer && (refreshedPeer.online === data.filter.online || data.filter.online === 'all')) {
                    console.debug("[network] {0} endpoint [{1}] is {2}".format(
                      refreshedPeer.bma.useBma ? 'BMA' : 'WS2P',
                      refreshedPeer.server,
                      refreshedPeer.online ? 'UP' : 'DOWN'
                    ));
                    list.push(refreshedPeer);
                    hasUpdates = true;
                  }
                })
           );
        }, []);
        return (jobs.length === 1 ? jobs[0] : $q.all(jobs))
          .then(function() {
            return hasUpdates;
          });
      },

      createPeerEntities = function(json, ep) {
        if (!json) return [];
        var peer = new Peer(json);

        // Read bma endpoints
        if (!ep) {
          var endpointsAsString = peer.getEndpoints();
          if (!endpointsAsString) return []; // no BMA

          var endpoints = endpointsAsString.reduce(function (res, epStr) {
            var ep = BMA.node.parseEndPoint(epStr);
            return ep ? res.concat(ep) : res;
          }, []);

          // recursive call, on each endpoint
          if (endpoints.length > 1) {
            return endpoints.reduce(function (res, ep) {
              return res.concat(createPeerEntities(json, ep));
            }, []);
          }

          // if only one bma endpoint: use it and continue
          ep = endpoints[0];
        }
        peer.bma = ep;
        peer.server = peer.getServer();
        peer.dns = peer.getDns();
        peer.blockNumber = peer.block.replace(/-.+$/, '');
        peer.uid = data.uidsByPubkeys[peer.pubkey];
        peer.id = peer.keyID();
        return [peer];
      },

      refreshPeer = function(peer) {

        // Apply filter
        if (!applyPeerFilter(peer)) return $q.when();

        if (!data.filter.online || (data.filter.online === 'all' && peer.status === 'DOWN') || !peer.getHost() /*fix #537*/) {
          peer.online = false;
          return $q.when(peer);
        }

        if (peer.bma.useWs2p && data.ws2pHeads) {
          var ws2pHeadKey = [peer.pubkey, peer.bma.ws2pid].join('-');
          var head = data.ws2pHeads[ws2pHeadKey];
          delete data.ws2pHeads[ws2pHeadKey];
          if (head) {
            peer.buid = head.buid;
            peer.currentNumber=peer.buid && peer.buid.split('-')[0];
            peer.version = head.version;
            peer.powPrefix = head.powPrefix;
          }
          peer.online = !!peer.buid;

          if (peer.uid && data.expertMode && data.difficulties) {
            peer.difficulty = data.difficulties[peer.uid];
          }

          return $q.when(peer);
        }

        // Cesium running in SSL: Do not try to access not SSL node,
        if (!peer.bma.useWs2p && isHttpsMode && !peer.bma.useSsl) {
          peer.online = (peer.status === 'UP');
          peer.buid = constants.UNKNOWN_BUID;
          delete peer.version;

          if (peer.uid && data.expertMode && data.difficulties) {
            peer.difficulty = data.difficulties[peer.uid];
          }

          return $q.when(peer);
        }

        // Do not try to access TOR or WS2P endpoints
        if (peer.bma.useTor || peer.bma.useWs2p) {
          peer.online = (peer.status == 'UP');
          peer.buid = constants.UNKNOWN_BUID;
          delete peer.version;

          if (peer.uid && data.expertMode && data.difficulties) {
            peer.difficulty = data.difficulties[peer.uid];
          }
          return $q.when(peer);
        }

        peer.api = peer.api ||  BMA.lightInstance(peer.getHost(), peer.getPort(), peer.isSsl(), data.timeout);

        // Get current block
        return peer.api.blockchain.current()
          .then(function(block) {
            peer.currentNumber = block.number;
            peer.online = true;
            peer.buid = buid(block);
            peer.medianTime = block.medianTime;
            if (data.knownBlocks.indexOf(peer.buid) === -1) {
              data.knownBlocks.push(peer.buid);
            }
            return peer;
          })
          .catch(function(err) {
            // Special case for currency init (root block not exists): use fixed values
            if (err && err.ucode == BMA.errorCodes.NO_CURRENT_BLOCK) {
              peer.online = true;
              peer.buid = buid({number:0, hash: BMA.constants.ROOT_BLOCK_HASH});
              peer.difficulty  = 0;
              return peer;
            }
            if (!peer.secondTry) {
              var bma = peer.bma || peer.getBMA();
              if (bma.dns && peer.server.indexOf(bma.dns) == -1) {
                // try again, using DNS instead of IPv4 / IPV6
                peer.secondTry = true;
                peer.api = BMA.lightInstance(bma.dns, bma.port, bma.useSsl);
                return refreshPeer(peer); // recursive call
              }
            }

            peer.online=false;
            peer.currentNumber = null;
            peer.buid = null;
            peer.uid = data.uidsByPubkeys[peer.pubkey];
            return peer;
          })
          .then(function(peer) {
            // Exit if offline, or not expert mode or too small device
            if (!data.filter.online || !peer || !peer.online || !data.expertMode) return peer;
            var jobs = [];

            // Get hardship (only for a member peer)
            if (peer.uid) {
              jobs.push(peer.api.blockchain.stats.hardship({pubkey: peer.pubkey})
                .then(function (res) {
                  peer.difficulty = res ? res.level : null;
                })
                .catch(function() {
                  peer.difficulty = null; // continue
                }));
            }

            // Get Version
            jobs.push(peer.api.node.summary()
              .then(function(res){
                peer.version = res && res.duniter && res.duniter.version;
              })
              .catch(function() {
                peer.version = '?'; // continue
              }));

            return $q.all(jobs)
              .then(function(){
                return peer;
              });
          });
      },

      flushNewPeersAndSort = function(newPeers, updateMainBuid) {
        newPeers = newPeers || data.newPeers;
        if (!newPeers.length) return;
        var ids = _.map(data.peers, function(peer){
          return peer.id;
        });
        var hasUpdates = false;
        var newPeersAdded = 0;
        _.forEach(newPeers.splice(0), function(peer) {
          if (!ids[peer.id]) {
            data.peers.push(peer);
            ids[peer.id] = peer;
            hasUpdates = true;
            newPeersAdded++;
          }
        });
        if (hasUpdates) {
          console.debug('[network] Flushing {0} new peers...'.format(newPeersAdded));
          sortPeers(updateMainBuid);
        }
      },

      computeScoreAlphaValue = function(value, nbChars, asc) {
        if (!value) return 0;
        var score = 0;
        value = value.toLowerCase();
        if (nbChars > value.length) {
          nbChars = value.length;
        }
        score += value.charCodeAt(0);
        for (var i=1; i < nbChars; i++) {
          score += Math.pow(0.001, i) * value.charCodeAt(i);
        }
        return asc ? (1000 - score) : score;
      },

      sortPeers = function(updateMainBuid) {
        // Construct a map of buid, with peer count and medianTime
        var buids = {};
        data.memberPeersCount = 0;
        _.forEach(data.peers, function(peer){
          if (peer.buid) {
            var buid = buids[peer.buid];
            if (!buid || !buid.medianTime) {
              buid = {
                buid: peer.buid,
                count: 0,
                medianTime: peer.medianTime
              };
              buids[peer.buid] = buid;
            }
            // If not already done, try to fill medianTime (need to compute consensusBlockDelta)
            else if (!buid.medianTime && peer.medianTime) {
              buid.medianTime = peer.medianTime;
            }
            if (buid.buid != constants.UNKNOWN_BUID) {
              buid.count++;
            }
          }
          data.memberPeersCount += peer.uid ? 1 : 0;
        });
        // Compute pct of use, per buid
        _.forEach(_.values(buids), function(buid) {
          buid.pct = buid.count * 100 / data.peers.length;
        });
        var mainBlock = _.max(buids, function(obj) {
          return obj.count;
        });
        _.forEach(data.peers, function(peer){
          peer.hasMainConsensusBlock = peer.buid == mainBlock.buid;
          peer.hasConsensusBlock = peer.buid && !peer.hasMainConsensusBlock && buids[peer.buid].count > 1;
          if (peer.hasConsensusBlock) {
            peer.consensusBlockDelta = buids[peer.buid].medianTime - mainBlock.medianTime;
          }
        });
        data.peers = _.uniq(data.peers, false, function(peer) {
          return peer.id;
        });
        data.peers = _.sortBy(data.peers, function(peer) {
          var score = 0;
          if (data.sort.type) {
            var sortScore = 0;
            sortScore += (data.sort.type == 'uid' ? computeScoreAlphaValue(peer.uid||peer.pubkey, 3, data.sort.asc) : 0);
            sortScore += (data.sort.type == 'api') &&
              ((peer.isWs2p() && (data.sort.asc ? 1 : -1) || 0) +
              (peer.hasEndpoint('ES_USER_API') && (data.sort.asc ? 0.01 : -0.01) || 0) +
              (peer.isSsl() && (data.sort.asc ? 0.75 : -0.75) || 0)) || 0;
            sortScore += (data.sort.type == 'difficulty' ? (peer.difficulty ? (data.sort.asc ? (10000-peer.difficulty) : peer.difficulty): 0) : 0);
            sortScore += (data.sort.type == 'current_block' ? (peer.currentNumber ? (data.sort.asc ? (1000000000 - peer.currentNumber) : peer.currentNumber) : 0) : 0);
            score += (10000000000 * sortScore);
          }
          score += (1000000000 * (peer.online ? 1 : 0));
          score += (100000000  * (peer.hasMainConsensusBlock ? 1 : 0));
          score += (1000000    * (peer.hasConsensusBlock ? buids[peer.buid].pct : 0));
          if (data.expertMode) {
            score += (100     * (peer.difficulty ? (10000-peer.difficulty) : 0));
            score += (1       * (peer.uid ? computeScoreAlphaValue(peer.uid, 2, true) : 0));
          }
          else {
            score += (100     * (peer.uid ? computeScoreAlphaValue(peer.uid, 2, true) : 0));
            score += (1       * (!peer.uid ? computeScoreAlphaValue(peer.pubkey, 2, true) : 0));
          }
          return -score;
        });

        // Raise event on new main block
        if (updateMainBuid && mainBlock.buid && (!data.mainBlock || data.mainBlock.buid !== mainBlock.buid)) {
          data.mainBlock = mainBlock;
          api.data.raise.mainBlockChanged(mainBlock);
        }

        // Raise event when changed
        api.data.raise.changed(data); // raise event
      },

      startListeningOnSocket = function() {
        // Listen for new block
        var wsBlock = data.bma.websocket.block();
        data.websockets.push(wsBlock);
        wsBlock.on(function(block) {
          if (!block || data.loading) return;
          var buid = [block.number, block.hash].join('-');
          if (data.knownBlocks.indexOf(buid) === -1) {
            console.debug('[network] Receiving block: ' + buid.substring(0, 20));
            data.knownBlocks.push(buid);
            // If first block: do NOT refresh peers (will be done in start() method)
            var skipRefreshPeers = data.knownBlocks.length === 1;
            if (!skipRefreshPeers) {
              data.loading = true;
              // We wait 2s when a new block is received, just to wait for network propagation
              $timeout(function() {
                console.debug('[network] new block received by WS: will refresh peers');
                loadPeers();
              }, 2000, false /*invokeApply*/);
            }
          }
        });
        // Listen for new peer
        var wsPeer = data.bma.websocket.peer();
        data.websockets.push(wsPeer);
        wsPeer.on(function(json) {
          if (!json || data.loading) return;
          var newPeers = [];
          addOrRefreshPeerFromJson(json, newPeers)
            .then(function(hasUpdates) {
              if (!hasUpdates) return;
              if (newPeers.length>0) {
                flushNewPeersAndSort(newPeers, true);
              }
              else {
                console.debug('[network] [ws] Peers updated received');
                sortPeers(true);
              }
            });
        });
      },

      sort = function(options) {
        options = options || {};
        data.filter = options.filter ? angular.merge(data.filter, options.filter) : data.filter;
        data.sort = options.sort ? angular.merge(data.sort, options.sort) : data.sort;
        sortPeers(false);
      },

      start = function(bma, options) {
        options = options || {};
        return BMA.ready()
          .then(function() {
            close();
            data.bma = bma ? bma : BMA;
            data.filter = options.filter ? angular.merge(data.filter, options.filter) : data.filter;
            data.sort = options.sort ? angular.merge(data.sort, options.sort) : data.sort;
            data.expertMode = angular.isDefined(options.expertMode) ? options.expertMode : data.expertMode;
            data.timeout = angular.isDefined(options.timeout) ? options.timeout : csConfig.timeout;
            console.info('[network] Starting network from [{0}]'.format(bma.server));
            var now = new Date();

            startListeningOnSocket();

            return loadPeers()
              .then(function(peers){
                console.debug('[network] Started in '+(new Date().getTime() - now.getTime())+'ms');
                return peers;
              });
          });
      },

      close = function() {
        if (data.bma) {
          console.info('[network] Stopping');

          _.forEach(data.websockets, function(ws){
            ws.close();
          });
          resetData();
        }
      },

      isStarted = function() {
        return !data.bma;
      },

      $q_started = function(callback) {
        if (!isStarted()) { // start first
          return start()
            .then(function() {
              return $q(callback);
            });
        }
        else {
          return $q(callback);
        }
      },

      getMainBlockUid = function() {
        return $q_started(function(resolve, reject){
          resolve (data.mainBuid);
        });
      },

      // Get peers on the main consensus blocks
      getTrustedPeers = function() {
        return $q_started(function(resolve, reject){
          resolve(data.peers.reduce(function(res, peer){
            return (peer.hasMainConsensusBlock && peer.uid) ? res.concat(peer) : res;
          }, []));
        });
      }
      ;

    // Register extension points
    api.registerEvent('data', 'changed');
    api.registerEvent('data', 'mainBlockChanged');
    api.registerEvent('data', 'rollback');

    return {
      id: id,
      data: data,
      start: start,
      close: close,
      hasPeers: hasPeers,
      getPeers: getPeers,
      sort: sort,
      getTrustedPeers: getTrustedPeers,
      getKnownBlocks: getKnownBlocks,
      getMainBlockUid: getMainBlockUid,
      loadPeers: loadPeers,
      isBusy: isBusy,
      // api extension
      api: api
    };
  };

  var service = factory('default');

  service.instance = factory;
  return service;
}]);

//var Base58, Base64, scrypt_module_factory = null, nacl_factory = null;

angular.module('cesium.crypto.services', ['cesium.utils.services'])

  .factory('CryptoUtils', ['$q', '$timeout', 'ionicReady', function($q, $timeout, ionicReady) {
    'ngInject';

    function test(regexpContent) {
      return new RegExp(regexpContent);
    }

    /**
     * CryptoAbstract, abstract class with useful methods
     * @type {number}
     */
    function CryptoAbstractService() {
      this.loaded = false;
      var that = this;

      this.copy = function(source) {
        _.forEach(_.keys(source), function(key) {
          that[key] = source[key];
        });
      };

      this.isLoaded = function() {
        return this.loaded;
      };

      this.util = this.util || {};

      /**
       * Converts an array buffer to a string
       *
       * @private
       * @param {ArrayBuffer} buf The buffer to convert
       * @param {Function} callback The function to call when conversion is complete
       */
      this.util.array_to_string = function(buf, callback) {
        var bb = new Blob([new Uint8Array(buf)]);
        var f = new FileReader();
        f.onload = function(e) {
          callback(e.target.result);
        };
        f.readAsText(bb);
      };
    }

    CryptoAbstractService.prototype.constants = {
      crypto_sign_BYTES: 64,
      crypto_secretbox_NONCEBYTES: 24,
      crypto_box_MACBYTES: 16,
      SEED_LENGTH: 32, // Length of the key
      SCRYPT_PARAMS:{
        SIMPLE: {
          N: 2048,
          r: 8,
          p: 1,
          memory: -1 // default
        },
        DEFAULT: {
          N: 4096,
          r: 16,
          p: 1,
          memory: -1 // default
        },
        SECURE: {
          N: 16384,
          r: 32,
          p: 2,
          memory: -1 // default
        },
        HARDEST: {
          N: 65536,
          r: 32,
          p: 4,
          memory: -1 // default
        },
        EXTREME: {
          N: 262144,
          r: 64,
          p: 8,
          memory: -1 // default
        }
      },
      REGEXP: {
        PUBKEY: '[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{43,44}',
        SECKEY: '[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{86,88}',
        FILE: {
          TYPE_LINE: '^Type: ([a-zA-Z0-9]+)\n',
          PUB: '\npub: ([123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{43,44})\n',
          SEC: '\nsec: ([123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{86,88})\n?$'
        }
      }
    };

    CryptoAbstractService.prototype.regexp = {
      FILE: {
        TYPE_LINE: test(CryptoAbstractService.prototype.constants.REGEXP.FILE.TYPE_LINE),
        PUB: test(CryptoAbstractService.prototype.constants.REGEXP.FILE.PUB),
        SEC: test(CryptoAbstractService.prototype.constants.REGEXP.FILE.SEC)
      }
    };

    CryptoAbstractService.prototype.async_load_base58 = function(on_ready) {
      var that = this;
      if (Base58 !== null){return on_ready(Base58);}
      else {$timeout(function(){that.async_load_base58(on_ready);}, 100);}
    };

    CryptoAbstractService.prototype.async_load_scrypt = function(on_ready, options) {
      var that = this;
      if (scrypt_module_factory !== null){
        on_ready(scrypt_module_factory(options.requested_total_memory));
        that.scrypt.requested_total_memory = options.requested_total_memory;
        //console.log('inside async_load_scrypt', that); // TODO manage memory changes
      }
      else {$timeout(function(){that.async_load_scrypt(on_ready, options);}, 100);}
    };

    CryptoAbstractService.prototype.async_load_nacl_js = function(on_ready, options) {
      var that = this;
      if (nacl_factory !== null) {nacl_factory.instantiate(on_ready, options);}
      else {$timeout(function(){that.async_load_nacl_js(on_ready, options);}, 100);}
    };

    CryptoAbstractService.prototype.async_load_base64 = function(on_ready) {
      var that = this;
      if (Base64 !== null) {on_ready(Base64);}
      else {$timetout(function(){that.async_load_base64(on_ready);}, 100);}
    };

    CryptoAbstractService.prototype.async_load_sha256 = function(on_ready) {
      var that = this;
      if (sha256 !== null){return on_ready(sha256);}
      else {$timeout(function(){that.async_load_sha256(on_ready);}, 100);}
    };

    // TODO pub file in file.* functions
    //CryptoAbstractService.prototype.file = {};

    CryptoAbstractService.prototype.readKeyFile = function(file, withSecret) {
      var that = this;

      if (file && file.content) {
        return  that.parseKeyFileContent(file.content, withSecret);
      }

      return $q(function(resolve, reject) {
        if (!file) {
          return reject('Argument [file] is missing');
        }

        console.debug('[crypto] [keypair] reading file: ', file);
        var reader = new FileReader();
        reader.onload = function (event) {
          that.parseKeyFileContent(event.target.result, withSecret)
            .then(function (res) {
              resolve(res);
            })
            .catch(function (err) {
              reject(err);
            });
        };
        reader.readAsText(file, 'utf8');
      });
    };

    CryptoAbstractService.prototype.parseKeyFileContent = function(content, withSecret, defaultType) {
      var that = this;
      return $q(function(resolve, reject) {
        if (!content) {
          return reject('Argument [content] is missing');
        }
        var typeMatch = that.regexp.FILE.TYPE_LINE.exec(content);

        // no Type (first line)
        if (!typeMatch) {
          // Add default type then retry
          return resolve(that.parseKeyFileContent('Type: PubSec\n' + content, withSecret, true));
        }

        var type = typeMatch[1];

        // Type: PubSec
        if (type == 'PubSec') {
          var pubMatch = that.regexp.FILE.PUB.exec(content);
          if (!pubMatch) {
            return reject('Missing [pub] field in file, or invalid public key value');
          }
          if (!withSecret) {
            return resolve({
              signPk: that.base58.decode(pubMatch[1])
            });
          }
          var secMatch = that.regexp.FILE.SEC.exec(content);
          if (!secMatch) {
            return reject('Missing [sec] field in file, or invalid secret key value');
          }
          return resolve({
            signPk: that.base58.decode(pubMatch[1]),
            signSk: that.base58.decode(secMatch[1])
          });
        }
        else {
          if (defaultType) {
            return reject('Bad file format: missing Type field');
          }
          else {
            return reject('Bad file format, unknown type [' + type + ']');
          }
        }
      });
    };

    // Web crypto API - see https://developer.mozilla.org/en-US/docs/Web/API/Web_Crypto_API
    var crypto =  window.crypto || window.msCrypto || window.Crypto;
    if (crypto && crypto.getRandomValues) {
      CryptoAbstractService.prototype.crypto = crypto;
      CryptoAbstractService.prototype.util = {};
      CryptoAbstractService.prototype.util.random_nonce = function() {
        var nonce = new Uint8Array(crypto_secretbox_NONCEBYTES);
        this.crypto.getRandomValues(nonce);
        return $q.when(nonce);
      };
    }
    else {
      // TODO: add a default function ?
      //CryptoAbstractService.prototype.random_nonce = function() {
      // var nonce = new Uint8Array(crypto_secretbox_NONCEBYTES);
      // var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      // for(var i = 0; i < length; i++) {
      //   text += possible.charAt(Math.floor(Math.random() * possible.length));
      // }
      //}
    }

    function FullJSServiceFactory() {
      this.id = 'FullJS';

      // libraries handlers
      this.scrypt = null;
      this.nacl = null;
      this.base58 = null;
      this.base64 = null;
      var that = this;

      this.util = this.util || {};
      this.util.decode_utf8 = function (s) {
        var i, d = unescape(encodeURIComponent(s)), b = new Uint8Array(d.length);
        for (i = 0; i < d.length; i++) b[i] = d.charCodeAt(i);
        return b;
      };
      this.util.encode_utf8 = function (s) {
        return that.nacl.encode_utf8(s);
      };
      this.util.encode_base58 = function (a) { // TODO : move to abstract factory
        return that.base58.encode(a);
      };
      this.util.decode_base58 = function (a) { // TODO : move to abstract factory
        var i;
        a = that.base58.decode(a);
        var b = new Uint8Array(a.length);
        for (i = 0; i < a.length; i++) b[i] = a[i];
        return b;
      };
      this.util.decode_base64 = function (a) {
        return that.base64.decode(a);
      };
      this.util.encode_base64 = function (b) {
        return that.base64.encode(b);
      };

      this.util.hash_sha256 = function (message) {
        return $q(function (resolve) {
          var msg = that.util.decode_utf8(message);
          var hash = that.nacl.to_hex(that.nacl.crypto_hash_sha256(msg));
          resolve(hash.toUpperCase());
        });
      };
      this.util.random_nonce = function () {
        if (that.crypto && that.crypto.getRandomValues) {
          var nonce = new Uint8Array(that.constants.crypto_secretbox_NONCEBYTES);
          that.crypto.getRandomValues(nonce);
          return $q.when(nonce);
        }
        else {
          return $q.when(that.nacl.crypto_box_random_nonce());
        }
      };

      /**
       * Compute the box key pair, from a sign key pair
       */
      this.box_keypair_from_sign = function (signKeyPair) {
        if (signKeyPair.bokSk && signKeyPair.boxPk) return $q.when(signKeyPair);
        return $q.when(that.nacl.crypto_box_keypair_from_sign_sk(signKeyPair.signSk));
      };

      /**
       * Compute the box public key, from a sign public key
       */
      this.box_pk_from_sign = function (signPk) {
        return $q.when(that.nacl.crypto_box_pk_from_sign_pk(signPk));
      };

      this.box_sk_from_sign = function (signSk) {
        return $q.when(that.nacl.crypto_box_sk_from_sign_sk(signSk));
      };

      /**
       * Encrypt a message, from a key pair
       */
      this.box = function(message, nonce, recipientPk, senderSk) {
        return $q(function (resolve, reject) {
          if (!message) {
            resolve(message);
            return;
          }
          var messageBin = that.util.decode_utf8(message);
          if (typeof recipientPk === "string") {
            recipientPk = that.util.decode_base58(recipientPk);
          }

          //console.debug('Original message: ' + message);
          try {
            var ciphertextBin = that.nacl.crypto_box(messageBin, nonce, recipientPk, senderSk);
            var ciphertext = that.util.encode_base64(ciphertextBin);

            //console.debug('Encrypted message: ' + ciphertext);
            resolve(ciphertext);
          }
          catch (err) {
            reject(err);
          }
        });
      };

      /**
       * Decrypt a message, from a key pair
       */
      this.box_open = function (cypherText, nonce, senderPk, recipientSk) {
        return $q(function (resolve, reject) {
          if (!cypherText) {
            resolve(cypherText);
            return;
          }
          var ciphertextBin = that.util.decode_base64(cypherText);
          if (typeof senderPk === "string") {
            senderPk = that.util.decode_base58(senderPk);
          }

          try {
            var message = that.nacl.crypto_box_open(ciphertextBin, nonce, senderPk, recipientSk);
            that.util.array_to_string(message, function (result) {
              //console.debug('Decrypted text: ' + result);
              resolve(result);
            });
          }
          catch (err) {
            reject(err);
          }

        });
      };

      /**
       * Create key pairs (sign and box), from salt+password (Scrypt auth)
       */
      this.scryptKeypair = function(salt, password, scryptParams) {
        return $q(function(resolve, reject) {
          var seed = that.scrypt.crypto_scrypt(
            that.util.encode_utf8(password),
            that.util.encode_utf8(salt),
            scryptParams && scryptParams.N || that.constants.SCRYPT_PARAMS.DEFAULT.N,
            scryptParams && scryptParams.r || that.constants.SCRYPT_PARAMS.DEFAULT.r,
            scryptParams && scryptParams.p || that.constants.SCRYPT_PARAMS.DEFAULT.p,
            that.constants.SEED_LENGTH);
          var signKeypair = that.nacl.crypto_sign_seed_keypair(seed);
          var boxKeypair = that.nacl.crypto_box_seed_keypair(seed);
          resolve({
            signPk: signKeypair.signPk,
            signSk: signKeypair.signSk,
            boxPk: boxKeypair.boxPk,
            boxSk: boxKeypair.boxSk
          });
        });
      };

      /**
       * Get sign pk from salt+password (scrypt auth)
       */
      this.scryptSignPk = function(salt, password, scryptParams) {
        return $q(function(resolve, reject) {
          try {
            var seed = that.scrypt.crypto_scrypt(
              that.util.encode_utf8(password),
              that.util.encode_utf8(salt),
              scryptParams && scryptParams.N || that.constants.SCRYPT_PARAMS.DEFAULT.N,
              scryptParams && scryptParams.r || that.constants.SCRYPT_PARAMS.DEFAULT.r,
              scryptParams && scryptParams.p || that.constants.SCRYPT_PARAMS.DEFAULT.p,
              that.constants.SEED_LENGTH);
            var signKeypair = that.nacl.crypto_sign_seed_keypair(seed);
            resolve(signKeypair.signPk);
          }
          catch(err) {
            reject(err);
          }
        });
      };

      /**
       * Verify a signature of a message, for a pubkey
       */
      this.verify = function (message, signature, pubkey) {
        return $q(function(resolve, reject) {
          var msg = that.util.decode_utf8(message);
          var sig = that.util.decode_base64(signature);
          var pub = that.util.decode_base58(pubkey);
          var sm = new Uint8Array(that.constants.crypto_sign_BYTES + msg.length);
          var i;
          for (i = 0; i < that.constants.crypto_sign_BYTES; i++) sm[i] = sig[i];
          for (i = 0; i < msg.length; i++) sm[i+that.constants.crypto_sign_BYTES] = msg[i];

          // Call to verification lib...
          var verified = that.nacl.crypto_sign_open(sm, pub) !== null;
          resolve(verified);
        });
      };

      /**
       * Sign a message, from a key pair
       */
      this.sign = function(message, keypair) {
        return $q(function(resolve, reject) {
          var m = that.util.decode_utf8(message);
          var sk = keypair.signSk;
          var signedMsg = that.nacl.crypto_sign(m, sk);
          var sig = new Uint8Array(that.constants.crypto_sign_BYTES);
          for (var i = 0; i < sig.length; i++) sig[i] = signedMsg[i];
          var signature = that.base64.encode(sig);
          resolve(signature);
        });
      };

      this.load = function() {
        var deferred = $q.defer();
        var naclOptions = {};
        var scryptOptions = {};
        if (ionic.Platform.grade.toLowerCase()!='a') {
          console.info('Reduce NaCl memory because plateform grade is not [a] but [' + ionic.Platform.grade + ']');
          naclOptions.requested_total_memory = 16 * 1048576; // 16 Mo
        }
        var loadedLib = 0;
        var checkAllLibLoaded = function() {
          loadedLib++;
          if (loadedLib === 4) {
            that.loaded = true;
            deferred.resolve();
          }
        };
        this.async_load_nacl_js(function(lib) {
          that.nacl = lib;
          checkAllLibLoaded();
        }, naclOptions);
        this.async_load_scrypt(function(lib) {
          that.scrypt = lib;
          checkAllLibLoaded();
        }, scryptOptions);
        this.async_load_base58(function(lib) {
          that.base58 = lib;
          checkAllLibLoaded();
        });
        this.async_load_base64(function(lib) {
          that.base64 = lib;
          checkAllLibLoaded();
        });
        return deferred.promise;
      };

      // Shortcuts
      this.util.hash = that.util.hash_sha256;
      this.box = {
        keypair: {
          fromSignKeypair: that.box_keypair_from_sign,
          skFromSignSk: that.box_sk_from_sign,
          pkFromSignPk: that.box_pk_from_sign
        },
        pack: that.box,
        open: that.box_open
      };
    }
    FullJSServiceFactory.prototype = new CryptoAbstractService();


    /* -----------------------------------------------------------------------------------------------------------------
     * Service that use Cordova MiniSodium plugin
     * ----------------------------------------------------------------------------------------------------------------*/

    /***
     * Factory for crypto, using Cordova plugin
     */
    function CordovaServiceFactory() {

      this.id = 'MiniSodium';

      // libraries handlers
      this.nacl = null; // the cordova plugin
      this.base58= null;
      this.sha256= null;
      var that = this;

      // functions
      this.util = this.util || {};
      this.util.decode_utf8 = function(s) {
        return that.nacl.to_string(s);
      };
      this.util.encode_utf8 = function(s) {
        return that.nacl.from_string(s);
      };
      this.util.encode_base58 = function(a) {
        return that.base58.encode(a);
      };
      this.util.decode_base58 = function(a) {
        var i;
        a = that.base58.decode(a);
        var b = new Uint8Array(a.length);
        for (i = 0; i < a.length; i++) b[i] = a[i];
        return b;
      };
      this.util.decode_base64 = function (a) {
        return that.nacl.from_base64(a);
      };
      this.util.encode_base64 = function (b) {
        return that.nacl.to_base64(b);
      };
      this.util.hash_sha256 = function(message) {
        return $q.when(that.sha256(message).toUpperCase());
      };
      this.util.random_nonce = function() {
        var nonce = new Uint8Array(that.constants.crypto_secretbox_NONCEBYTES);
        that.crypto.getRandomValues(nonce);
        return $q.when(nonce);
      };

      /**
       * Create key pairs (sign and box), from salt+password (Scrypt), using cordova
       */
      this.scryptKeypair = function(salt, password, scryptParams) {
        var deferred = $q.defer();

        that.nacl.crypto_pwhash_scryptsalsa208sha256_ll(
          that.nacl.from_string(password),
          that.nacl.from_string(salt),
          scryptParams && scryptParams.N || that.constants.SCRYPT_PARAMS.DEFAULT.N,
          scryptParams && scryptParams.r || that.constants.SCRYPT_PARAMS.DEFAULT.r,
          scryptParams && scryptParams.p || that.constants.SCRYPT_PARAMS.DEFAULT.p,
          that.constants.SEED_LENGTH,
          function (err, seed) {
            if (err) { deferred.reject(err); return;}

            that.nacl.crypto_sign_seed_keypair(seed, function (err, signKeypair) {
              if (err) { deferred.reject(err); return;}
              var result = {
                signPk: signKeypair.pk,
                signSk: signKeypair.sk
              };
              that.box_keypair_from_sign(result)
                .then(function(boxKeypair) {
                  result.boxPk = boxKeypair.pk;
                  result.boxSk = boxKeypair.sk;
                  deferred.resolve(result);
                })
                .catch(function(err) {
                  deferred.reject(err);
                });
            });

          }
        );

        return deferred.promise;
      };

      /**
       * Get sign PK from salt+password (Scrypt), using cordova
       */
      this.scryptSignPk = function(salt, password, scryptParams) {
        var deferred = $q.defer();

        that.nacl.crypto_pwhash_scryptsalsa208sha256_ll(
          that.nacl.from_string(password),
          that.nacl.from_string(salt),
          scryptParams && scryptParams.N || that.constants.SCRYPT_PARAMS.DEFAULT.N,
          scryptParams && scryptParams.r || that.constants.SCRYPT_PARAMS.DEFAULT.r,
          scryptParams && scryptParams.p || that.constants.SCRYPT_PARAMS.DEFAULT.p,
          that.constants.SEED_LENGTH,
          function (err, seed) {
            if (err) { deferred.reject(err); return;}

            that.nacl.crypto_sign_seed_keypair(seed, function (err, signKeypair) {
              if (err) { deferred.reject(err); return;}
              deferred.resolve(signKeypair.pk);
            });

          }
        );

        return deferred.promise;
      };

      /**
       * Verify a signature of a message, for a pubkey
       */
      this.verify = function (message, signature, pubkey) {
        var deferred = $q.defer();
        that.nacl.crypto_sign_verify_detached(
          that.nacl.from_base64(signature),
          that.nacl.from_string(message),
          that.nacl.from_base64(pubkey),
          function(err, verified) {
            if (err) { deferred.reject(err); return;}
            deferred.resolve(verified);
          });
        return deferred.promise;
      };

      /**
       * Sign a message, from a key pair
       */
      this.sign = function(message, keypair) {
        var deferred = $q.defer();

        that.nacl.crypto_sign(
          that.nacl.from_string(message), // message
          keypair.signSk, // sk
          function(err, signedMsg) {
            if (err) { deferred.reject(err); return;}
            var sig;
            if (signedMsg.length > that.constants.crypto_sign_BYTES) {
              sig = new Uint8Array(that.constants.crypto_sign_BYTES);
              for (var i = 0; i < sig.length; i++) sig[i] = signedMsg[i];
            }
            else {
              sig = signedMsg;
            }
            var signature = that.nacl.to_base64(sig);
            deferred.resolve(signature);
          });

        return deferred.promise;
      };

      /**
       * Compute the box key pair, from a sign key pair
       */
      this.box_keypair_from_sign = function(signKeyPair) {
        if (signKeyPair.bokSk && signKeyPair.boxPk) return $q.when(signKeyPair);
        var deferred = $q.defer();
        var result = {};
        that.nacl.crypto_sign_ed25519_pk_to_curve25519(signKeyPair.signPk, function(err, boxPk) {
          if (err) { deferred.reject(err); return;}
          result.boxPk = boxPk;
          if (result.boxSk) deferred.resolve(result);
        });
        that.nacl.crypto_sign_ed25519_sk_to_curve25519(signKeyPair.signSk, function(err, boxSk) {
          if (err) { deferred.reject(err); return;}
          result.boxSk = boxSk;
          if (result.boxPk) deferred.resolve(result);
        });

        return deferred.promise;
      };

      /**
       * Compute the box public key, from a sign public key
       */
      this.box_pk_from_sign = function(signPk) {
        var deferred = $q.defer();
        that.nacl.crypto_sign_ed25519_pk_to_curve25519(signPk, function(err, boxPk) {
          if (err) { deferred.reject(err); return;}
          deferred.resolve(boxPk);
        });
        return deferred.promise;
      };

      /**
       * Compute the box secret key, from a sign secret key
       */
      this.box_sk_from_sign = function(signSk) {
        var deferred = $q.defer();
        that.nacl.crypto_sign_ed25519_sk_to_curve25519(signSk, function(err, boxSk) {
          if (err) { deferred.reject(err); return;}
          deferred.resolve(boxSk);
        });
        return deferred.promise;
      };

      /**
       * Encrypt a message, from a key pair
       */
      this.box = function(message, nonce, recipientPk, senderSk) {
        if (!message) {
          return $q.reject('No message');
        }
        var deferred = $q.defer();

        var messageBin = that.nacl.from_string(message);
        if (typeof recipientPk === "string") {
          recipientPk = that.util.decode_base58(recipientPk);
        }

        that.nacl.crypto_box_easy(messageBin, nonce, recipientPk, senderSk, function(err, ciphertextBin) {
          if (err) { deferred.reject(err); return;}
          var ciphertext = that.util.encode_base64(ciphertextBin);
          //console.debug('Encrypted message: ' + ciphertext);
          deferred.resolve(ciphertext);
        });
        return deferred.promise;
      };

      /**
       * Decrypt a message, from a key pair
       */
      this.box_open = function(cypherText, nonce, senderPk, recipientSk) {
        if (!cypherText) {
          return $q.reject('No cypherText');
        }
        var deferred = $q.defer();

        var ciphertextBin = that.nacl.from_base64(cypherText);
        if (typeof senderPk === "string") {
          senderPk = that.util.decode_base58(senderPk);
        }

        // Avoid crash if content has not the minimal length - Fix #346
        if (ciphertextBin.length < that.constants.crypto_box_MACBYTES) {
          deferred.reject('Invalid cypher content length');
          return;
        }

        that.nacl.crypto_box_open_easy(ciphertextBin, nonce, senderPk, recipientSk, function(err, message) {
          if (err) { deferred.reject(err); return;}
          that.util.array_to_string(message, function(result) {
            //console.debug('Decrypted text: ' + result);
            deferred.resolve(result);
          });
        });

        return deferred.promise;
      };

      this.load = function() {
        var deferred = $q.defer();
        if (!window.plugins || !window.plugins.MiniSodium) {
          deferred.reject("Cordova plugin 'MiniSodium' not found. Please load Full JS implementation instead.");
        }
        else {
          that.nacl = window.plugins.MiniSodium;
          var loadedLib = 0;
          var checkAllLibLoaded = function() {
            loadedLib++;
            if (loadedLib == 2) {
              that.loaded = true;
              deferred.resolve();
            }
          };
          that.async_load_base58(function(lib) {
            that.base58 = lib;
            checkAllLibLoaded();
          });
          that.async_load_sha256(function(lib) {
            that.sha256 = lib;
            checkAllLibLoaded();
          });
        }

        return deferred.promise;
      };

      // Shortcuts
      this.util.hash = that.util.hash_sha256;
      this.box = {
        keypair: {
          fromSignKeypair: that.box_keypair_from_sign,
          skFromSignSk: that.box_sk_from_sign,
          pkFromSignPk: that.box_pk_from_sign
        },
        pack: that.box,
        open: that.box_open
      };
    }
    CordovaServiceFactory.prototype = new CryptoAbstractService();

    /* -----------------------------------------------------------------------------------------------------------------
     * Create service instance
     * ----------------------------------------------------------------------------------------------------------------*/


    var service = new CryptoAbstractService();

    var isDevice = true;
    // removeIf(android)
    // removeIf(ios)
    isDevice = false;
    // endRemoveIf(ios)
    // endRemoveIf(android)

    //console.debug("[crypto] Created CryptotUtils service. device=" + isDevice);

    ionicReady().then(function() {
      console.debug('[crypto] Starting...');
      var now = new Date().getTime();

      var serviceImpl;

      // Use Cordova plugin implementation, when exists
      if (isDevice && window.plugins && window.plugins.MiniSodium && crypto && crypto.getRandomValues) {
        console.debug('[crypto] Loading Cordova MiniSodium implementation...');
        serviceImpl = new CordovaServiceFactory();
      }
      else {
        console.debug('[crypto] Loading FullJS implementation...');
        serviceImpl = new FullJSServiceFactory();
      }

      // Load (async lib)
      serviceImpl.load()
        .catch(function(err) {
          console.error(err);
          throw err;
        })
        .then(function() {
          service.copy(serviceImpl);
          console.debug('[crypto] Loaded \'{0}\' implementation in {1}ms'.format(service.id, new Date().getTime() - now));
        });

    });


    return service;
  }])
;

angular.module('cesium.utils.services', [])

// Replace the '$ionicPlatform.ready()', to enable multiple calls
// See http://stealthcode.co/multiple-calls-to-ionicplatform-ready/
.factory('ionicReady', ['$ionicPlatform', function($ionicPlatform) {
  'ngInject';

  var readyPromise;

  return function () {
    if (!readyPromise) {
      readyPromise = $ionicPlatform.ready();
    }
    return readyPromise;
  };
}])

.factory('UIUtils', ['$ionicLoading', '$ionicPopup', '$ionicConfig', '$translate', '$q', 'ionicMaterialInk', 'ionicMaterialMotion', '$window', '$timeout', '$ionicPopover', '$state', '$rootScope', 'screenmatch', 'csSettings', function($ionicLoading, $ionicPopup, $ionicConfig, $translate, $q, ionicMaterialInk, ionicMaterialMotion, $window, $timeout,
                             $ionicPopover, $state, $rootScope, screenmatch, csSettings) {
  'ngInject';


  var
    loadingTextCache=null,
    CONST = {
      MAX_HEIGHT: 400,
      MAX_WIDTH: 400,
      THUMB_MAX_HEIGHT: 100,
      THUMB_MAX_WIDTH: 100
    },
    data = {
      smallscreen: screenmatch.bind('xs, sm', $rootScope)
    },
    exports,
    raw = {}
  ;

  function alertError(err, subtitle) {
    if (!err) {
      return $q.when();
    }

    return $q(function(resolve) {
      $translate([err, subtitle, 'ERROR.POPUP_TITLE', 'ERROR.UNKNOWN_ERROR', 'COMMON.BTN_OK'])
      .then(function (translations) {
        var message = err.message || translations[err];
        return $ionicPopup.show({
          template: '<p>' + (message || translations['ERROR.UNKNOWN_ERROR']) + '</p>',
          title: translations['ERROR.POPUP_TITLE'],
          subTitle: translations[subtitle],
          buttons: [
            {
              text: '<b>'+translations['COMMON.BTN_OK']+'</b>',
              type: 'button-assertive',
              onTap: function(e) {
                resolve(e);
              }
            }
          ]
        });
      });
    });
  }

  function alertInfo(message, subtitle) {
    return $q(function(resolve) {
      $translate([message, subtitle, 'INFO.POPUP_TITLE', 'COMMON.BTN_OK'])
      .then(function (translations) {
        $ionicPopup.show({
          template: '<p>' + translations[message] + '</p>',
          title: translations['INFO.POPUP_TITLE'],
          subTitle: translations[subtitle],
          buttons: [
            {
              text: translations['COMMON.BTN_OK'],
              type: 'button-positive',
              onTap: function(e) {
                resolve(e);
              }
            }
          ]
        });
      });
    });
  }

  function alertNotImplemented() {
    return alertInfo('INFO.FEATURES_NOT_IMPLEMENTED');
  }

  function askConfirm(message, title, options) {
    title = title || 'CONFIRM.POPUP_TITLE';

    options = options || {};
    options.cssClass = options.cssClass || 'confirm';
    options.okText = options.okText || 'COMMON.BTN_OK';
    options.cancelText = options.cancelText || 'COMMON.BTN_CANCEL';

    return $translate([message, title, options.cancelText, options.okText])
      .then(function (translations) {
        return $ionicPopup.confirm({
          template: translations[message],
          cssClass: options.cssClass,
          title: translations[title],
          cancelText: translations[options.cancelText],
          cancelType: options.cancelType,
          okText: translations[options.okText],
          okType: options.okType
        });
      });
  }

  function hideLoading(timeout){
    if (timeout) {
      return $timeout(function(){
        return $ionicLoading.hide();
      }, timeout);
    }
    else {
      return $ionicLoading.hide();
    }
  }

  function showLoading(options) {
    if (!loadingTextCache) {
      return $translate('COMMON.LOADING')
        .then(function(translation){
          loadingTextCache = translation;
          return showLoading(options);
        });
    }
    options = options || {};
    options.template = options.template||loadingTextCache;

    return $ionicLoading.show(options);
  }

  function showToast(message, duration, position) {
    duration = duration || 'short';
    position = position || 'bottom';

    return $translate([message])
      .then(function(translations){

        // removeIf(device)
        // Use the $ionicLoading toast.
        // First, make sure to convert duration in number
        if (typeof duration == 'string') {
          if (duration == 'short') {
            duration = 2000;
          }
          else {
            duration = 5000;
          }
        }
        return $ionicLoading.show({ template: translations[message], noBackdrop: true, duration: duration });
        // endRemoveIf(device)
      });
  }

  function onError(msg, reject/*optional*/) {
    return function(err) {
      var fullMsg = msg;
      var subtitle;
      if (!!err && !!err.message) {
        fullMsg = err.message;
        subtitle = msg;
      }
      else if (!msg){
        fullMsg = err;
      }
      // If reject has been given, use it
      if (!!reject) {
        reject(fullMsg);
      }
      // If just a user cancellation: silent
      else if (fullMsg == 'CANCELLED') {
        return hideLoading(10); // timeout, to avoid bug on transfer (when error on reference)
      }

      // Otherwise, log to console and display error
      else {
        hideLoading(10); // timeout, to avoid bug on transfer (when error on reference)
        return alertError(fullMsg, subtitle);
      }
    };
  }

  function isSmallScreen() {
    return data.smallscreen.active;
  }

  function selectElementText(el) {
    if (el.value || el.type == "text" || el.type == "textarea") {
      // Source: http://stackoverflow.com/questions/14995884/select-text-on-input-focus
      if ($window.getSelection && !$window.getSelection().toString()) {
        el.setSelectionRange(0, el.value.length);
      }
    }
    else {
      if (el.childNodes && el.childNodes.length > 0) {
        selectElementText(el.childNodes[0]);
      }
      else {
        // See http://www.javascriptkit.com/javatutors/copytoclipboard.shtml
        var range = $window.document.createRange(); // create new range object
        range.selectNodeContents(el); // set range to encompass desired element text
        var selection = $window.getSelection(); // get Selection object from currently user selected text
        selection.removeAllRanges(); // unselect any user selected text (if any)
        selection.addRange(range); // add range to Selection object to select it
      }
    }
  }

  function getSelectionText(){
    var selectedText = "";
    if (window.getSelection){ // all modern browsers and IE9+
        selectedText = $window.getSelection().toString();
    }
    return selectedText;
  }

  function imageOnLoadResize(resolve, reject, thumbnail) {
    return function(event) {
          var width = event.target.width;
          var height = event.target.height;
       var maxWidth = (thumbnail ? CONST.THUMB_MAX_WIDTH : CONST.MAX_WIDTH);
       var maxHeight = (thumbnail ? CONST.THUMB_MAX_HEIGHT : CONST.MAX_HEIGHT);

          if (width > height) {
         if (width > maxWidth) {
           height *= maxWidth / width;
           width = maxWidth;
            }
          } else {
         if (height > maxHeight) {
           width *= maxHeight / height;
           height = maxHeight;
            }
          }
          var canvas = document.createElement("canvas");
          canvas.width = width;
          canvas.height = height;
          var ctx = canvas.getContext("2d");
          ctx.drawImage(event.target, 0, 0,  canvas.width, canvas.height);

          var dataurl = canvas.toDataURL();

          canvas.remove();

          resolve(dataurl);
        };
  }

  function resizeImageFromFile(file, thumbnail) {
    var img = document.createElement("img");
    return $q(function(resolve, reject) {

      if (file) {
        var reader = new FileReader();
        reader.onload = function(event){
          img.onload = imageOnLoadResize(resolve, reject, thumbnail);
          img.src = event.target.result;
        };
        reader.readAsDataURL(file);
      }
      else {
        reject('no file to resize');
      }
    })
    .then(function(dataurl) {
      img.remove();
      return dataurl;
    })
    ;
  }

  function resizeImageFromSrc(imageSrc, thumbnail) {
    var img = document.createElement("img");
    return $q(function(resolve, reject) {
        img.onload = imageOnLoadResize(resolve, reject, thumbnail);
        img.src = imageSrc;
      })
      .then(function(data){
        img.remove();
        return data;
      });
  }

  function imageOnLoadRotate(resolve, reject) {
    var deg = Math.PI / 180;
    var angle = 90 * deg;
    return function(event) {
      var width = event.target.width;
      var height = event.target.height;
      var maxWidth = CONST.MAX_WIDTH;
      var maxHeight = CONST.MAX_HEIGHT;

      if (width > height) {
        if (width > maxWidth) {
          height *= maxWidth / width;
          width = maxWidth;
        }
      } else {
        if (height > maxHeight) {
          width *= maxHeight / height;
          height = maxHeight;
        }
      }

      var canvas = document.createElement("canvas");
      canvas.width = height;
      canvas.height = width;

      var ctx = canvas.getContext("2d");
      ctx.rotate(angle);
      ctx.drawImage(event.target, 0, (-1) * canvas.width);

      var dataurl = canvas.toDataURL();

      canvas.remove();

      resolve(dataurl);
    };
  }

  function rotateFromSrc(imageSrc, angle) {
    var img = document.createElement("img");
    return $q(function(resolve, reject) {
        img.onload = imageOnLoadRotate(resolve, reject, angle);
        img.src = imageSrc;
      })
      .then(function(data){
        img.remove();
        return data;
      });
  }

  function showPopover(event, options) {

    var deferred = $q.defer();

    options = options || {};
    options.templateUrl = options.templateUrl ? options.templateUrl : 'templates/common/popover_copy.html';
    options.scope = options.scope || $rootScope;
    options.scope.popovers = options.scope.popovers || {};
    options.autoselect = options.autoselect || false;
    options.bindings = options.bindings || {};
    options.autoremove = angular.isDefined(options.autoremove) ? options.autoremove : true;
    options.backdropClickToClose = angular.isDefined(options.backdropClickToClose) ? options.backdropClickToClose : true;
    options.focusFirstInput = angular.isDefined(options.focusFirstInput) ? options.focusFirstInput : false;

    var _show = function(popover) {
      popover = popover || options.scope.popovers[options.templateUrl];
      popover.isResolved=false;
      popover.deferred=deferred;
      popover.options=options;
      // Fill the popover scope
      angular.merge(popover.scope, options.bindings);
      $timeout(function() { // This is need for Firefox
        popover.show(event)
        .then(function() {
          var element;
          // Auto select text
          if (options.autoselect) {
            element = document.querySelectorAll(options.autoselect)[0];
            if (element) {
              if ($window.getSelection && !$window.getSelection().toString()) {
                element.setSelectionRange(0, element.value.length);
                element.focus();
              }
              else {
                element.focus();
              }
            }
          }
          else {
            // Auto focus on a element
            if (options.autofocus) {
              element = document.querySelectorAll(options.autofocus)[0];
              if (element) element.focus();
            }
          }

          popover.scope.$parent.$emit('popover.shown');

          // Callback 'afterShow'
          if (options.afterShow) options.afterShow(popover);
        });
      });
    };

    var _cleanup = function(popover) {
      popover = popover || options.scope.popovers[options.templateUrl];
      if (popover) {
        delete options.scope.popovers[options.templateUrl];
        // Remove the popover
        popover.remove()
          // Workaround for issue #244
          // See also https://github.com/driftyco/ionic-v1/issues/71
          // and https://github.com/driftyco/ionic/issues/9069
          .then(function() {
            var bodyEl = angular.element($window.document.querySelectorAll('body')[0]);
            bodyEl.removeClass('popover-open');
          });
      }
    };

    var popover = options.scope.popovers[options.templateUrl];
    if (!popover) {

      $ionicPopover.fromTemplateUrl(options.templateUrl, {
        scope: options.scope,
        backdropClickToClose: options.backdropClickToClose
      })
        .then(function (popover) {
          popover.isResolved = false;

          popover.scope.closePopover = function(result) {
            var autoremove = popover.options.autoremove;
            delete popover.options.autoremove; // remove to avoid to trigger 'popover.hidden'
            popover.hide()
              .then(function() {
                if (autoremove) {
                  return _cleanup(popover);
                }
              })
              .then(function() {
                if (popover.deferred) {
                  popover.deferred.resolve(result);
                }
                delete popover.deferred;
                delete popover.options;
              });
          };

          // Execute action on hidden popover
          popover.scope.$on('popover.hidden', function() {
            if (popover.options && popover.options.afterHidden) {
              popover.options.afterHidden();
            }
            if (popover.options && popover.options.autoremove) {
              _cleanup(popover);
            }
          });

          // Cleanup the popover when hidden
          options.scope.$on('$remove', function() {
            if (popover.deferred) {
              popover.deferred.resolve();
            }
            _cleanup();
          });

          options.scope.popovers[options.templateUrl] = popover;
          _show(popover);
        });
    }
    else {
      _show(popover);
    }

    return deferred.promise;
  }

  function showCopyPopover(event, value) {
    var rows = value && value.indexOf('\n') >= 0 ? value.split('\n').length : 1;
    return showPopover(event, {
      templateUrl: 'templates/common/popover_copy.html',
      bindings: {
        value: value,
        rows: rows
      },
      autoselect: '.popover-copy ' + (rows <= 1 ? 'input' : 'textarea')
    });
  }

  function showSharePopover(event, options) {
    options = options || {};
    options.templateUrl = options.templateUrl ? options.templateUrl : 'templates/common/popover_share.html';
    options.autoselect = options.autoselect || '.popover-share input';
    options.bindings = options.bindings || {};
    options.bindings.value = options.bindings.value || options.bindings.url ||
      $state.href($state.current, $state.params, {absolute: true});
    options.bindings.postUrl = options.bindings.postUrl || options.bindings.value;
    options.bindings.postMessage = options.bindings.postMessage || '';
    options.bindings.titleKey = options.bindings.titleKey || 'COMMON.POPOVER_SHARE.TITLE';
    return showPopover(event, options);
  }

  function showHelptip(id, options) {
    var element = (typeof id == 'string') && id ? $window.document.getElementById(id) : id;
    if (!id && !element && options.selector) {
      element = $window.document.querySelector(options.selector);
    }

    options = options || {};
    var deferred = options.deferred || $q.defer();

    if(element && !options.timeout) {
      if (options.preAction) {
        element[options.preAction]();
      }
      options.templateUrl = options.templateUrl ? options.templateUrl : 'templates/common/popover_helptip.html';
      options.autofocus = options.autofocus || '#helptip-btn-ok';
      options.bindings = options.bindings || {};
      options.bindings.icon = options.bindings.icon || {};
      options.bindings.icon.position = options.bindings.icon.position || false;
      options.bindings.icon.glyph = options.bindings.icon.glyph ||
        (options.bindings.icon.position && options.bindings.icon.position.startsWith('bottom-') ? 'ion-arrow-down-c' :'ion-arrow-up-c');
      options.bindings.icon.class = options.bindings.icon.class || 'calm icon ' + options.bindings.icon.glyph;
      options.bindings.tour = angular.isDefined(options.bindings.tour) ? options.bindings.tour : false;
      showPopover(element, options)
        .then(function(result){
          if (options.postAction) {
            element[options.postAction]();
          }
          deferred.resolve(result);
        })
        .catch(function(err){
          if (options.postAction) {
            element[options.postAction]();
          }
          deferred.reject(err);
        });
    }
    else {

      // Do timeout if ask
      if (options.timeout) {
        var timeout = options.timeout;
        options.retryTimeout = options.retryTimeout || timeout;
        delete options.timeout;
        options.deferred = deferred;
        $timeout(function () {
          showHelptip(id, options);
        }, timeout);
      }

      // No element: reject
      else if (angular.isDefined(options.retry) && !options.retry) {

        if (options.onError === 'continue') {
          $timeout(function () {
            deferred.resolve(true);
          });
        }
        else {
          $timeout(function () {
            deferred.reject("[helptip] element now found: " + id);
          });
        }
      }

      // Retry until element appears
      else {
        options.retry = angular.isUndefined(options.retry) ? 2 : (options.retry-1);
        options.deferred = deferred;
        $timeout(function() {
          showHelptip(id, options);
        }, options.timeout || options.retryTimeout || 100);
      }
    }

    return deferred.promise;
  }

  function showFab(id, timeout) {
    if (!timeout) {
      timeout = 900;
    }
    $timeout(function () {
      // Could not use 'getElementById', because it return only once element,
      // but many fabs can have the same id (many view could be loaded at the same time)
      var fabs = document.getElementsByClassName('button-fab');
      _.forEach(fabs, function(fab){
        if (fab.id == id) {
          fab.classList.toggle('on', true);
        }
      });
    }, timeout);
  }

  function hideFab(id, timeout) {
    if (!timeout) {
      timeout = 10;
    }
    $timeout(function () {
      // Could not use 'getElementById', because it return only once element,
      // but many fabs can have the same id (many view could be loaded at the same time)
      var fabs = document.getElementsByClassName('button-fab');
      _.forEach(fabs, function(fab){
        if (fab.id == id) {
          fab.classList.toggle('on', false);
        }
      });
    }, timeout);
  }

  function motionDelegate(delegate, ionListClass) {
    var motionTimeout = isSmallScreen() ? 100 : 10;
    var defaultSelector = '.list.{0} .item, .list .{0} .item'.format(ionListClass, ionListClass);
      return {
      ionListClass: ionListClass,
      show: function(options) {
        options = options || {};
        options.selector = options.selector || defaultSelector;
        options.ink = angular.isDefined(options.ink) ? options.ink : true;
        options.startVelocity = options.startVelocity || (isSmallScreen() ? 1100 : 3000);
        return $timeout(function(){

          // Display ink effect (no selector need)
          if (options.ink) exports.ink();

          // Display the delegated motion effect
          delegate(options);
        }, options.timeout || motionTimeout);
      }
    };
  }

  function setEffects(enable) {
    if (exports.motion.enable === enable) return; // same
    console.debug('[UI] [effects] ' + (enable ? 'Enable' : 'Disable'));

    if (enable) {
      $ionicConfig.views.transition('platform');
      exports.motion = raw.motion;
    }
    else {
      $ionicConfig.views.transition('none');
      var nothing = {
        class: undefined,
        show: function(){}
      };
      exports.motion = {
        enable : false,
        default: nothing,
        fadeSlideIn: nothing,
        fadeSlideInRight: nothing,
        panInLeft: nothing,
        pushDown: nothing,
        ripple: nothing,
        slideUp: nothing,
        fadeIn: nothing,
        toggleOn: toggleOn,
        toggleOff: toggleOff
      };
    }
  }

  raw.motion = {
    enable: true,
    default: motionDelegate(ionicMaterialMotion.ripple, 'animate-ripple'),
    blinds: motionDelegate(ionicMaterialMotion.blinds, 'animate-blinds'),
    fadeSlideIn: motionDelegate(ionicMaterialMotion.fadeSlideIn, 'animate-fade-slide-in'),
    fadeSlideInRight: motionDelegate(ionicMaterialMotion.fadeSlideInRight, 'animate-fade-slide-in-right'),
    panInLeft: motionDelegate(ionicMaterialMotion.panInLeft, 'animate-pan-in-left'),
    pushDown: motionDelegate(ionicMaterialMotion.pushDown, 'push-down'),
    ripple: motionDelegate(ionicMaterialMotion.ripple, 'animate-ripple'),
    slideUp: motionDelegate(ionicMaterialMotion.slideUp, 'slide-up'),
    fadeIn: motionDelegate(function(options) {
        toggleOn(options);
      }, 'fade-in'),
    toggleOn: toggleOn,
    toggleOff: toggleOff
  };


  function toggleOn(options, timeout) {
    // We have a single option, so it may be passed as a string or property
    if (typeof options === 'string') {
      options = {
        selector: options
      };
    }

    // Fail early & silently log
    var isInvalidSelector = typeof options.selector === 'undefined' || options.selector === '';

    if (isInvalidSelector) {
      console.error('invalid toggleOn selector');
      return false;
    }

    $timeout(function () {
      var elements = document.querySelectorAll(options.selector);
      if (elements) _.forEach(elements, function(element){
        element.classList.toggle('on', true);
      });
    }, timeout || 100);
  }

  function toggleOff(options, timeout) {
    // We have a single option, so it may be passed as a string or property
    if (typeof options === 'string') {
      options = {
        selector: options
      };
    }

    // Fail early & silently log
    var isInvalidSelector = typeof options.selector === 'undefined' || options.selector === '';

    if (isInvalidSelector) {
      console.error('invalid toggleOff selector');
      return false;
    }

    $timeout(function () {
      var elements = document.querySelectorAll(options.selector);
      if (elements) _.forEach(elements, function(element){
        element.classList.toggle('on', false);
      });
    }, timeout || 900);
  }



  csSettings.api.data.on.changed($rootScope, function(data) {
   setEffects(data.uiEffects);
  });

  exports = {
    alert: {
      error: alertError,
      info: alertInfo,
      confirm: askConfirm,
      notImplemented: alertNotImplemented
    },
    loading: {
      show: showLoading,
      hide: hideLoading
    },
    toast: {
      show: showToast
    },
    onError: onError,
    screen: {
      isSmall: isSmallScreen
    },
    ink: ionicMaterialInk.displayEffect,
    motion: raw.motion,
    setEffects: setEffects,
    fab: {
      show: showFab,
      hide: hideFab
    },
    popover: {
      show: showPopover,
      copy: showCopyPopover,
      share: showSharePopover,
      helptip: showHelptip
    },
    selection: {
      select: selectElementText,
      get: getSelectionText
    },
    image: {
      resizeFile: resizeImageFromFile,
      resizeSrc: resizeImageFromSrc,
      rotateSrc: rotateFromSrc
    },
    raw: raw
  };

  return exports;
}])


// See http://plnkr.co/edit/vJQXtsZiX4EJ6Uvw9xtG?p=preview
.factory('$focus', ['$timeout', '$window', function($timeout, $window) {
  'ngInject';

  return function(id) {
    // timeout makes sure that it is invoked after any other event has been triggered.
    // e.g. click events that need to run before the focus or
    // inputs elements that are in a disabled state but are enabled when those events
    // are triggered.
    $timeout(function() {
      var element = $window.document.getElementById(id);
      if(element)
        element.focus();
    });
  };
}])

;

angular.module('cesium.cache.services', ['angular-cache'])

.factory('csCache', ['$http', 'csSettings', 'CacheFactory', function($http, csSettings, CacheFactory) {
  'ngInject';

  var
    constants = {
      LONG: 1 * 60  * 60 * 1000 /*5 min*/,
      SHORT: csSettings.defaultSettings.cacheTimeMs
    },
    cacheNames = []
  ;

  function getOrCreateCache(prefix, maxAge, onExpire){
    prefix = prefix || 'csCache-';
    maxAge = maxAge || constants.SHORT;
    var cacheName = prefix + maxAge;
    if (!onExpire) {
      if (!cacheNames[cacheName]) {
        cacheNames[cacheName] = true;
      }
      return CacheFactory.get(cacheName) ||
        CacheFactory.createCache(cacheName, {
          maxAge: maxAge,
          deleteOnExpire: 'aggressive',
          //cacheFlushInterval: 60 * 60 * 1000, //  clear itself every hour
          recycleFreq: Math.max(maxAge - 1000, 5 * 60 * 1000 /*5min*/),
          storageMode: 'memory'
            // FIXME : enable this when cache is cleaning on rollback
            //csSettings.data.useLocalStorage ? 'localStorage' : 'memory'
        });
    }
    else {
      var counter = 1;
      while(CacheFactory.get(cacheName + counter)) {
        counter++;
      }
      cacheName = cacheName + counter;
      if (!cacheNames[cacheName]) {
        cacheNames[cacheName] = true;
      }
      return CacheFactory.createCache(cacheName, {
          maxAge: maxAge,
          deleteOnExpire: 'aggressive',
          //cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
          recycleFreq: maxAge,
          onExpire: onExpire,
          storageMode: 'memory'
            // FIXME : enable this when cache is cleaning on rollback
            //csSettings.data.useLocalStorage ? 'localStorage' : 'memory'
        });
    }
  }

  function clearAllCaches() {
    console.debug("[cache] cleaning all caches");
    _.forEach(_.keys(cacheNames), function(cacheName) {
      var cache = CacheFactory.get(cacheName);
      if (cache) {
        cache.removeAll();
      }
    });
  }

  function clearFromPrefix(cachePrefix) {
    _.forEach(_.keys(cacheNames), function(cacheName) {
      if (cacheName.startsWith(cachePrefix)) {
        var cache = CacheFactory.get(cacheNames);
        if (cache) {
          cache.removeAll();
        }
      }
    });
  }

  return {
    get: getOrCreateCache,
    clear: clearFromPrefix,
    clearAll: clearAllCaches,
    constants: {
      LONG : constants.LONG,
      SHORT: constants.SHORT
    }
  };
}])
;

angular.module('cesium.modal.services', [])

// Useful for modal with no controller
.controller('EmptyModalCtrl', function () {
  'ngInject';

})

.controller('AboutModalCtrl', ['$scope', 'UIUtils', 'csHttp', function ($scope, UIUtils, csHttp) {
  'ngInject';

  $scope.openLink = function(event, uri, options) {
    options = options || {};

    // If unable to open, just copy value
    options.onError = function() {
      return UIUtils.popover.copy(event, uri);
    };

    return csHttp.uri.open(uri, options);
  };
}])

.factory('ModalUtils', ['$ionicModal', '$rootScope', '$q', '$injector', '$controller', '$timeout', function($ionicModal, $rootScope, $q, $injector, $controller, $timeout) {
  'ngInject';


  function _evalController(ctrlName) {
    var result = {
        isControllerAs: false,
        controllerName: '',
        propName: ''
    };
    var fragments = (ctrlName || '').trim().split(/\s+/);
    result.isControllerAs = fragments.length === 3 && (fragments[1] || '').toLowerCase() === 'as';
    if (result.isControllerAs) {
        result.controllerName = fragments[0];
        result.propName = fragments[2];
    } else {
        result.controllerName = ctrlName;
    }

    return result;
  }

  function DefaultModalController($scope, deferred, parameters) {

    $scope.deferred = deferred || $q.defer();
    $scope.resolved = false;

    $scope.openModal = function () {
      return $scope.modal.show();
    };

    $scope.hideModal = function () {
      return $scope.modal.hide();
    };

    $scope.closeModal = function (result) {
      $scope.resolved = true;
      return $scope.modal.remove()
        .then(function() {
          $scope.deferred.resolve(result);
          return result;
        });
    };


    // Useful method for modal with forms
    $scope.setForm = function (form, propName) {
      if (propName) {
        $scope[propName] = form;
      }
      else {
        $scope.form = form;
      }
    };

    // Useful method for modal to get input parameters
    $scope.getParameters = function () {
      return parameters;
    };

    $scope.$on('modal.hidden', function () {
      // If not resolved yet: send result
      // (after animation out)
      if (!$scope.resolved) {
        $scope.resolved = true;

        $timeout(function() {
          $scope.deferred.resolve();
          return $scope.modal.remove();
        }, ($scope.modal.hideDelay || 320) + 20);
      }
    });
  }

  function show(templateUrl, controller, parameters, options) {
    var deferred = $q.defer();

    options = options ? options : {} ;
    options.animation = options.animation || 'slide-in-up';

    // If modal has a controller
    if (controller) {
      // If a controller defined, always use a new scope
      options.scope = options.scope ? options.scope.$new() : $rootScope.$new();
      DefaultModalController.call({}, options.scope, deferred, parameters);

      // Invoke the controller on this new scope
      var locals = { '$scope': options.scope, 'parameters': parameters };
      var ctrlEval = _evalController(controller);
      var ctrlInstance = $controller(controller, locals);
      if (ctrlEval.isControllerAs) {
        ctrlInstance.openModal = options.scope.openModal;
        ctrlInstance.closeModal = options.scope.closeModal;
      }
    }

    $ionicModal.fromTemplateUrl(templateUrl, options)
      .then(function (modal) {
          if (controller) {
            // Set modal into the controller's scope
            modal.scope.$parent.modal = modal;
          }
          else {
            var scope = modal.scope;
            // Define default scope functions
            DefaultModalController.call({}, scope, deferred, parameters);
            // Set modal
            scope.modal = modal;
          }

          // Show the modal
          return modal.show();
        },
        function (err) {
          deferred.reject(err);
        });

    return deferred.promise;
  }

  return {
    show: show
  };
}])

.factory('Modals', ['$rootScope', 'ModalUtils', 'UIUtils', function($rootScope, ModalUtils, UIUtils) {
  'ngInject';

  function showTransfer(parameters) {
    var useDigitKeyboard = UIUtils.screen.isSmall();
    return ModalUtils.show('templates/wallet/modal_transfer.html','TransferModalCtrl',
      parameters, {
        focusFirstInput: !useDigitKeyboard
      });
  }

  function showLogin(parameters) {
    return ModalUtils.show('templates/login/modal_login.html','LoginModalCtrl',
      parameters, {focusFirstInput: true});
  }

  function showAdvancedLogin(parameters) {
    return ModalUtils.show('templates/login/modal_advanced_login.html','LoginModalCtrl',
      parameters, {focusFirstInput: true});
  }


  function showWotLookup(parameters) {
    return ModalUtils.show('templates/wot/modal_lookup.html','WotLookupModalCtrl',
      parameters || {}, {focusFirstInput: true});
  }

  function showNetworkLookup(parameters) {
    return ModalUtils.show('templates/network/modal_network.html', 'NetworkLookupModalCtrl',
      parameters, {focusFirstInput: true});
  }

  function showAbout(parameters) {
    return ModalUtils.show('templates/modal_about.html','AboutModalCtrl',
      parameters);
  }

  function showAccountSecurity(parameters) {
    return ModalUtils.show('templates/wallet/modal_security.html', 'WalletSecurityModalCtrl',
      parameters);
  }

  function showJoin(parameters) {
    return ModalUtils.show('templates/join/modal_choose_account_type.html','JoinChooseAccountTypeModalCtrl',
      parameters)
      .then(function(res){
        if (!res) return;
        return (res.accountType == 'member') ?
          showJoinMember(res) :
          showJoinWallet(res);
      });
  }

  function showJoinMember(parameters) {
    return ModalUtils.show('templates/join/modal_join_member.html','JoinModalCtrl',
      parameters);
  }

  function showJoinWallet(parameters) {
    return ModalUtils.show('templates/join/modal_join_wallet.html','JoinModalCtrl',
      parameters);
  }

  function showHelp(parameters) {
    return ModalUtils.show('templates/help/modal_help.html','HelpModalCtrl',
      parameters);
  }

  function showLicense(parameters) {
    return ModalUtils.show('templates/currency/modal_license.html','CurrencyLicenseModalCtrl',
      parameters);
  }

  function showSelectPubkeyIdentity(parameters) {
    return ModalUtils.show('templates/wot/modal_select_pubkey_identity.html', 'WotSelectPubkeyIdentityModalCtrl',
      parameters);
  }

  return {
    showTransfer: showTransfer,
    showLogin: showLogin,
    showAdvancedLogin: showAdvancedLogin,
    showWotLookup: showWotLookup,
    showNetworkLookup: showNetworkLookup,
    showAbout: showAbout,
    showJoin: showJoin,
    showJoinMember: showJoinMember,
    showJoinWallet: showJoinWallet,
    showHelp: showHelp,
    showAccountSecurity: showAccountSecurity,
    showLicense: showLicense,
    showSelectPubkeyIdentity: showSelectPubkeyIdentity
  };

}]);

angular.module('cesium.http.services', ['cesium.cache.services'])

.factory('csHttp', ['$http', '$q', '$timeout', '$window', 'csSettings', 'csCache', 'Device', function($http, $q, $timeout, $window, csSettings, csCache, Device) {
  'ngInject';

  var timeout = csSettings.data.timeout;

  var
    sockets = [],
    cachePrefix = 'csHttp'
  ;

  if (!timeout) {
    timeout=4000; // default
  }

  function getServer(host, port) {
    // Remove port if 80 or 443
    return  !host ? null : (host + (port && port != 80 && port != 443 ? ':' + port : ''));
  }

  function getUrl(host, port, path, useSsl) {
    var protocol = (port == 443 || useSsl) ? 'https' : 'http';
    return  protocol + '://' + getServer(host, port) + (path ? path : '');
  }

  function getWsUrl(host, port, path, useSsl) {
    var protocol = (port == 443 || useSsl) ? 'wss' : 'ws';
    return  protocol + '://' + getServer(host, port) + (path ? path : '');
  }

  function processError(reject, data, url, status) {
    if (data && data.message) {
      reject(data);
    }
    else {
      if (status == 404) {
        reject({ucode: 404, message: 'Resource not found' + (url ? ' ('+url+')' : '')});
      }
      else if (url) {
        reject('Error while requesting [' + url + ']');
      }
      else {
        reject('Unknown error from node');
      }
    }
  }

  function prepare(url, params, config, callback) {
    var pkeys = [], queryParams = {}, newUri = url;
    if (typeof params == 'object') {
      pkeys = _.keys(params);
    }

    _.forEach(pkeys, function(pkey){
      var prevURI = newUri;
      newUri = newUri.replace(':' + pkey, params[pkey]);
      if (prevURI == newUri) {
        queryParams[pkey] = params[pkey];
      }
    });
    config.params = queryParams;
    return callback(newUri, config);
  }

  function getResource(host, port, path, useSsl, forcedTimeout) {
    // Make sure host is defined - fix #537
    if (!host) {
      return $q.reject('[http] invalid URL from host: ' + host);
    }
    var url = getUrl(host, port, path, useSsl);
    return function(params) {
      return $q(function(resolve, reject) {
        var config = {
          timeout: forcedTimeout || timeout,
          responseType: 'json'
        };

        prepare(url, params, config, function(url, config) {
            $http.get(url, config)
            .success(function(data, status, headers, config) {
              resolve(data);
            })
            .error(function(data, status, headers, config) {
              processError(reject, data, url, status);
            });
        });
      });
    };
  }

  function getResourceWithCache(host, port, path, useSsl, maxAge, autoRefresh, forcedTimeout) {
    var url = getUrl(host, port, path, useSsl);
    maxAge = maxAge || csCache.constants.LONG;
    //console.debug('[http] will cache ['+url+'] ' + maxAge + 'ms' + (autoRefresh ? ' with auto-refresh' : ''));

    return function(params) {
      return $q(function(resolve, reject) {
        var config = {
          timeout: forcedTimeout || timeout,
          responseType: 'json'
        };
        if (autoRefresh) { // redo the request if need
          config.cache = csCache.get(cachePrefix, maxAge, function (key, value) {
              console.debug('[http] Refreshing cache for ['+key+'] ');
              $http.get(key, config)
                .success(function (data) {
                  config.cache.put(key, data);
              });
            });
        }
        else {
          config.cache = csCache.get(cachePrefix, maxAge);
        }

        prepare(url, params, config, function(url, config) {
          $http.get(url, config)
            .success(function(data) {
              resolve(data);
            })
            .error(function(data, status) {
              processError(reject, data, url, status);
            });
        });
      });
    };
  }

  function postResource(host, port, path, useSsl, forcedTimeout) {
    var url = getUrl(host, port, path, useSsl);
    return function(data, params) {
      return $q(function(resolve, reject) {
        var config = {
          timeout: forcedTimeout || timeout,
          headers : {'Content-Type' : 'application/json;charset=UTF-8'}
        };

        prepare(url, params, config, function(url, config) {
            $http.post(url, data, config)
            .success(function(data) {
              resolve(data);
            })
            .error(function(data, status) {
              processError(reject, data, url, status);
            });
        });
      });
    };
  }

  function ws(host, port, path, useSsl, timeout) {
    if (!path) {
      console.error('calling csHttp.ws without path argument');
      throw 'calling csHttp.ws without path argument';
    }
    var uri = getWsUrl(host, port, path, useSsl);
    timeout = timeout || csSettings.data.timeout;

    function _waitOpen(self) {
      if (!self.delegate) throw new Error('Websocket not opened');
      if (self.delegate.readyState == 1) {
        return $q.when(self.delegate);
      }
      if (self.delegate.readyState == 3) {
        return $q.reject('Unable to connect to websocket ['+self.delegate.url+']');
      }
      console.debug('[http] Waiting websocket ['+self.path+'] opening...');

      if (self.waitDuration >= timeout) {
        console.debug("[http] Will retry openning websocket later...");
        self.waitRetryDelay = 2000; // 2 seconds
      }

      return $timeout(function(){
        self.waitDuration += self.waitRetryDelay;
        return _waitOpen(self);
      }, self.waitRetryDelay);
    }

    function _open(self, callback, params) {
      if (!self.delegate) {
        self.path = path;
        self.callbacks = [];
        self.waitDuration = 0;
        self.waitRetryDelay = 200;

        prepare(uri, params, {}, function(uri) {
          self.delegate = new WebSocket(uri);
          self.delegate.onerror = function(e) {
            self.delegate.readyState=3;
          };
          self.delegate.onmessage = function(e) {
            var obj = JSON.parse(e.data);
            _.forEach(self.callbacks, function(callback) {
              callback(obj);
            });
          };
          self.delegate.onopen = function(e) {
            console.debug('[http] Listening on websocket ['+self.path+']...');
            sockets.push(self);
            self.delegate.openTime = new Date().getTime();
          };
          self.delegate.onclose = function() {

            // Remove from sockets arrays
            var index = _.findIndex(sockets, function(socket){return socket.path === self.path;});
            if (index >= 0) {
              sockets.splice(index,1);
            }

            // If close event comes from Cesium
            if (self.delegate.closing) {
              self.delegate = null;
            }

            // If unexpected close event, reopen the socket (fix #535)
            else {
              console.debug('[http] Unexpected close of websocket ['+path+'] (open '+ (new Date().getTime() - self.delegate.openTime) +'ms ago): re-opening...');

              self.delegate = null;

              // Loop, but without the already registered callback
              _open(self, null, params);
            }
          };
        });
      }
      if (callback) self.callbacks.push(callback);
      return _waitOpen(self);
    }

    return {
      open: function(params) {
        return _open(this, null, params);
      },
      on: function(callback, params) {
        return _open(this, callback, params);
      },
      send: function(data) {
        var self = this;
        return _waitOpen(self)
          .then(function(){
            self.delegate.send(data);
          });
      },
      close: function() {
        var self = this;
        if (self.delegate) {
          self.delegate.closing = true;
          console.debug('[http] Closing websocket ['+self.path+']...');
          self.delegate.close();
          self.callbacks = [];
        }
      }
    };
  }

  function closeAllWs() {
    if (sockets.length > 0) {
      console.debug('[http] Closing all websocket...');
      _.forEach(sockets, function(sock) {
        sock.close();
      });
      sockets = []; // Reset socks list
    }
  }

  // See doc : https://gist.github.com/jlong/2428561
  function parseUri(uri) {
    var protocol;
    if (uri.startsWith('duniter://')) {
      protocol = 'duniter';
      uri = uri.replace('duniter://', 'http://');
    }

    var parser = document.createElement('a');
    parser.href = uri;

    var pathname = parser.pathname;
    if (pathname && pathname.startsWith('/')) {
      pathname = pathname.substring(1);
    }

    var result = {
      protocol: protocol ? protocol : parser.protocol,
      hostname: parser.hostname,
      host: parser.host,
      port: parser.port,
      username: parser.username,
      password: parser.password,
      pathname: pathname,
      search: parser.search,
      hash: parser.hash
    };
    parser.remove();
    return result;
  }

  /**
   * Open a URI (url, email, phone, ...)
   * @param event
   * @param link
   * @param type
   */
  function openUri(uri, options) {
    options = options || {};

    if (!uri.startsWith('http://') && !uri.startsWith('https://')) {
      var parts = parseUri(uri);

      if (!parts.protocol && options.type) {
        parts.protocol = (options.type == 'email')  ? 'mailto:' :
          ((options.type == 'phone') ? 'tel:' : '');
        uri = parts.protocol + uri;
      }

      // Check if device is enable, on spcial tel: or mailto: protocole
      var validProtocol = (parts.protocol == 'mailto:' || parts.protocol == 'tel:') && Device.enable;
      if (!validProtocol) {
        if (options.onError && typeof options.onError == 'function') {
          options.onError(uri);
        }
        return;
      }
    }

    // Note: If device enable, then target=_system will use InAppBrowser cordova plugin
    var openTarget = (options.target || (Device.enable ? '_system' : '_blank'));
    var openOptions;
    // If desktop, should always open in new window (no tabs)
    if (openTarget == '_blank' && Device.isDesktop() && $window.screen && $window.screen.width && $window.screen.height) {
      openOptions= "width={0},height={1},location=1,menubar=1,toolbar=1,resizable=1,scrollbars=1".format($window.screen.width/2, $window.screen.height/2);
    }
    var win = $window.open(uri,
      openTarget,
      openOptions);
    if (openOptions) {
      win.moveTo($window.screen.width/2/2, $window.screen.height/2/2);
      win.focus();
    }

  }

  // Get time (UTC)
  function getDateNow() {
    return Math.floor(moment().utc().valueOf() / 1000);
  }

  function isPositiveInteger(x) {
    // http://stackoverflow.com/a/1019526/11236
    return /^\d+$/.test(x);
  }

  /**
   * Compare two software version numbers (e.g. 1.7.1)
   * Returns:
   *
   *  0 if they're identical
   *  negative if v1 < v2
   *  positive if v1 > v2
   *  Nan if they in the wrong format
   *
   *  E.g.:
   *
   *  assert(version_number_compare("1.7.1", "1.6.10") > 0);
   *  assert(version_number_compare("1.7.1", "1.7.10") < 0);
   *
   *  "Unit tests": http://jsfiddle.net/ripper234/Xv9WL/28/
   *
   *  Taken from http://stackoverflow.com/a/6832721/11236
   */
  function compareVersionNumbers(v1, v2){
    var v1parts = v1.split('.');
    var v2parts = v2.split('.');

    // First, validate both numbers are true version numbers
    function validateParts(parts) {
      for (var i = 0; i < parts.length; ++i) {
        if (!isPositiveInteger(parts[i])) {
          return false;
        }
      }
      return true;
    }
    if (!validateParts(v1parts) || !validateParts(v2parts)) {
      return NaN;
    }

    for (var i = 0; i < v1parts.length; ++i) {
      if (v2parts.length === i) {
        return 1;
      }

      if (v1parts[i] === v2parts[i]) {
        continue;
      }
      if (v1parts[i] > v2parts[i]) {
        return 1;
      }
      return -1;
    }

    if (v1parts.length != v2parts.length) {
      return -1;
    }

    return 0;
  }

  function isVersionCompatible(minVersion, actualVersion) {
    // TODO: add implementation
    console.debug('[http] TODO: implement check version [{0}] compatible with [{1}]'.format(actualVersion, minVersion));
    return compareVersionNumbers(minVersion, actualVersion) <= 0;
  }

  var cache = angular.copy(csCache.constants);
  cache.clear = function() {
    console.debug('[http] Cleaning cache...');
    csCache.clear(cachePrefix);
  };

  return {
    get: getResource,
    getWithCache: getResourceWithCache,
    post: postResource,
    ws: ws,
    closeAllWs: closeAllWs,
    getUrl : getUrl,
    getServer: getServer,
    uri: {
      parse: parseUri,
      open: openUri
    },
    date: {
      now: getDateNow
    },
    version: {
      compare: compareVersionNumbers,
      isCompatible: isVersionCompatible
    },
    cache: cache
  };
}])
;

angular.module('cesium.storage.services', [ 'cesium.config'])


  .factory('sessionStorage', ['$window', '$q', function($window, $q) {
    'ngInject';

    var
      exports = {
        storage: $window.sessionStorage || {}
      };

    exports.put = function(key, value) {
      exports.storage[key] = value;
      return $q.when();
    };

    exports.get = function(key, defaultValue) {
      return $q.when(exports.storage[key] || defaultValue);
    };

    exports.setObject = function(key, value) {
      exports.storage[key] = JSON.stringify(value);
      return $q.when();
    };

    exports.getObject = function(key) {
      return $q.when(JSON.parse(exports.storage[key] || '{}'));
    };

    return exports;
  }])

  .factory('localStorage', ['$window', '$q', 'sessionStorage', function($window, $q, sessionStorage) {
    'ngInject';

    var
      appName = "Cesium",
      started = false,
      startPromise,
      isDevice = true, // default for device (override later)
      exports = {
        standard: {
          storage: null
        },
        secure: {
          storage: null
        }
      };

    // removeIf(device)
    // Use this workaround to avoid to wait ionicReady() event
    isDevice = false;
    // endRemoveIf(device)

    /* -- Use standard browser implementation -- */

    exports.standard.put = function(key, value) {
      exports.standard.storage[key] = value;
      return $q.when();
    };

    exports.standard.get = function(key, defaultValue) {
      return $q.when(exports.standard.storage[key] || defaultValue);
    };

    exports.standard.setObject = function(key, value) {
      exports.standard.storage[key] = JSON.stringify(value);
      return $q.when();
    };

    exports.standard.getObject = function(key) {
      return $q.when(JSON.parse(exports.standard.storage[key] || '{}'));
    };

    /* -- Use secure storage (using a cordova plugin) -- */

    // Set a value to the secure storage (or remove if value is not defined)
    exports.secure.put = function(key, value) {
      var deferred = $q.defer();
      if (angular.isDefined(value)) {
        exports.secure.storage.set(
          function (key) { deferred.resolve(); },
          function (err) { deferred.reject(err); },
          key, value);
      }
      // Remove
      else {
        exports.secure.storage.remove(
          function (key) { deferred.resolve(); },
          function (err) { deferred.reject(err); },
          key);
      }
      return deferred.promise;
    };

    // Get a value from the secure storage
    exports.secure.get = function(key, defaultValue) {
      var deferred = $q.defer();
      exports.secure.storage.get(
        function (value) {
          if (!value && defaultValue) {
            deferred.resolve(defaultValue);
          }
          else {
            deferred.resolve(value);
          }
        },
        function (err) { deferred.reject(err); },
        key);
      return deferred.promise;
    };

    // Set a object to the secure storage
    exports.secure.setObject = function(key, value) {
      return exports.secure.put(key, value ? JSON.stringify(value) : undefined);
    };

    // Get a object from the secure storage
    exports.secure.getObject = function(key) {
      return exports.secure.storage.get(key)
        .then(function(value) {
          return (value && JSON.parse(value)) || {};
        });
    };

    function initStandardStorage() {
      // use local browser storage
      if ($window.localStorage) {
        console.debug('[storage] Starting {local} storage...');
        exports.standard.storage = $window.localStorage;
        // Set standard storage as default
        _.forEach(_.keys(exports.standard), function(key) {
          exports[key] = exports.standard[key];
        });
      }

      // Fallback to session storage (locaStorage could have been disabled on some browser)
      else {
        console.debug('[storage] Starting {session} storage...');
        // Set standard storage as default
        _.forEach(_.keys(sessionStorage), function(key) {
          exports[key] = sessionStorage[key];
        });
      }
      return $q.when();
    }

    function initSecureStorage() {
      console.debug('[storage] Starting {secure} storage...');
      // Set secure storage as default
      _.forEach(_.keys(exports.secure), function(key) {
        exports[key] = exports.secure[key];
      });

      var deferred = $q.defer();

      // No secure storage plugin: fall back to standard storage
      if (!cordova.plugins || !cordova.plugins.SecureStorage) {
        initStandardStorage();
        deferred.resolve();
      }
      else {

        exports.secure.storage = new cordova.plugins.SecureStorage(
          function () {
            deferred.resolve();
          },
          function (err) {
            console.error('[storage] Could not use secure storage. Will use standard.', err);
            initStandardStorage();
            deferred.resolve();
          },
          appName);
      }
      return deferred.promise;
    }

    exports.isStarted = function() {
      return started;
    };

    exports.ready = function() {
      if (started) return $q.when();
      return startPromise || start();
    };

    function start() {
      if (startPromise) return startPromise;

      var now = new Date().getTime();

      // Use Cordova secure storage plugin
      if (isDevice) {
        console.debug("[storage] Starting secure storage...");
        startPromise = initSecureStorage();
      }

      // Use default browser local storage
      else {
        startPromise = initStandardStorage();
      }

      return startPromise
        .then(function() {
          console.debug('[storage] Started in ' + (new Date().getTime() - now) + 'ms');
          started = true;
          startPromise = null;
        });
    }

    // default action
    start();

    return exports;
  }])


;

var App;

angular.module('cesium.device.services', ['cesium.utils.services', 'cesium.settings.services'])

  .factory('Device',
    ['$rootScope', '$translate', '$ionicPopup', '$q', 'ionicReady', function($rootScope, $translate, $ionicPopup, $q,
      ionicReady) {
      'ngInject';

      var
        CONST = {
          MAX_HEIGHT: 400,
          MAX_WIDTH: 400
        },
        exports = {
          // workaround to quickly no is device or not (even before the ready() event)
          enable: true
        },
        cache = {},
        started = false,
        startPromise;

      // removeIf(device)
      // workaround to quickly no is device or not (even before the ready() event)
      exports.enable = false;
      // endRemoveIf(device)

      function getPicture(options) {
        if (!exports.camera.enable) {
          return $q.reject("Camera not enable. Please call 'ionicReady()' once before use (e.g in app.js).");
        }

        // Options is the sourceType by default
        if (options && (typeof options === "string")) {
          options = {
            sourceType: options
          };
        }
        options = options || {};

        // Make sure a source type has been given (if not, ask user)
        if (angular.isUndefined(options.sourceType)) {
          return $translate(['SYSTEM.PICTURE_CHOOSE_TYPE', 'SYSTEM.BTN_PICTURE_GALLERY', 'SYSTEM.BTN_PICTURE_CAMERA'])
            .then(function(translations){
              return $ionicPopup.show({
                title: translations['SYSTEM.PICTURE_CHOOSE_TYPE'],
                buttons: [
                  {
                    text: translations['SYSTEM.BTN_PICTURE_GALLERY'],
                    type: 'button',
                    onTap: function(e) {
                      return navigator.camera.PictureSourceType.PHOTOLIBRARY;
                    }
                  },
                  {
                    text: translations['SYSTEM.BTN_PICTURE_CAMERA'],
                    type: 'button button-positive',
                    onTap: function(e) {
                      return navigator.camera.PictureSourceType.CAMERA;
                    }
                  }
                ]
              })
              .then(function(sourceType){
                console.info('[camera] User select sourceType:' + sourceType);
                options.sourceType = sourceType;
                return exports.camera.getPicture(options);
              });
            });
        }

        options.quality = options.quality || 50;
        options.destinationType = options.destinationType || navigator.camera.DestinationType.DATA_URL;
        options.encodingType = options.encodingType || navigator.camera.EncodingType.PNG;
        options.targetWidth = options.targetWidth || CONST.MAX_WIDTH;
        options.targetHeight = options.targetHeight || CONST.MAX_HEIGHT;
        return $cordovaCamera.getPicture(options);
      }

      function scan(n) {
        if (!exports.enable) {
          return $q.reject("Barcode scanner not enable. Please call 'ionicReady()' once before use (e.g in app.js).");
        }
        var deferred = $q.defer();
        cordova.plugins.barcodeScanner.scan(
          function(result) {
            console.debug('[device] bar code result', result);
            if (!result.cancelled) {
              deferred.resolve(result.text); // make sure to convert into String
            }
            else {
              deferred.resolve();
            }
          },
          function(err) {
            console.error('[device] Error while using barcode scanner -> ' + err);
            deferred.reject(err);
          },
          n);
        return deferred.promise;
      }

      function copy(text, callback) {
        if (!exports.enable) {
          return $q.reject('Device disabled');
        }
        var deferred = $q.defer();
        $cordovaClipboard
          .copy(text)
          .then(function () {
            // success
            if (callback) {
              callback();
            }
            deferred.resolve();
          }, function () {
            // error
            deferred.reject({message: 'ERROR.COPY_CLIPBOARD'});
          });
        return deferred.promise;
      }

      exports.clipboard = {copy: copy};
      exports.camera = {
          getPicture : getPicture,
          scan: function(n){
            console.warn('Deprecated use of Device.camera.scan(). Use Device.barcode.scan() instead');
            return scan(n);
          }
        };
      exports.barcode = {
        enable : false,
        scan: scan
      };
      exports.keyboard = {
        enable: false,
        close: function() {
          if (!exports.keyboard.enable) return;
          cordova.plugins.Keyboard.close();
        }
      };

      // Numerical keyboard - fix #30
      exports.keyboard.digit = {
        settings: {
          bindModel: function(modelScope, modelPath, settings) {
            settings = settings || {};
            modelScope = modelScope || $rootScope;
            var getModelValue = function() {
              return (modelPath||'').split('.').reduce(function(res, path) {
                return res ? res[path] : undefined;
              }, modelScope);
            };
            var setModelValue = function(value) {
              var paths = (modelPath||'').split('.');
              var property = paths.length && paths[paths.length-1];
              paths.reduce(function(res, path) {
                if (path == property) {
                  res[property] = value;
                  return;
                }
                return res[path];
              }, modelScope);
            };

            settings.action = settings.action || function(number) {
                setModelValue((getModelValue() ||'') + number);
              };
            if (settings.decimal) {
              settings.decimalSeparator = settings.decimalSeparator || '.';
              settings.leftButton = settings.leftButton = {
                html: '<span>.</span>',
                action: function () {
                  var text = getModelValue() || '';
                  // only one '.' allowed
                  if (text.indexOf(settings.decimalSeparator) >= 0) return;
                  // Auto add zero when started with '.'
                  if (!text.trim().length) {
                    text = '0';
                  }
                  setModelValue(text + settings.decimalSeparator);
                }
              };
            }
            settings.rightButton = settings.rightButton || {
                html: '<i class="icon ion-backspace-outline"></i>',
                action: function() {
                  var text = getModelValue();
                  if (text && text.length) {
                    text = text.slice(0, -1);
                    setModelValue(text);
                  }
                }
              };
            return settings;
          }
        }
      };

      exports.isIOS = function() {
        return !!navigator.userAgent.match(/iPhone | iPad | iPod/i) || ionic.Platform.isIOS();
      };

      exports.isDesktop = function() {
        if (!angular.isDefined(cache.isDesktop)) {
          try {
            // Should have NodeJs and NW
            cache.isDesktop = !exports.enable && !!process && !!App;
          } catch (err) {
            cache.isDesktop = false;
          }
        }
        return cache.isDesktop;
      };

      exports.isWeb = function() {
        return !exports.enable && !exports.isDesktop();
      };

      exports.ready = function() {
        if (started) return $q.when();
        return startPromise || exports.start();
      };

      exports.start = function() {

        startPromise = ionicReady()
          .then(function(){

            exports.enable = window.cordova && cordova && cordova.plugins;

            if (exports.enable){
              exports.camera.enable = !!navigator.camera;
              exports.keyboard.enable = cordova && cordova.plugins && !!cordova.plugins.Keyboard;
              exports.barcode.enable = cordova && cordova.plugins && !!cordova.plugins.barcodeScanner;
              exports.clipboard.enable = cordova && cordova.plugins && !!cordova.plugins.clipboard;

              if (exports.keyboard.enable) {
                angular.extend(exports.keyboard, cordova.plugins.Keyboard);
              }

              console.debug('[device] Ionic platform ready, with [camera: {0}] [barcode scanner: {1}] [keyboard: {2}] [clipboard: {3}]'
                .format(exports.camera.enable, exports.barcode.enable, exports.keyboard.enable, exports.clipboard.enable));

              if (cordova.InAppBrowser) {
                console.debug('[device] Enabling InAppBrowser');
              }
            }
            else {
              console.debug('[device] Ionic platform ready - no device detected.');
            }

            started = true;
            startPromise = null;
          });

        return startPromise;
      };

      return exports;
    }])

  ;


angular.module('cesium.currency.services', ['ngApi', 'cesium.bma.services'])

.factory('csCurrency', ['$rootScope', '$q', '$timeout', 'BMA', 'Api', function($rootScope, $q, $timeout, BMA, Api) {
  'ngInject';

  function factory(id, BMA) {
    var
      constants = {
        // Avoid to many call on well known currencies
        WELL_KNOWN_CURRENCIES: {
          g1: {
            firstBlockTime: 1488987127
          }
        }
      },

      data = {},
      started = false,
      startPromise,
      listeners,
      api = new Api(this, "csCurrency-" + id);

    function powBase(amount, base) {
      return base <= 0 ? amount : amount * Math.pow(10, base);
    }

    function resetData() {
      data.name = null;
      data.parameters = null;
      data.firstBlockTime = null;
      data.membersCount = null;
      data.cache = {};
      data.node = BMA;
      data.currentUD = null;
      started = false;
      startPromise = undefined;
      api.data.raise.reset(data);
    }

    function loadData() {

      // Load currency from default node
      return $q.all([

        // get parameters
        loadParameters()
          .then(function(parameters) {
            // load first block info
            return loadFirstBlock(parameters.currency);
          }),

        // get current UD
        loadCurrentUD(),

        // call extensions
        api.data.raisePromise.load(data)
      ])
      .catch(function(err) {
        resetData();
        throw err;
      });
    }

    function loadParameters() {
      return BMA.blockchain.parameters()
        .then(function(res){
          data.name = res.currency;
          data.parameters = res;
          return res;
        });
    }

    function loadFirstBlock(currencyName) {
      // Well known currencies
      if (constants.WELL_KNOWN_CURRENCIES[currencyName]){
        angular.merge(data, constants.WELL_KNOWN_CURRENCIES[currencyName]);
        return $q.when();
      }

      return BMA.blockchain.block({block:0})
        .then(function(json) {
          // Need by graph plugin
          data.firstBlockTime = json.medianTime;
        })
        .catch(function(err) {
          // Special case, when currency not started yet
          if (err && err.ucode === BMA.errorCodes.BLOCK_NOT_FOUND) {
            data.firstBlockTime = 0;
            data.initPhase = true;
            console.warn('[currency] Blockchain not launched: Enable init phase mode');
            return;
          }
          throw err;
        });
    }

    function loadCurrentUD() {
      return BMA.blockchain.stats.ud()
        .then(function(res){
          // Special case for currency init
          if (!res.result.blocks.length) {
            data.currentUD = data.parameters ? data.parameters.ud0 : -1;
            return data.currentUD ;
          }
          else {
            var lastBlockWithUD = res.result.blocks[res.result.blocks.length - 1];
            return BMA.blockchain.block({ block: lastBlockWithUD })
              .then(function(block){
                data.currentUD = powBase(block.dividend, block.unitbase);
                return data.currentUD;
              })
              .catch(function(err) {
                console.error("[currency] Unable to load last block with UD, with number {0}".format(lastBlockWithUD));
                data.currentUD = null;
                throw err;
              });
          }
        })
        .catch(function(err) {
          data.currentUD = null;
          throw err;
        });
    }

    function getData() {

      if (started) { // load only once
        return $q.when(data);
      }

      // Previous load not finished: return the existing promise - fix #452
      return startPromise || start();
    }

    function getDataField(field) {
      return function() {
        if (started) { // load only once
          return $q.when(data[field]);
        }

        // Previous load not finished: return the existing promise - fix #452
        return startPromise || start() // load only once
            .then(function(){
              return data[field];
            });
      };
    }

    function onBlock(json) {
      var block = new Block(json);
      block.cleanData(); // Remove unused content (arrays...) and keep items count

      //console.debug('[currency] Received new block', block);
      console.debug('[currency] Received new block [' + block.number + '-' + block.hash + ']');

      data.currentBlock = block;
      data.currentBlock.receivedAt = new Date().getTime() / 1000;

      data.medianTime = block.medianTime;
      data.membersCount = block.membersCount;

      // Update UD
      if (block.dividend) {
        data.currentUD = block.dividend;
      }

      // Dispatch to extensions
      api.data.raise.newBlock(block);
    }

    function addListeners() {
      // open web socket on block
      var wsBlock = BMA.websocket.block();
      wsBlock.on(onBlock);

      listeners = [
        // Listen if node changed
        BMA.api.node.on.restart($rootScope, restart, this),
        wsBlock.close
      ];
    }

    function removeListeners() {
      _.forEach(listeners, function(remove){
        remove();
      });
      listeners = [];
    }

    function ready() {
      if (started) return $q.when(data);
      return startPromise || start();
    }

    function stop() {
      console.debug('[currency] Stopping...');
      removeListeners();
      resetData();
    }

    function restart() {
      stop();
      return $timeout(start, 200);
    }

    function start() {
      console.debug('[currency] Starting...');
      var now = new Date().getTime();

      startPromise = BMA.ready()

        // Load data
        .then(loadData)

        // Emit ready event
        .then(function() {
          addListeners();

          console.debug('[currency] Started in ' + (new Date().getTime() - now) + 'ms');

          started = true;
          startPromise = null;

          // Emit event (used by plugins)
          api.data.raise.ready(data);
        })
        .then(function(){
          return data;
        });

      return startPromise;
    }

    var currentBlockField = getDataField('currentBlock');

    function getCurrent(cache) {
      // Get field (and make sure service is started)
      return currentBlockField()

        .then(function(currentBlock) {
          if (cache) {
            var now = new Date().getTime() / 1000;
            if (currentBlock && (currentBlock.receivedAt - now) < 60/*1min*/) {
              console.debug('[currency] find current block in cache: use it');
              return currentBlock;
            }

            // TODO : Should never occured if block event listener works !
            console.warn('[currency] No current block in cache: get it from network');
          }

          return BMA.blockchain.current()
            .catch(function(err){
              // Special case for currency init (root block not exists): use fixed values
              if (err && err.ucode == BMA.errorCodes.NO_CURRENT_BLOCK) {
                return {number: 0, hash: BMA.constants.ROOT_BLOCK_HASH, medianTime: Math.trunc(new Date().getTime() / 1000)};
              }
              throw err;
            })
            .then(function(current) {
              data.currentBlock = current;
              data.currentBlock.receivedAt = now;
              return current;
            });
        });
    }

    // TODO register new block event, to get new UD value

    // Register extension points
    api.registerEvent('data', 'ready');
    api.registerEvent('data', 'load');
    api.registerEvent('data', 'reset');
    api.registerEvent('data', 'newBlock');

    // init data
    resetData();

    // Default action
    //start();

    return {
      ready: ready,
      start: start,
      stop: stop,
      data: data,
      get: getData,
      parameters: getDataField('parameters'),
      currentUD: getDataField('currentUD'),
      blockchain: {
        current: getCurrent
      },
      // api extension
      api: api,
      // deprecated methods
      default: function() {
        console.warn('[currency] \'csCurrency.default()\' has been DEPRECATED - Please use \'csCurrency.get()\' instead.');
        return getData();
      }
    };
  }

  var service = factory('default', BMA);
  service.instance = factory;
  return service;
}]);

//var Base58, Base64, scrypt_module_factory = null, nacl_factory = null;

angular.module('cesium.bma.services', ['ngApi', 'cesium.http.services', 'cesium.settings.services'])

.factory('BMA', ['$q', '$window', '$rootScope', '$timeout', 'Api', 'Device', 'csConfig', 'csSettings', 'csHttp', function($q, $window, $rootScope, $timeout, Api, Device, csConfig, csSettings, csHttp) {
  'ngInject';

  function BMA(host, port, useSsl, useCache) {

    var
      // TX output conditions
      SIG = "SIG\\(([0-9a-zA-Z]{43,44})\\)",
      XHX = 'XHX\\(([A-F0-9]{1,64})\\)',
      CSV = 'CSV\\(([0-9]{1,8})\\)',
      CLTV = 'CLTV\\(([0-9]{1,10})\\)',
      OUTPUT_FUNCTION = SIG+'|'+XHX+'|'+CSV+'|'+CLTV,
      OUTPUT_OPERATOR = '(&&)|(\\|\\|)',
      OUTPUT_FUNCTIONS = OUTPUT_FUNCTION+'([ ]*' + OUTPUT_OPERATOR + '[ ]*' + OUTPUT_FUNCTION +')*',
      OUTPUT_OBJ = 'OBJ\\(([0-9]+)\\)',
      OUTPUT_OBJ_OPERATOR = OUTPUT_OBJ + '[ ]*' + OUTPUT_OPERATOR + '[ ]*' + OUTPUT_OBJ,
      REGEX_ENDPOINT_PARAMS = "( ([a-z_][a-z0-9-_.ğĞ]*))?( ([0-9.]+))?( ([0-9a-f:]+))?( ([0-9]+))( (.+))?",
      regexp = {
        USER_ID: "[A-Za-z0-9_-]+",
        CURRENCY: "[A-Za-z0-9_-]+",
        PUBKEY: "[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{43,44}",
        COMMENT: "[ a-zA-Z0-9-_:/;*\\[\\]()?!^\\+=@&~#{}|\\\\<>%.]*",
        // duniter://[uid]:[pubkey]@[host]:[port]
        URI_WITH_AT: "duniter://(?:([A-Za-z0-9_-]+):)?([123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{43,44})@([a-zA-Z0-9-.]+.[ a-zA-Z0-9-_:/;*?!^\\+=@&~#|<>%.]+)",
        URI_WITH_PATH: "duniter://([a-zA-Z0-9-.]+.[a-zA-Z0-9-_:.]+)/([123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{43,44})(?:/([A-Za-z0-9_-]+))?",
        BMA_ENDPOINT: "BASIC_MERKLED_API" + REGEX_ENDPOINT_PARAMS,
        BMAS_ENDPOINT: "BMAS" + REGEX_ENDPOINT_PARAMS,
        WS2P_ENDPOINT: "WS2P ([a-f0-9]{8})"+ REGEX_ENDPOINT_PARAMS,
        BMATOR_ENDPOINT: "BMATOR ([a-z0-9-_.]*|[0-9.]+|[0-9a-f:]+.onion)(?: ([0-9]+))?",
        WS2PTOR_ENDPOINT: "WS2PTOR ([a-f0-9]{8}) ([a-z0-9-_.]*|[0-9.]+|[0-9a-f:]+.onion)(?: ([0-9]+))?(?: (.+))?"
      },
      errorCodes = {
        REVOCATION_ALREADY_REGISTERED: 1002,
        HTTP_LIMITATION: 1006,
        IDENTITY_SANDBOX_FULL: 1007,
        NO_MATCHING_IDENTITY: 2001,
        UID_ALREADY_USED: 2003,
        NO_MATCHING_MEMBER: 2004,
        NO_IDTY_MATCHING_PUB_OR_UID: 2021,
        WRONG_SIGNATURE_MEMBERSHIP: 2006,
        MEMBERSHIP_ALREADY_SEND: 2007,
        NO_CURRENT_BLOCK: 2010,
        BLOCK_NOT_FOUND: 2011,
        SOURCE_ALREADY_CONSUMED: 2015,
        TX_INPUTS_OUTPUTS_NOT_EQUAL: 2024,
        TX_OUTPUT_SUM_NOT_EQUALS_PREV_DELTAS: 2025,
        TX_ALREADY_PROCESSED: 2030
      },
      constants = {
        PROTOCOL_VERSION: 10,
        ROOT_BLOCK_HASH: 'E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855',
        LIMIT_REQUEST_COUNT: 5, // simultaneous async request to a Duniter node
        LIMIT_REQUEST_DELAY: 1000, // time (in second) to wait between to call of a rest request
        regex: regexp, // deprecated
        regexp: regexp
      },
      listeners,
      that = this;

    that.date = {now: csHttp.date.now};
    that.api = new Api(this, 'BMA-' + that.server);
    that.started = false;
    that.init = init;

    // Allow to force SSL connection with port different from 443
    that.forceUseSsl = (csConfig.httpsMode === 'true' || csConfig.httpsMode === true || csConfig.httpsMode === 'force') ||
    ($window.location && $window.location.protocol === 'https:') ? true : false;
    if (that.forceUseSsl) {
      console.debug('[BMA] Enable SSL (forced by config or detected in URL)');
    }


    if (host) {
      init(host, port, useSsl, useCache);
    }
    that.useCache = useCache; // need here because used in get() function

    function init(host, port, useSsl, useCache) {
      if (that.started) that.stop();
      that.alive = false;
      that.cache = _emptyCache();

      // Use settings as default, if exists
      if (csSettings.data && csSettings.data.node) {
        host = host || csSettings.data.node.host;
        port = port || csSettings.data.node.port;

        useSsl = angular.isDefined(useSsl) ? useSsl : (port == 443 || csSettings.data.node.useSsl || that.forceUseSsl);
        useCache =  angular.isDefined(useCache) ? useCache : true;
      }

      if (!host) {
        return; // could not init yet
      }
      that.host = host;
      that.port = port || 80;
      that.useSsl = angular.isDefined(useSsl) ? useSsl : (that.port == 443 || that.forceUseSsl);
      that.useCache = angular.isDefined(useCache) ? useCache : false;
      that.server = csHttp.getServer(host, port);
      that.url = csHttp.getUrl(host, port, ''/*path*/, useSsl);
    }

    function exact(regexpContent) {
      return new RegExp("^" + regexpContent + "$");
    }

    function test(regexpContent) {
      return new RegExp(regexpContent);
    }

    function _emptyCache() {
      return {
        getByPath: {},
        postByPath: {},
        wsByPath: {}
      };
    }

    function closeWs() {
      console.warn('[BMA] Closing all websockets...');
      _.keys(that.cache.wsByPath).forEach(function(key) {
        var sock = that.cache.wsByPath[key];
        sock.close();
      });
      that.cache.wsByPath = {};
    }

    that.cleanCache = function() {
      console.debug('[BMA] Cleaning requests cache...');
      closeWs();
      that.cache = _emptyCache();
    };

    get = function (path, cacheTime) {

      cacheTime = that.useCache && cacheTime;
      var cacheKey = path + (cacheTime ? ('#'+cacheTime) : '');

      var getRequest = function(params) {

        if (!that.started) {
          if (!that._startPromise) {
            console.error('[BMA] Trying to get [{0}] before start()...'.format(path));
          }
          return that.ready().then(function() {
            return getRequest(params);
          });
        }

        var request = that.cache.getByPath[cacheKey];
        if (!request) {
          if (cacheTime) {
            request = csHttp.getWithCache(that.host, that.port, path, that.useSsl, cacheTime);
          }
          else {
            request = csHttp.get(that.host, that.port, path, that.useSsl);
          }
          that.cache.getByPath[cacheKey] = request;
        }
        return request(params);
      };

      return getRequest;
    };

    post = function(path) {
      var postRequest = function(obj, params) {
        if (!that.started) {
          if (!that._startPromise) {
            console.error('[BMA] Trying to post [{0}] before start()...'.format(path));
          }
          return that.ready().then(function() {
            return postRequest(obj, params);
          });
        }

        var request = that.cache.postByPath[path];
        if (!request) {
          request =  csHttp.post(that.host, that.port, path, that.useSsl);
          that.cache.postByPath[path] = request;
        }
        return request(obj, params);
      };

      return postRequest;
    };

    ws = function(path) {
      return function() {
        var sock = that.cache.wsByPath[path];
        if (!sock) {
          sock =  csHttp.ws(that.host, that.port, path, that.useSsl);

          // Override close methods (add a usage counter)
          sock._counter = 1;
          var inheritedClose = sock.close;
          sock.close = function() {
            sock._counter--;
            if (sock._counter <= 0) {
              console.debug('[BMA] Closing websocket ['+path+']');
              inheritedClose();
              delete that.cache.wsByPath[path];
            }
          };

          that.cache.wsByPath[path] = sock;
        }
        else {
          sock._counter++;
        }
        return sock;
      };
    };

    that.isAlive = function() {
      return csHttp.get(that.host, that.port, '/node/summary', that.useSsl)()
        .then(function(json) {
          var isDuniter = json && json.duniter && json.duniter.software == 'duniter' && json.duniter.version;
          var isCompatible = isDuniter && csHttp.version.isCompatible(csSettings.data.minVersion, json.duniter.version);
          if (isDuniter && !isCompatible) {
            console.error('[BMA] Uncompatible version [{0}] - expected at least [{1}]'.format(json.duniter.version, csSettings.data.minVersion));
          }
          return isCompatible;
        })
        .catch(function() {
          return false;
        });
    };

    function removeListeners() {
      _.forEach(listeners, function(remove){
        remove();
      });
      listeners = [];
    }

    function addListeners() {
      listeners = [
        // Listen if node changed
        csSettings.api.data.on.changed($rootScope, onSettingsChanged, this)
      ];
    }

    function onSettingsChanged(settings) {

      var server = csHttp.getUrl(settings.node.host, settings.node.port, ''/*path*/, settings.node.useSsl);
      var hasChanged = (server != that.url);
      if (hasChanged) {
        init(settings.node.host, settings.node.port, settings.node.useSsl, that.useCache);
        that.restart();
      }
    }

    that.isStarted = function() {
      return that.started;
    };

    that.ready = function() {
      if (that.started) return $q.when(true);
      return that._startPromise || that.start();
    };

    that.start = function() {
      if (that._startPromise) return that._startPromise;
      if (that.started) return $q.when(that.alive);

      if (!that.host) {
        return csSettings.ready()
          .then(function() {
            that.init();

            // Always enable cache
            that.useCache = true;

            return that.start(); // recursive call
          });
      }

      if (that.useSsl) {
        console.debug('[BMA] Starting [{0}] (SSL on)...'.format(that.server));
      }
      else {
        console.debug('[BMA] Starting [{0}]...'.format(that.server));
      }

      var now = new Date().getTime();

      that._startPromise = $q.all([
          csSettings.ready,
          that.isAlive()
        ])
        .then(function(res) {
          that.alive = res[1];
          if (!that.alive) {
            console.error('[BMA] Could not start [{0}]: node unreachable'.format(that.server));
            that.started = true;
            delete that._startPromise;
            return false;
          }

          // Add listeners
          if (!listeners || listeners.length === 0) {
            addListeners();
          }
          console.debug('[BMA] Started in '+(new Date().getTime()-now)+'ms');

          that.api.node.raise.start();
          that.started = true;
          delete that._startPromise;
          return true;
        });
      return that._startPromise;
    };

    that.stop = function() {
      console.debug('[BMA] Stopping...');
      removeListeners();
      csHttp.cache.clear();
      that.cleanCache();
      that.alive = false;
      that.started = false;
      delete that._startPromise;
      that.api.node.raise.stop();
    };

    that.restart = function() {
      that.stop();
      return $timeout(that.start, 200)
        .then(function(alive) {
          if (alive) {
            that.api.node.raise.restart();
          }
          return alive;
        });
    };

    that.api.registerEvent('node', 'start');
    that.api.registerEvent('node', 'stop');
    that.api.registerEvent('node', 'restart');

    var exports = {
      errorCodes: errorCodes,
      constants: constants,
      regexp: {
        USER_ID: exact(regexp.USER_ID),
        COMMENT: exact(regexp.COMMENT),
        PUBKEY: exact(regexp.PUBKEY),
        CURRENCY: exact(regexp.CURRENCY),
        URI: exact(regexp.URI),
        BMA_ENDPOINT: exact(regexp.BMA_ENDPOINT),
        BMAS_ENDPOINT: exact(regexp.BMAS_ENDPOINT),
        WS2P_ENDPOINT: exact(regexp.WS2P_ENDPOINT),
        BMATOR_ENDPOINT: exact(regexp.BMATOR_ENDPOINT),
        WS2PTOR_ENDPOINT: exact(regexp.WS2PTOR_ENDPOINT),
        // TX output conditions
        TX_OUTPUT_SIG: exact(SIG),
        TX_OUTPUT_FUNCTION: test(OUTPUT_FUNCTION),
        TX_OUTPUT_OBJ_OPERATOR_AND: test(OUTPUT_OBJ + '([ ]*&&[ ]*(' + OUTPUT_OBJ + '))+'),
        TX_OUTPUT_OBJ_OPERATOR_OR: test(OUTPUT_OBJ + '([ ]*\\|\\|[ ]*(' + OUTPUT_OBJ + '))+'),
        TX_OUTPUT_OBJ: test(OUTPUT_OBJ),
        TX_OUTPUT_OBJ_OPERATOR: test(OUTPUT_OBJ_OPERATOR),
        TX_OUTPUT_OBJ_PARENTHESIS: test('\\(('+OUTPUT_OBJ+')\\)'),
        TX_OUTPUT_FUNCTIONS: test(OUTPUT_FUNCTIONS)
      },
      node: {
        summary: get('/node/summary', csHttp.cache.LONG),
        same: function(host2, port2) {
          return host2 == that.host && ((!that.port && !port2) || (that.port == port2||80));
        },
      },
      network: {
        peering: {
          self: get('/network/peering'),
          peers: get('/network/peering/peers')
        },
        peers: get('/network/peers'),
        ws2p: {
          info: get('/network/ws2p/info'),
          heads: get('/network/ws2p/heads')
        }
      },
      wot: {
        lookup: get('/wot/lookup/:search'),
        certifiedBy: get('/wot/certified-by/:pubkey'),
        certifiersOf: get('/wot/certifiers-of/:pubkey'),
        member: {
          all: get('/wot/members', csHttp.cache.LONG),
          pending: get('/wot/pending', csHttp.cache.SHORT)
        },
        requirements: get('/wot/requirements/:pubkey'),
        add: post('/wot/add'),
        certify: post('/wot/certify'),
        revoke: post('/wot/revoke')
      },
      blockchain: {
        parameters: get('/blockchain/parameters', csHttp.cache.LONG),
        block: get('/blockchain/block/:block', csHttp.cache.SHORT),
        blocksSlice: get('/blockchain/blocks/:count/:from'),
        current: get('/blockchain/current'),
        membership: post('/blockchain/membership'),
        stats: {
          ud: get('/blockchain/with/ud', csHttp.cache.SHORT),
          tx: get('/blockchain/with/tx'),
          newcomers: get('/blockchain/with/newcomers'),
          hardship: get('/blockchain/hardship/:pubkey'),
          difficulties: get('/blockchain/difficulties')
        }
      },
      tx: {
        sources: get('/tx/sources/:pubkey'),
        process: post('/tx/process'),
        history: {
          all: get('/tx/history/:pubkey'),
          times: get('/tx/history/:pubkey/times/:from/:to', csHttp.cache.LONG),
          timesNoCache: get('/tx/history/:pubkey/times/:from/:to'),
          blocks: get('/tx/history/:pubkey/blocks/:from/:to', csHttp.cache.LONG),
          pending: get('/tx/history/:pubkey/pending')
        }
      },
      ud: {
        history: get('/ud/history/:pubkey')
      },
      uri: {},
      version: {},
      raw: {}
    };
    exports.regex = exports.regexp; // deprecated

    exports.tx.parseUnlockCondition = function(unlockCondition) {

      //console.debug('[BMA] Parsing unlock condition: {0}.'.format(unlockCondition));
      var convertedOutput = unlockCondition;
      var treeItems = [];
      var treeItem;
      var treeItemId;
      var childrenContent;
      var childrenMatches;
      var functions = {};

      // Parse functions, then replace with an 'OBJ()' generic function, used to build a object tree
      var matches = exports.regexp.TX_OUTPUT_FUNCTION.exec(convertedOutput);
      while(matches) {
        treeItem = {};
        treeItemId = 'OBJ(' + treeItems.length + ')';
        treeItem.type = convertedOutput.substr(matches.index, matches[0].indexOf('('));
        treeItem.value = matches[1] || matches[2] || matches[3] || matches[4]; // get value from regexp OUTPUT_FUNCTION
        treeItems.push(treeItem);

        functions[treeItem.type] = functions[treeItem.type]++ || 1;

        convertedOutput = convertedOutput.replace(matches[0], treeItemId);
        matches = exports.regexp.TX_OUTPUT_FUNCTION.exec(convertedOutput);
      }

      var loop = true;
      while(loop) {
        // Parse AND operators
        matches = exports.regexp.TX_OUTPUT_OBJ_OPERATOR_AND.exec(convertedOutput);
        loop = !!matches;
        while (matches) {
          treeItem = {};
          treeItemId = 'OBJ(' + treeItems.length + ')';
          treeItem.type = 'AND';
          treeItem.children = [];
          treeItems.push(treeItem);

          childrenContent = matches[0];
          childrenMatches = exports.regexp.TX_OUTPUT_OBJ.exec(childrenContent);
          while(childrenMatches) {

            treeItem.children.push(treeItems[childrenMatches[1]]);
            childrenContent = childrenContent.replace(childrenMatches[0], '');
            childrenMatches = exports.regexp.TX_OUTPUT_OBJ.exec(childrenContent);
          }

          convertedOutput = convertedOutput.replace(matches[0], treeItemId);
          matches = exports.regexp.TX_OUTPUT_OBJ_OPERATOR_AND.exec(childrenContent);
        }

        // Parse OR operators

        matches = exports.regexp.TX_OUTPUT_OBJ_OPERATOR_OR.exec(convertedOutput);
        loop = loop || !!matches;
        while (matches) {
          treeItem = {};
          treeItemId = 'OBJ(' + treeItems.length + ')';
          treeItem.type = 'OR';
          treeItem.children = [];
          treeItems.push(treeItem);

          childrenContent = matches[0];
          childrenMatches = exports.regexp.TX_OUTPUT_OBJ.exec(childrenContent);
          while(childrenMatches) {
            treeItem.children.push(treeItems[childrenMatches[1]]);
            childrenContent = childrenContent.replace(childrenMatches[0], '');
            childrenMatches = exports.regexp.TX_OUTPUT_OBJ.exec(childrenContent);
          }

          convertedOutput = convertedOutput.replace(matches[0], treeItemId);
          matches = exports.regexp.TX_OUTPUT_OBJ_OPERATOR_AND.exec(convertedOutput);
        }

        // Remove parenthesis
        matches = exports.regexp.TX_OUTPUT_OBJ_PARENTHESIS.exec(convertedOutput);
        loop = loop || !!matches;
        while (matches) {
          convertedOutput = convertedOutput.replace(matches[0], matches[1]);
          matches = exports.regexp.TX_OUTPUT_OBJ_PARENTHESIS.exec(convertedOutput);
        }
      }

      functions = _.keys(functions);
      if (functions.length === 0) {
        console.error('[BMA] Unparseable unlock condition: ', output);
        return;
      }
      console.debug('[BMA] Unlock conditions successfully parsed:', treeItem);
      return {
        unlockFunctions: functions,
        unlockTree: treeItem
      };
    };

    exports.node.parseEndPoint = function(endpoint) {
      // Try BMA
      var matches = exports.regexp.BMA_ENDPOINT.exec(endpoint);
      if (matches) {
        return {
          "dns": matches[2] || '',
          "ipv4": matches[4] || '',
          "ipv6": matches[6] || '',
          "port": matches[8] || 80,
          "useSsl": matches[8] && matches[8] == 443,
          "path": matches[10],
          "useBma": true
        };
      }
      // Try BMAS
      matches = exports.regexp.BMAS_ENDPOINT.exec(endpoint);
      if (matches) {
        return {
          "dns": matches[2] || '',
          "ipv4": matches[4] || '',
          "ipv6": matches[6] || '',
          "port": matches[8] || 80,
          "useSsl": true,
          "path": matches[10],
          "useBma": true
        };
      }
      // Try BMATOR
      matches = exports.regexp.BMATOR_ENDPOINT.exec(endpoint);
      if (matches) {
        return {
          "dns": matches[1] || '',
          "port": matches[2] || 80,
          "useSsl": false,
          "useTor": true,
          "useBma": true
        };
      }
      // Try WS2P
      matches = exports.regexp.WS2P_ENDPOINT.exec(endpoint);
      if (matches) {
        return {
          "ws2pid": matches[1] || '',
          "dns": matches[3] || '',
          "ipv4": matches[5] || '',
          "ipv6": matches[7] || '',
          "port": matches[9] || 80,
          "useSsl": matches[9] && matches[9] == 443,
          "path": matches[11] || '',
          "useWs2p": true
        };
      }
      // Try WS2PTOR
      matches = exports.regexp.WS2PTOR_ENDPOINT.exec(endpoint);
      if (matches) {
        return {
          "ws2pid": matches[1] || '',
          "dns": matches[2] || '',
          "port": matches[3] || 80,
          "path": matches[4] || '',
          "useSsl": false,
          "useTor": true,
          "useWs2p": true
        };
      }
    };

    exports.copy = function(otherNode) {
      if (that.started) that.stop();
      that.init(otherNode.host, otherNode.port, otherNode.useSsl, that.useCache/*keep original value*/);
      return that.start();
    };

    exports.wot.member.uids = function() {
      return exports.wot.member.all()
        .then(function(res){
          return res.results.reduce(function(res, member){
            res[member.pubkey] = member.uid;
            return res;
          }, {});
        });
    };

    exports.wot.member.get = function(pubkey) {
      return exports.wot.member.uids()
        .then(function(memberUidsByPubkey){
          var uid = memberUidsByPubkey[pubkey];
          return {
              pubkey: pubkey,
              uid: (uid ? uid : null)
            };
        });
    };

    exports.wot.member.getByUid = function(uid) {
      return exports.wot.member.all()
        .then(function(res){
          return _.findWhere(res.results, {uid: uid});
        });
    };

    /**
     * Return all expected blocks
     * @param blockNumbers a rray of block number
    */
    exports.blockchain.blocks = function(blockNumbers){
      return exports.raw.getHttpRecursive(exports.blockchain.block, 'block', blockNumbers);
    };

    /**
     * Return all expected blocks
     * @param blockNumbers a rray of block number
     */
    exports.network.peering.peersByLeaves = function(leaves){
      return exports.raw.getHttpRecursive(exports.network.peering.peers, 'leaf', leaves, 0, 10, callbackFlush);
    };

    exports.raw.getHttpRecursive = function(httpGetRequest, paramName, paramValues, offset, size, callbackFlush) {
      offset = angular.isDefined(offset) ? offset : 0;
      size = size || exports.constants.LIMIT_REQUEST_COUNT;
      return $q(function(resolve, reject) {
        var result = [];
        var jobs = [];
        _.each(paramValues.slice(offset, offset+size), function(paramValue) {
          var requestParams = {};
          requestParams[paramName] = paramValue;
          jobs.push(
            httpGetRequest(requestParams)
              .then(function(res){
                if (!res) return;
                result.push(res);
              })
          );
        });

        $q.all(jobs)
          .then(function() {
            if (offset < paramValues.length - 1) {
              $timeout(function() {
                exports.raw.getHttpRecursive(httpGetRequest, paramName, paramValues, offset+size, size)
                  .then(function(res) {
                    if (!res || !res.length) {
                      resolve(result);
                      return;
                    }

                    resolve(result.concat(res));
                  })
                  .catch(function(err) {
                    reject(err);
                  });
              }, exports.constants.LIMIT_REQUEST_DELAY);
            }
            else {
              resolve(result);
            }
          })
          .catch(function(err){
            if (err && err.ucode === exports.errorCodes.HTTP_LIMITATION) {
              resolve(result);
            }
            else {
              reject(err);
            }
          });
      });
    };

    exports.raw.getHttpWithRetryIfLimitation = function(exec) {
      return exec()
        .catch(function(err){
          // When too many request, retry in 3s
          if (err && err.ucode == exports.errorCodes.HTTP_LIMITATION) {
            return $timeout(function() {
              // retry
              return exports.raw.getHttpWithRetryIfLimitation(exec);
            }, exports.constants.LIMIT_REQUEST_DELAY);
          }
        });
    };

    exports.blockchain.lastUd = function() {
      return exports.blockchain.stats.ud()
        .then(function(res) {
          if (!res.result.blocks || !res.result.blocks.length) {
            return null;
          }
          var lastBlockWithUD = res.result.blocks[res.result.blocks.length - 1];
          return exports.blockchain.block({block: lastBlockWithUD})
            .then(function(block){
              return (block.unitbase > 0) ? block.dividend * Math.pow(10, block.unitbase) : block.dividend;
            });
        });
    };

    exports.uri.parse = function(uri) {
      return $q(function(resolve, reject) {
        // If pubkey: not need to parse
        if (exact(regexp.PUBKEY).test(uri)) {
          resolve({
            pubkey: uri
          });
        }
        else if(uri.startsWith('duniter://')) {
          var parser = csHttp.uri.parse(uri),
            pubkey,
            uid,
            currency = parser.host.indexOf('.') === -1 ? parser.host : null,
            host = parser.host.indexOf('.') !== -1 ? parser.host : null;
          if (parser.username) {
            if (parser.password) {
              uid = parser.username;
              pubkey = parser.password;
            }
            else {
              pubkey = parser.username;
            }
          }
          if (parser.pathname) {
            var paths = parser.pathname.split('/');
            var pathCount = !paths ? 0 : paths.length;
            var index = 0;
            if (!currency && pathCount > index) {
              currency = paths[index++];
            }
            if (!pubkey && pathCount > index) {
              pubkey = paths[index++];
            }
            if (!uid && pathCount > index) {
              uid = paths[index++];
            }
            if (pathCount > index) {
              reject( {message: 'Bad Duniter URI format. Invalid path (incomplete or redundant): '+ parser.pathname}); return;
            }
          }

          if (!currency){
            if (host) {
              csHttp.get(host + '/blockchain/parameters')()
              .then(function(parameters){
                resolve({
                  uid: uid,
                  pubkey: pubkey,
                  host: host,
                  currency: parameters.currency
                });
              })
              .catch(function(err) {
                reject({message: 'Could not get node parameter. Currency could not be retrieve'});
              });
            }
            else {
              reject({message: 'Bad Duniter URI format. Missing currency name (or node address).'}); return;
            }
          }
          else {
            if (!host) {
              resolve({
                uid: uid,
                pubkey: pubkey,
                currency: currency
              });
            }

            // Check if currency are the same (between node and uri)
            return csHttp.get(host + '/blockchain/parameters')()
              .then(function(parameters){
                if (parameters.currency !== currency) {
                  reject( {message: "Node's currency ["+parameters.currency+"] does not matched URI's currency ["+currency+"]."}); return;
                }
                resolve({
                  uid: uid,
                  pubkey: pubkey,
                  host: host,
                  currency: currency
                });
              });
          }
        }
        else {
          throw {message: 'Bad URI format: ' + uri};
        }
      })

      // Check values against regex
      .then(function(result) {
        if (result.pubkey && !(exact(regexp.PUBKEY).test(result.pubkey))) {
          reject({message: "Invalid pubkey format [" + result.pubkey + "]"}); return;
        }
        if (result.uid && !(exact(regexp.USER_ID).test(result.uid))) {
          reject({message: "Invalid uid format [" + result.uid + "]"}); return;
        }
        if (result.currency && !(exact(regexp.CURRENCY).test(result.currency))) {
          reject({message: "Invalid currency format ["+result.currency+"]"}); return;
        }
        return result;
      });
    };

    // Define get latest release (or fake function is no URL defined)
    var duniterLatestReleaseUrl = csSettings.data.duniterLatestReleaseUrl && csHttp.uri.parse(csSettings.data.duniterLatestReleaseUrl);
    exports.raw.getLatestRelease = duniterLatestReleaseUrl ?
      csHttp.getWithCache(duniterLatestReleaseUrl.host,
        duniterLatestReleaseUrl.port,
        "/" + duniterLatestReleaseUrl.pathname,
        /*useSsl*/ (duniterLatestReleaseUrl.port == 443 || duniterLatestReleaseUrl.protocol == 'https:' || that.forceUseSsl),
        csHttp.cache.LONG
      ) :
      // No URL define: use a fake function
      function() {
        return $q.when();
      };

    exports.version.latest = function() {
      return exports.raw.getLatestRelease()
        .then(function (json) {
          if (!json) return;
          if (json.name && json.html_url) {
            return {
              version: json.name,
              url: json.html_url
            };
          }
          if (json.tag_name && json.html_url) {
            return {
              version: json.tag_name.substring(1),
              url: json.html_url
            };
          }
        })
        .catch(function(err) {
          // silent (just log it)
          console.error('[BMA] Failed to get Duniter latest version', err);
        });
    };

    exports.websocket = {
        block: ws('/ws/block'),
        peer: ws('/ws/peer'),
        close : closeWs
      };

    angular.merge(that, exports);
  }

  var service = new BMA(undefined, undefined, undefined, true);

  service.instance = function(host, port, useSsl, useCache) {
    var bma = new BMA();
    bma.init(host, port, useSsl, useCache);
    return bma;
  };

  service.lightInstance = function(host, port, useSsl, timeout) {
    port = port || 80;
    useSsl = angular.isDefined(useSsl) ? useSsl : (port == 443);
    return {
      host: host,
      port: port,
      useSsl: useSsl,
      url: csHttp.getUrl(host, port, ''/*path*/, useSsl),
      node: {
        summary: csHttp.getWithCache(host, port, '/node/summary', useSsl, csHttp.cache.LONG, false, timeout)
      },
      network: {
        peering: {
          self: csHttp.get(host, port, '/network/peering', useSsl, timeout)
        },
        peers: csHttp.get(host, port, '/network/peers', useSsl, timeout)
      },
      blockchain: {
        current: csHttp.get(host, port, '/blockchain/current', useSsl, timeout),
        stats: {
          hardship: csHttp.get(host, port, '/blockchain/hardship/:pubkey', useSsl, timeout)
        }
      }
    };
  };

  // default action
  //service.start();

  return service;
}])

;


angular.module('cesium.wot.services', ['ngApi', 'cesium.bma.services', 'cesium.crypto.services', 'cesium.utils.services',
  'cesium.settings.services'])

.factory('csWot', ['$q', '$timeout', 'BMA', 'Api', 'CacheFactory', 'csConfig', 'csCurrency', 'csSettings', 'csCache', function($q, $timeout, BMA, Api, CacheFactory, csConfig, csCurrency, csSettings, csCache) {
  'ngInject';

  function factory(id) {

    var
      api = new Api(this, "csWot-" + id),
      identityCache = csCache.get('csWot-idty-', csCache.constants.SHORT),

      // Add id, and remove duplicated id
      _addUniqueIds = function(idties) {
        var idtyKeys = {};
        return idties.reduce(function(res, idty) {
          idty.id = idty.id || idty.uid + '-' + idty.pubkey;
          if (!idtyKeys[idty.id]) {
            idtyKeys[idty.id] = true;
            return res.concat(idty);
          }
          return res;
        }, []);
      },

      _sortAndSliceIdentities = function(idties, offset, size) {
        offset = offset || 0;

        // Add unique ids
        idties = _addUniqueIds(idties);

        // Sort by block and
        idties = _.sortBy(idties, function(idty){
          var score = 1;
          score += (1000000 * (idty.block));
          score += (10      * (900 - idty.uid.toLowerCase().charCodeAt(0)));
          return -score;
        });
        if (angular.isDefined(size) && idties.length > size) {
          idties = idties.slice(offset, offset+size); // limit if more than expected size
        }


        return idties;
      },

      _sortCertifications = function(certifications) {
        certifications = _.sortBy(certifications, function(cert){
          var score = 1;
          score += (1000000000000 * (cert.expiresIn ? cert.expiresIn : 0));
          score += (10000000      * (cert.isMember ? 1 : 0));
          score += (10            * (cert.block ? cert.block : 0));
          return -score;
        });
        return certifications;
      },

      _resetRequirements = function(data) {
        data.requirements = {
          needSelf: true,
          needMembership: true,
          canMembershipOut: false,
          needRenew: false,
          pendingMembership: false,
          wasMember: false,
          certificationCount: 0,
          needCertifications: false,
          needCertificationCount: 0,
          willNeedCertificationCount: 0,
          alternatives: undefined
        };
        data.blockUid = null;
        data.isMember = false;
        data.sigDate = null;
      },

      _fillRequirements = function(requirements, currencyParameters) {
        // Add useful custom fields
        requirements.hasSelf = true;
        requirements.needSelf = false;
        requirements.wasMember = angular.isDefined(requirements.wasMember) ? requirements.wasMember : false; // Compat with Duniter 0.9
        requirements.needMembership = (requirements.membershipExpiresIn <= 0 && requirements.membershipPendingExpiresIn <= 0 && !requirements.wasMember);
        requirements.needRenew = (!requirements.needMembership &&
          requirements.membershipExpiresIn <= csSettings.data.timeWarningExpireMembership &&
          requirements.membershipPendingExpiresIn <= 0) ||
          (requirements.wasMember && requirements.membershipExpiresIn === 0 &&
          requirements.membershipPendingExpiresIn === 0);
        requirements.canMembershipOut = (requirements.membershipExpiresIn > 0);
        requirements.pendingMembership = (requirements.membershipExpiresIn <= 0 && requirements.membershipPendingExpiresIn > 0);
        requirements.isMember = (requirements.membershipExpiresIn > 0);
        requirements.blockUid = requirements.meta.timestamp;
        // Force certification count to 0, is not a member yet - fix #269
        requirements.certificationCount = (requirements.isMember && requirements.certifications) ? requirements.certifications.length : 0;
        requirements.willExpireCertificationCount = requirements.certifications ? requirements.certifications.reduce(function(count, cert){
          return count + (cert.expiresIn <= csSettings.data.timeWarningExpire ? 1 : 0);
        }, 0) : 0;
        requirements.willExpire = requirements.willExpireCertificationCount > 0;
        requirements.pendingRevocation = !requirements.revoked && !!requirements.revocation_sig;

        // Fix pending certifications count - Fix #624
        if (!requirements.isMember && !requirements.wasMember) {
          var certifiers = _.union(
            _.pluck(requirements.pendingCerts || [], 'from'),
            _.pluck(requirements.certifications || [], 'from')
          );
          requirements.pendingCertificationCount = _.size(certifiers);
        }
        else {
          requirements.pendingCertificationCount = angular.isDefined(requirements.pendingCerts) ? requirements.pendingCerts.length : 0 ;
        }

        // Compute
        requirements.needCertificationCount = (!requirements.needSelf && (requirements.certificationCount < currencyParameters.sigQty)) ?
          (currencyParameters.sigQty - requirements.certificationCount) : 0;
        requirements.willNeedCertificationCount = (!requirements.needMembership && !requirements.needCertificationCount &&
        (requirements.certificationCount - requirements.willExpireCertificationCount) < currencyParameters.sigQty) ?
          (currencyParameters.sigQty - requirements.certificationCount + requirements.willExpireCertificationCount) : 0;


        return requirements;
      },

      loadRequirements = function(data) {
        if (!data || (!data.pubkey && !data.uid)) return $q.when(data);

        return $q.all([
          // Get currency
          csCurrency.get(),
          // Get requirements
          BMA.wot.requirements({pubkey: data.pubkey||data.uid})
        ])
          .then(function(res){
            var currency = res[0];

            res = res[1];

            if (!res.identities || !res.identities.length)  return;

            // Sort to select the best identity
            if (res.identities.length > 1) {
              // Select the best identity, by sorting using this order
              //  - same wallet uid
              //  - is member
              //  - has a pending membership
              //  - is not expired (in sandbox)
              //  - is not outdistanced
              //  - if has certifications
              //      max(count(certification)
              //    else
              //      max(membershipPendingExpiresIn) = must recent membership
              res.identities = _.sortBy(res.identities, function(idty) {
                var score = 0;
                score += (10000000000 * ((data.uid && idty.uid === data.uid) ? 1 : 0));
                score += (10000000000 * ((data.blockUid && idty.meta && idty.meta.timestamp === data.blockUid) ? 1 : 0));
                score += (1000000000  * (idty.membershipExpiresIn > 0 ? 1 : 0));
                score += (100000000   * (idty.membershipPendingExpiresIn > 0 ? 1 : 0));
                score += (10000000    * (!idty.expired ? 1 : 0));
                score += (1000000     * (!idty.outdistanced ? 1 : 0));
                score += (100000      * (idty.wasMember ? 1 : 0));
                var certCount = !idty.expired && idty.certifications ? idty.certifications.length : 0;
                score += (1         * (certCount ? certCount : 0));
                score += (1         * (!certCount && idty.membershipPendingExpiresIn > 0 ? idty.membershipPendingExpiresIn/1000 : 0));
                return -score;
              });
              console.debug('[wot] Found {0} identities. Will selected the best one'.format(res.identities.length));
            }

            // Select the first identity
            var requirements = _fillRequirements(res.identities[0], currency.parameters);

            data.requirements = requirements;
            data.pubkey = requirements.pubkey;
            data.uid = requirements.uid;
            data.isMember =  requirements.isMember;
            data.blockUid =  requirements.blockUid;

            // Prepare alternatives identities if any
            if (!requirements.isMember && !requirements.wasMember && res.identities.length > 1) {
              requirements.alternatives = res.identities.splice(1);
              _.forEach(requirements.alternatives, function(requirements) {
                _fillRequirements(requirements, currency.parameters);
              });
            }

            // TODO : get sigDate from blockUid ??

            return data;
          })
          .catch(function(err) {
            _resetRequirements(data);
            // If not a member: continue
            if (!!err &&
                (err.ucode == BMA.errorCodes.NO_MATCHING_MEMBER ||
                 err.ucode == BMA.errorCodes.NO_IDTY_MATCHING_PUB_OR_UID)) {
              return data;
            }
            throw err;
          })
          ;
      },

      loadIdentityByLookup = function(pubkey, uid) {
        return BMA.wot.lookup({ search: pubkey||uid })
          .then(function(res){
            var identities = res.results.reduce(function(idties, res) {
              return idties.concat(res.uids.reduce(function(uids, idty) {
                var blockUid = idty.meta.timestamp.split('-', 2);
                return uids.concat({
                  uid: idty.uid,
                  pubkey: res.pubkey,
                  timestamp: idty.meta.timestamp,
                  number: parseInt(blockUid[0]),
                  hash: blockUid[1],
                  revoked: idty.revoked,
                  revocationNumber: idty.revoked_on,
                  sig: idty.self
                });
              }, []));
            }, []);

            // Sort identities if need
            if (identities.length) {
              // Select the best identity, by sorting using this order
              //  - same given uid
              //  - not revoked
              //  - max(block_number)
              identities = _.sortBy(identities, function(idty) {
                var score = 0;
                score += (10000000000 * ((uid && idty.uid === uid) ? 1 : 0));
                score += (1000000000  * (!idty.revoked ? 1 : 0));
                score += (1           * (idty.number ? idty.number : 0));
                return -score;
              });
            }
            var identity = identities[0];

            identity.hasSelf = !!(identity.uid && identity.timestamp && identity.sig);
            identity.lookup = {};

            // Store received certifications
            var certPubkeys = [];
            identity.lookup.certifications = !res.results ? {} : res.results.reduce(function(certsMap, res) {
              return res.uids.reduce(function(certsMap, idty) {
                var idtyFullKey = idty.uid + '-' + (idty.meta ? idty.meta.timestamp : '');
                certsMap[idtyFullKey] = idty.others.reduce(function(certs, cert) {
                  var certFullKey = idtyFullKey + '-' + cert.pubkey;
                  var result = {
                    pubkey: cert.pubkey,
                    uid: cert.uids[0],
                    cert_time:  {
                      block: (cert.meta && cert.meta.block_number)  ? cert.meta.block_number : 0,
                      block_hash: (cert.meta && cert.meta.block_hash)  ? cert.meta.block_hash : null
                    },
                    isMember: cert.isMember,
                    wasMember: cert.wasMember,
                  };
                  if (!certPubkeys[certFullKey]) {
                    certPubkeys[certFullKey] = result;
                  }
                  else { // if duplicated cert: keep the most recent
                    if (result.cert_time.block > certPubkeys[certFullKey].cert_time.block) {
                      certPubkeys[certFullKey] = result;
                      certs.splice(_.findIndex(certs, {pubkey: cert.pubkey}), 1, result);
                      return certs;
                    }
                    else {
                      return certs; // skip this cert
                    }
                  }
                  return certs.concat(result);
                }, []);
                return certsMap;
              }, certsMap);
            }, {});

            // Store given certifications
            certPubkeys = [];
            identity.lookup.givenCertifications = !res.results ? [] : res.results.reduce(function(certs, res) {
              return res.signed.reduce(function(certs, cert) {
                var result = {
                  pubkey: cert.pubkey,
                  uid: cert.uid,
                  cert_time:  {
                    block: (cert.cert_time && cert.cert_time.block)  ? cert.cert_time.block : 0,
                    block_hash: (cert.cert_time && cert.cert_time.block_hash)  ? cert.cert_time.block_hash : null
                  },
                  sigDate: cert.meta ? cert.meta.timestamp : null,
                  isMember: cert.isMember,
                  wasMember: cert.wasMember
                };
                if (!certPubkeys[cert.pubkey]) {
                  certPubkeys[cert.pubkey] = result;
                  // TODO : to not add, but replace the old one
                }
                else { // if duplicated cert: keep the most recent
                  if (result.block > certPubkeys[cert.pubkey].block) {
                    certPubkeys[cert.pubkey] = result;
                  }
                  else {
                    return certs; // skip this result
                  }
                }
                return certs.concat(result);
              }, certs);
            }, []);

            // Retrieve time (self and revocation)
            var blocks = [identity.number];
            if (identity.revocationNumber) {
              blocks.push(identity.revocationNumber);
            }
            return BMA.blockchain.blocks(blocks)
              .then(function(blocks){
                identity.sigDate = blocks[0].medianTime;

                // Check if self has been done on a valid block
                if (identity.number !== 0 && identity.hash !== blocks[0].hash) {
                  identity.hasBadSelfBlock = true;
                }

                // Set revocation time
                if (identity.revocationNumber) {
                  identity.revocationTime = blocks[1].medianTime;
                }

                return identity;
              })
              .catch(function(err){
                // Special case for currency init (root block not exists): use now
                if (err && err.ucode == BMA.errorCodes.BLOCK_NOT_FOUND && identity.number === 0) {
                  identity.sigDate = Math.trunc(new Date().getTime() / 1000);
                  return identity;
                }
                else {
                  throw err;
                }
              });
          })
          .catch(function(err) {
            if (!!err && err.ucode == BMA.errorCodes.NO_MATCHING_IDENTITY) { // Identity not found (if no self)
              var identity = {
                uid: null,
                pubkey: pubkey,
                hasSelf: false
              };
              return identity;
            }
            else {
              throw err;
            }
          });
      },

      loadCertifications = function(getFunction, pubkey, lookupCertifications, parameters, medianTime, certifiersOf) {

        function _certId(pubkey, block) {
          return pubkey + '-' + block;
        }

        // TODO : remove this later (when all node will use duniter v0.50+)
        var lookupHasCertTime = true; // Will be set ti FALSE before Duniter v0.50
        var lookupCerticationsByCertId = lookupCertifications ? lookupCertifications.reduce(function(res, cert){
          var certId = _certId(cert.pubkey, cert.cert_time ? cert.cert_time.block : cert.sigDate);
          if (!cert.cert_time) lookupHasCertTime = false;
          res[certId] = cert;
          return res;
        }, {}) : {};

        var isMember = true;

        return getFunction({ pubkey: pubkey })
          .then(function(res) {
            return res.certifications.reduce(function (res, cert) {
              // Rappel :
              //   cert.sigDate = blockstamp de l'identité
              //   cert.cert_time.block : block au moment de la certification
              //   cert.written.number : block où la certification est écrite

              var pending = !cert.written;
              var certTime = cert.cert_time ? cert.cert_time.medianTime : null;
              var expiresIn = (!certTime) ? 0 : (pending ?
                (certTime + parameters.sigWindow - medianTime) :
                (certTime + parameters.sigValidity - medianTime));
              expiresIn = (expiresIn < 0) ? 0 : expiresIn;
              // Remove from lookup certs
              var certId = _certId(cert.pubkey, lookupHasCertTime && cert.cert_time ? cert.cert_time.block : cert.sigDate);
              delete lookupCerticationsByCertId[certId];

              // Add to result list
              return res.concat({
                pubkey: cert.pubkey,
                uid: cert.uid,
                time: certTime,
                isMember: cert.isMember,
                wasMember: cert.wasMember,
                expiresIn: expiresIn,
                willExpire: (expiresIn && expiresIn <= csSettings.data.timeWarningExpire),
                pending: pending,
                block: (cert.written !== null) ? cert.written.number :
                  (cert.cert_time ? cert.cert_time.block : null),
                valid: (expiresIn > 0)
              });
            }, []);
          })
          .catch(function(err) {
            if (!!err && err.ucode == BMA.errorCodes.NO_MATCHING_MEMBER) { // member not found
              isMember = false;
              return []; // continue (append pendings cert if exists in lookup)
            }
            else {
              throw err;
            }
          })

          // Add pending certs (found in lookup - see loadIdentityByLookup())
          .then(function(certifications) {
            var pendingCertifications = _.values(lookupCerticationsByCertId);
            if (!pendingCertifications.length) return certifications; // No more pending continue

            // Special case for initPhase - issue #
            if (csCurrency.data.initPhase) {
              return pendingCertifications.reduce(function(res, cert) {
                return res.concat({
                  pubkey: cert.pubkey,
                  uid: cert.uid,
                  isMember: cert.isMember,
                  wasMember: cert.wasMember,
                  time: null,
                  expiresIn: parameters.sigWindow,
                  willExpire: false,
                  pending: true,
                  block: 0,
                  valid: true
                });
              }, certifications);
            }

            var pendingCertByBlocks = pendingCertifications.reduce(function(res, cert){
              var block = lookupHasCertTime && cert.cert_time ? cert.cert_time.block :
                (cert.sigDate ? cert.sigDate.split('-')[0] : null);
              if (angular.isDefined(block)) {
                if (!res[block]) {
                  res[block] = [cert];
                }
                else {
                  res[block].push(cert);
                }
              }
              return res;
            }, {});

            // Set time to pending cert, from blocks
            return BMA.blockchain.blocks(_.keys(pendingCertByBlocks)).then(function(blocks){
              certifications = blocks.reduce(function(res, block){
                return res.concat(pendingCertByBlocks[block.number].reduce(function(res, cert) {
                  var certTime = block.medianTime;
                  var expiresIn = Math.max(0, certTime + parameters.sigWindow - medianTime);
                  var validBuid = (!cert.cert_time || !cert.cert_time.block_hash || cert.cert_time.block_hash == block.hash);
                  if (!validBuid) {
                    console.debug("[wot] Invalid cert {0}: block hash changed".format(cert.pubkey.substring(0,8)));
                  }
                  var valid = (expiresIn > 0) && (!certifiersOf || cert.isMember) && validBuid;
                  return res.concat({
                    pubkey: cert.pubkey,
                    uid: cert.uid,
                    isMember: cert.isMember,
                    wasMember: cert.wasMember,
                    time: certTime,
                    expiresIn: expiresIn,
                    willExpire: (expiresIn && expiresIn <= csSettings.data.timeWarningExpire),
                    pending: true,
                    block: lookupHasCertTime && cert.cert_time ? cert.cert_time.block :
                    (cert.sigDate ? cert.sigDate.split('-')[0] : null),
                    valid: valid
                  });
                }, []));
              }, certifications);
              return certifications;
            });
          })

          // Sort and return result
          .then(function(certifications) {

            // Remove pending cert duplicated with a written & valid cert
            var writtenCertByPubkey = certifications.reduce(function(res, cert) {
              if (!cert.pending && cert.valid && cert.expiresIn >= parameters.sigWindow) {
                res[cert.pubkey] = true;
              }
              return res;
            }, {});

            // Final sort
            certifications = _sortCertifications(certifications);

            // Split into valid/pending/error
            var pendingCertifications = [];
            var errorCertifications = [];
            var validCertifications = certifications.reduce(function(res, cert) {
              if (cert.pending) {
                if (cert.valid && !writtenCertByPubkey[cert.pubkey]) {
                  pendingCertifications.push(cert);
                }
                else if (!cert.valid && !writtenCertByPubkey[cert.pubkey]){
                  errorCertifications.push(cert);
                }
                return res;
              }
              return res.concat(cert);
            }, []);

            return {
              valid: validCertifications,
              pending: pendingCertifications,
              error: errorCertifications
            };
          })
          ;
      },

      // Add events on given account
      addEvents = function(data) {

        if (data.requirements.revoked) {
          delete data.hasBadSelfBlock;
          addEvent(data, {type: 'error', message: 'ERROR.IDENTITY_REVOKED', messageParams: {revocationTime: data.revocationTime}});
          console.debug("[wot] Identity [{0}] has been revoked".format(data.uid));
        }
        else if (data.requirements.pendingRevocation) {
          addEvent(data, {type:'error', message: 'ERROR.IDENTITY_PENDING_REVOCATION'});
          console.debug("[wot] Identity [{0}] has pending revocation".format(data.uid));
        }
        else if (data.hasBadSelfBlock) {
          delete data.hasBadSelfBlock;
          if (!data.isMember) {
            addEvent(data, {type: 'error', message: 'ERROR.IDENTITY_INVALID_BLOCK_HASH'});
            console.debug("[wot] Invalid membership for {0}: block hash changed".format(data.uid));
          }
        }
        else if (data.requirements.expired) {
          addEvent(data, {type: 'error', message: 'ERROR.IDENTITY_EXPIRED'});
          console.debug("[wot] Identity {0} expired (in sandbox)".format(data.uid));
        }
        else if (data.requirements.willNeedCertificationCount > 0) {
          addEvent(data, {type: 'error', message: 'INFO.IDENTITY_WILL_MISSING_CERTIFICATIONS', messageParams: data.requirements});
          console.debug("[wot] Identity {0} will need {1} certification(s)".format(data.uid, data.requirements.willNeedCertificationCount));
        }
        else if (!data.requirements.needSelf && data.requirements.needMembership) {
          addEvent(data, {type: 'error', message: 'INFO.IDENTITY_NEED_MEMBERSHIP'});
          console.debug("[wot] Identity {0} has a self but no membership".format(data.uid));
        }
      },

      loadData = function(pubkey, withCache, uid, force) {

        var data;

        if (!pubkey && uid && !force) {
          return BMA.wot.member.getByUid(uid)
            .then(function(member) {
              if (member) return loadData(member.pubkey, withCache, member.uid); // recursive call
              //throw {message: 'NOT_A_MEMBER'};
              return loadData(pubkey, withCache, uid, true/*force*/);
            });
        }

        // Check cached data
        if (pubkey) {
          data = withCache ? identityCache.get(pubkey) : null;
          if (data && (!uid || data.uid == uid)) {
            console.debug("[wot] Identity " + pubkey.substring(0, 8) + " found in cache");
            return $q.when(data);
          }
          console.debug("[wot] Loading identity " + pubkey.substring(0, 8) + "...");
          data = {
            pubkey: pubkey,
            uid: uid
          };
        }
        else {
          console.debug("[wot] Loading identity from uid " + uid);
          data = {
            uid: uid
          };
        }

        var now = new Date().getTime();

        var parameters;
        var medianTime;

        return $q.all([
            // Get parameters
            BMA.blockchain.parameters()
              .then(function(res) {
                parameters = res;

              }),
            // Get current time
            BMA.blockchain.current()
              .then(function(current) {
                medianTime = current.medianTime;
              })
              .catch(function(err){
                // Special case for currency init (root block not exists): use now
                if (err && err.ucode == BMA.errorCodes.NO_CURRENT_BLOCK) {
                  medianTime = Math.trunc(new Date().getTime()/1000);
                }
                else {
                  throw err;
                }
              }),

            // Get requirements
            loadRequirements(data),

            // Get identity using lookup
            loadIdentityByLookup(pubkey, uid)
              .then(function (identity) {
                  angular.merge(data, identity);
              })
          ])
          .then(function() {
            if (!data.requirements.uid) return;

            var idtyFullKey = data.requirements.uid + '-' + data.requirements.meta.timestamp;

            return $q.all([
              // Get received certifications
              loadCertifications(BMA.wot.certifiersOf, data.pubkey, data.lookup ? data.lookup.certifications[idtyFullKey] : null, parameters, medianTime, true /*certifiersOf*/)
                .then(function (res) {
                  data.received_cert = res.valid;
                  data.received_cert_pending = res.pending;
                  data.received_cert_error = res.error;
                }),

              // Get given certifications
              loadCertifications(BMA.wot.certifiedBy, data.pubkey, data.lookup ? data.lookup.givenCertifications : null, parameters, medianTime, false/*certifiersOf*/)
                .then(function (res) {
                  data.given_cert = res.valid;
                  data.given_cert_pending = res.pending;
                  data.given_cert_error = res.error;
                })
            ]);
          })
          .then(function() {

            // Add compute some additional requirements (that required all data like certifications)
            data.requirements.pendingCertificationCount = data.received_cert_pending ? data.received_cert_pending.length : data.requirements.pendingCertificationCount;
            // Use /wot/lookup.revoked when requirements not filled
            data.requirements.revoked = angular.isDefined(data.requirements.revoked) ? data.requirements.revoked : data.revoked;

            // Add account events
            addEvents(data);

            // API extension
            return api.data.raisePromise.load(data)
              .catch(function(err) {
                console.debug('Error while loading identity data, on extension point.');
                console.error(err);
              });
          })
          .then(function() {
            if (!data.pubkey) return undefined; // not found
            delete data.lookup; // not need anymore
            identityCache.put(data.pubkey, data); // add to cache
            console.debug('[wot] Identity '+ data.pubkey.substring(0, 8) +' loaded in '+ (new Date().getTime()-now) +'ms');
            return data;
          });
      },

      search = function(text, options) {
        if (!text || text.trim() !== text) {
          return $q.when(undefined);
        }

        // Remove first special characters (to avoid request error)
        var safeText = text.replace(/(^|\s)#\w+/g, ''); // remove tags
        safeText = safeText.replace(/[^a-zA-Z0-9_-\s]+/g, '');
        safeText = safeText.replace(/\s+/g, ' ').trim();

        options = options || {};
        options.addUniqueId = angular.isDefined(options.addUniqueId) ? options.addUniqueId : true;
        options.allowExtension = angular.isDefined(options.allowExtension) ? options.allowExtension : true;
        options.excludeRevoked = angular.isDefined(options.excludeRevoked) ? options.excludeRevoked : false;

        var promise;
        if (!safeText) {
          promise = $q.when([]);
        }
        else {
          promise = $q.all(
            safeText.split(' ').reduce(function(res, text) {
              console.debug('[wot] Will search on: \'' + text + '\'');
              return res.concat(BMA.wot.lookup({ search: text }));
            }, [])
          ).then(function(res){
              return res.reduce(function(idties, res) {
                return idties.concat(res.results.reduce(function(idties, res) {
                  return idties.concat(res.uids.reduce(function(uids, idty) {
                    var blocUid = idty.meta.timestamp.split('-', 2);
                    var revoked = !idty.revoked && idty.revocation_sig;
                    if (!options.excludeRevoked || !revoked) {
                      return uids.concat({
                        uid: idty.uid,
                        pubkey: res.pubkey,
                        number: blocUid[0],
                        hash: blocUid[1],
                        revoked: revoked
                      });
                    }
                    return uids;
                  }, []));
                }, []));
              }, []);
            })
            .catch(function(err) {
              if (err && err.ucode == BMA.errorCodes.NO_MATCHING_IDENTITY) {
                return [];
              }
              else {
                throw err;
              }
            });
        }

        return promise
          .then(function(idties) {
            if (!options.allowExtension) {
              // Add unique id (if enable)
              return options.addUniqueId ? _addUniqueIds(idties) : idties;
            }
            var lookupResultCount = idties.length;
            // call extension point
            return api.data.raisePromise.search(text, idties, 'pubkey')
              .then(function() {

                // Make sure to add uid to new results - fix #488
                if (idties.length > lookupResultCount) {
                  var idtiesWithoutUid = _.filter(idties, function(idty) {
                    return !idty.uid && idty.pubkey;
                  });
                  if (idtiesWithoutUid.length) {
                    return BMA.wot.member.uids()
                      .then(function(uids) {
                        _.forEach(idties, function(idty) {
                          if (!idty.uid && idty.pubkey) {
                            idty.uid = uids[idty.pubkey];
                          }
                        });
                      });
                  }
                }
              })
              .then(function() {
                // Add unique id (if enable)
                return options.addUniqueId ? _addUniqueIds(idties) : idties;
              });
          });
      },

      getNewcomers = function(offset, size) {
        offset = offset || 0;
        size = size || 20;
        var total;
        return $q.all([
            csCurrency.blockchain.current(true)
              .then(function(block) {
                total = block.membersCount;
              }),
            BMA.blockchain.stats.newcomers()
          ])
          .then(function(res) {
            res = res[1];
            if (!res.result.blocks || !res.result.blocks.length) {
              return null;
            }
            var blocks = _.sortBy(res.result.blocks, function (n) {
              return -n;
            });
            return getNewcomersRecursive(blocks, 0, 5, offset+size);
          })
          .then(function(idties){
            if (!idties || !idties.length) {
              return null;
            }
            idties = _sortAndSliceIdentities(idties, offset, size);

            // Extension point
            return extendAll(idties, 'pubkey', true/*skipAddUid*/);
          })
            .then(function(idties) {
              return {
                hits: idties,
                total: total
              };
            })
          ;
      },


      getNewcomersRecursive = function(blocks, offset, size, maxResultSize) {
        return $q(function(resolve, reject) {
          var result = [];
          var jobs = [];
          _.each(blocks.slice(offset, offset+size), function(number) {
            jobs.push(
              BMA.blockchain.block({block: number})
                .then(function(block){
                  if (!block || !block.joiners) return;
                  _.each(block.joiners, function(joiner){
                    var parts = joiner.split(':');
                    var idtyKey = parts[parts.length-1]/*uid*/ + '-' + parts[0]/*pubkey*/;
                    result.push({
                      id: idtyKey,
                      uid: parts[parts.length-1],
                      pubkey:parts[0],
                      memberDate: block.medianTime,
                      block: block.number
                    });
                  });
                })
            );
          });

          $q.all(jobs)
            .then(function() {
              if (result.length < maxResultSize && offset < blocks.length - 1) {
                $timeout(function() {
                  getNewcomersRecursive(blocks, offset+size, size, maxResultSize - result.length)
                    .then(function(res) {
                      resolve(result.concat(res));
                    })
                    .catch(function(err) {
                      reject(err);
                    });
                }, 1000);
              }
              else {
                resolve(result);
              }
            })
            .catch(function(err){
              if (err && err.ucode === BMA.errorCodes.HTTP_LIMITATION) {
                resolve(result);
              }
              else {
                reject(err);
              }
            });
        });
      },

      getPending = function(offset, size) {
        offset = offset || 0;
        size = size || 20;
        var now = new Date().getTime();
        return $q.all([
          BMA.wot.member.uids(),
          BMA.wot.member.pending()
            .then(function(res) {
              return (res.memberships && res.memberships.length) ? res.memberships : undefined;
            })
          ])
          .then(function(res) {
            var uids = res[0];
            var memberships = res[1];
            if (!memberships) return;

            var idtiesByBlock = {};
            var idtiesByPubkey = {};
            _.forEach(memberships, function(ms){
              if (ms.membership == 'IN' && !uids[ms.pubkey]) {
                var idty = {
                  uid: ms.uid,
                  pubkey: ms.pubkey,
                  block: ms.blockNumber,
                  blockHash: ms.blockHash
                };
                var otherIdtySamePubkey = idtiesByPubkey[ms.pubkey];
                if (otherIdtySamePubkey && idty.block > otherIdtySamePubkey.block) {
                  return; // skip
                }
                idtiesByPubkey[idty.pubkey] = idty;
                if (!idtiesByBlock[idty.block]) {
                  idtiesByBlock[idty.block] = [idty];
                }
                else {
                  idtiesByBlock[idty.block].push(idty);
                }

                // Remove previous idty from map
                if (otherIdtySamePubkey) {
                  idtiesByBlock[otherIdtySamePubkey.block] = idtiesByBlock[otherIdtySamePubkey.block].reduce(function(res, aidty){
                    if (aidty.pubkey == otherIdtySamePubkey.pubkey) return res; // if match idty to remove, to NOT add
                    return (res||[]).concat(aidty);
                  }, null);
                  if (idtiesByBlock[otherIdtySamePubkey.block] === null) {
                    delete idtiesByBlock[otherIdtySamePubkey.block];
                  }
                }
              }
            });

            var idties = _.values(idtiesByPubkey);
            var total = idties.length; // get total BEFORE slice

            idties = _sortAndSliceIdentities(idties, offset, size);
            var blocks = idties.reduce(function(res, aidty) {
              return res.concat(aidty.block);
            }, []);

            return  $q.all([
              // Get time from blocks
              BMA.blockchain.blocks(_.uniq(blocks))
              .then(function(blocks) {

                _.forEach(blocks, function(block){
                  _.forEach(idtiesByBlock[block.number], function(idty) {
                    idty.sigDate = block.medianTime;
                    if (block.number !== 0 && idty.blockHash !== block.hash) {
                      addEvent(idty, {type:'error', message: 'ERROR.WOT_PENDING_INVALID_BLOCK_HASH'});
                      console.debug("Invalid membership for uid={0}: block hash changed".format(idty.uid));
                    }
                  });
                });
              }),

              // Extension point
              extendAll(idties, 'pubkey', true/*skipAddUid*/)
            ])
            .then(function() {
              console.debug("[ES] [wot] Loaded {0}/{1} pending identities in {2} ms".format(idties && idties.length || 0, total, new Date().getTime() - now));
              return {
                hits: idties,
                total: total
              };
            });
          });
      },

      getAll = function() {
        var letters = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','u','v','w','x','y','z'];
        return getAllRecursive(letters, 0, BMA.constants.LIMIT_REQUEST_COUNT)
          .then(function(idties) {
            return extendAll(idties, 'pubkey', true/*skipAddUid*/);
          })
          .then(_addUniqueIds)
          .then(function() {
            return {
              hits: idties,
              total: idties.length
            };
          });
      },

      getAllRecursive = function(letters, offset, size) {
        return $q(function(resolve, reject) {
          var result = [];
          var pubkeys = {};
          var jobs = [];
          _.each(letters.slice(offset, offset+size), function(letter) {
            jobs.push(
              search(letter, {
                addUniqueId: false, // will be done in parent method
                allowExtension: false // extension point will be called in parent method
              })
            .then(function(idties){
                if (!idties || !idties.length) return;
                result = idties.reduce(function(res, idty) {
                  if (!pubkeys[idty.pubkey]) {
                    pubkeys[idty.pubkey] = true;
                    return res.concat(idty);
                  }
                  return res;
                }, result);
              })
            );
          });

          $q.all(jobs)
            .then(function() {
              if (offset < letters.length - 1) {
                $timeout(function() {
                  getAllRecursive(letters, offset+size, size)
                    .then(function(idties) {
                      if (!idties || !idties.length) {
                        resolve(result);
                        return;
                      }
                      resolve(idties.reduce(function(res, idty) {
                        if (!pubkeys[idty.pubkey]) {
                          pubkeys[idty.pubkey] = true;
                          return res.concat(idty);
                        }
                        return res;
                      }, result));
                    })
                    .catch(function(err) {
                      reject(err);
                    });
                }, BMA.constants.LIMIT_REQUEST_DELAY);
              }
              else {
                resolve(result);
              }
            })
            .catch(function(err){
              if (err && err.ucode === BMA.errorCodes.HTTP_LIMITATION) {
                resolve(result);
              }
              else {
                reject(err);
              }
            });
        });
      },

      extend = function(idty, pubkeyAttributeName, skipAddUid) {
        return extendAll([idty], pubkeyAttributeName, skipAddUid)
          .then(function(res) {
            return res[0];
          });
      },

      extendAll = function(idties, pubkeyAttributeName, skipAddUid) {

        pubkeyAttributeName = pubkeyAttributeName || 'pubkey';

        var jobs = [];
        if (!skipAddUid) jobs.push(BMA.wot.member.uids());

        jobs.push(api.data.raisePromise.search(null, idties, pubkeyAttributeName)
          .catch(function(err) {
            console.debug('Error while search identities, on extension point.');
            console.error(err);
          }));

        return $q.all(jobs)
        .then(function(res) {
          if (!skipAddUid) {
            var uidsByPubkey = res[0];
            // Set uid (on every data)
            _.forEach(idties, function(data) {
              if (!data.uid && data[pubkeyAttributeName]) {
                data.uid = uidsByPubkey[data[pubkeyAttributeName]];
                // Remove name if redundant with uid
                if (data.uid && data.uid == data.name) {
                  delete data.name;
                }
              }
            });
          }

          return idties;
        });
      },

      addEvent = function(data, event) {
        event = event || {};
        event.type = event.type || 'info';
        event.message = event.message || '';
        event.messageParams = event.messageParams || {};
        data.events = data.events || [];
        data.events.push(event);
      }
    ;

    // Register extension points
    api.registerEvent('data', 'load');
    api.registerEvent('data', 'search');

    return {
      id: id,
      load: loadData,
      loadRequirements: loadRequirements,
      search: search,
      newcomers: getNewcomers,
      pending: getPending,
      all: getAll,
      extend: extend,
      extendAll: extendAll,
      // api extension
      api: api
    };
  }

  var service = factory('default', BMA);

  service.instance = factory;
  return service;
}]);


angular.module('cesium.tx.services', ['ngApi', 'cesium.bma.services',
  'cesium.settings.services', 'cesium.wot.services' ])

.factory('csTx', ['$q', '$timeout', '$filter', '$translate', 'FileSaver', 'UIUtils', 'BMA', 'Api', 'csConfig', 'csSettings', 'csWot', 'csCurrency', function($q, $timeout, $filter, $translate, FileSaver, UIUtils, BMA, Api,
                          csConfig, csSettings, csWot, csCurrency) {
  'ngInject';

  function factory(id, BMA) {

    var
      api = new Api(this, "csTx-" + id),

      _reduceTxAndPush = function(pubkey, txArray, result, processedTxMap, allowPendings) {
        if (!txArray || txArray.length === 0) {
          return;
        }

        _.forEach(txArray, function(tx) {
          if (tx.block_number || allowPendings) {
            var walletIsIssuer = false;
            var otherIssuer = tx.issuers.reduce(function(issuer, res) {
              walletIsIssuer = (res === pubkey) ? true : walletIsIssuer;
              return issuer + ((res !== pubkey) ? ', ' + res : '');
            }, '');
            if (otherIssuer.length > 0) {
              otherIssuer = otherIssuer.substring(2);
            }
            var otherReceiver;
            var outputBase;
            var sources = [];
            var lockedOutputs;
            var amount = tx.outputs.reduce(function(sum, output, noffset) {
              // FIXME duniter v1.4.13
              var outputArray = (typeof output == 'string') ? output.split(':',3) : [output.amount,output.base,output.conditions];
              outputBase = parseInt(outputArray[1]);
              var outputAmount = powBase(parseInt(outputArray[0]), outputBase);
              var outputCondition = outputArray[2];
              var sigMatches =  BMA.regexp.TX_OUTPUT_SIG.exec(outputCondition);

              // Simple unlock condition
              if (sigMatches) {
                var outputPubkey = sigMatches[1];
                if (outputPubkey == pubkey) { // output is for the wallet
                  if (!walletIsIssuer) {
                    return sum + outputAmount;
                  }
                  // If pending: use output as new sources
                  else if (tx.block_number === null) {
                    sources.push({
                      amount: parseInt(outputArray[0]),
                      base: outputBase,
                      type: 'T',
                      identifier: tx.hash,
                      noffset: noffset,
                      consumed: false
                    });
                  }
                }
                else { // output is for someone else
                  if (outputPubkey !== '' && outputPubkey != otherIssuer) {
                    otherReceiver = outputPubkey;
                  }
                  if (walletIsIssuer) {
                    return sum - outputAmount;
                  }
                }
              }

              // Complex unlock condition, on the issuer pubkey
              else if (outputCondition.indexOf('SIG('+pubkey+')') != -1) {
                var lockedOutput = BMA.tx.parseUnlockCondition(outputCondition);
                if (lockedOutput) {
                  // Add a source
                  // FIXME: should be uncomment when filtering source on transfer()
                  /*sources.push(angular.merge({
                   amount: parseInt(outputArray[0]),
                   base: outputBase,
                   type: 'T',
                   identifier: tx.hash,
                   noffset: noffset,
                   consumed: false
                   }, lockedOutput));
                   */
                  lockedOutput.amount = outputAmount;
                  lockedOutputs = lockedOutputs || [];
                  lockedOutputs.push(lockedOutput);
                  console.debug('[tx] has locked output:', lockedOutput);

                  return sum + outputAmount;
                }
              }
              return sum;
            }, 0);

            var txPubkey = amount > 0 ? otherIssuer : otherReceiver;
            var time = tx.time || tx.blockstampTime;

            // Avoid duplicated tx, or tx to him self
            var txKey = amount + ':' + tx.hash + ':' + time;
            if (!processedTxMap[txKey] && amount !== 0) {
              processedTxMap[txKey] = true;
              var newTx = {
                time: time,
                amount: amount,
                pubkey: txPubkey,
                comment: tx.comment,
                isUD: false,
                hash: tx.hash,
                locktime: tx.locktime,
                block_number: tx.block_number
              };
              // If pending: store sources and inputs for a later use - see method processTransactionsAndSources()
              if (walletIsIssuer && tx.block_number === null) {
                newTx.inputs = tx.inputs;
                newTx.sources = sources;
              }
              if (lockedOutputs) {
                newTx.lockedOutputs = lockedOutputs;
              }
              result.push(newTx);
            }
          }
        });
      },

      loadTx = function(pubkey, fromTime) {
        return $q(function(resolve, reject) {
          var txHistory = [];
          var udHistory = [];
          var txPendings = [];

          var nowInSec = Math.trunc(new Date().getTime() / 1000); // TODO test to replace using moment().utc().unix()
          fromTime = fromTime || (nowInSec - csSettings.data.walletHistoryTimeSecond);
          var processedTxMap = {};
          var tx = {
            pendings: []
          };

          var _reduceTx = function(res){
            _reduceTxAndPush(pubkey, res.history.sent, txHistory, processedTxMap);
            _reduceTxAndPush(pubkey, res.history.received, txHistory, processedTxMap);
            _reduceTxAndPush(pubkey, res.history.sending, txPendings, processedTxMap, true /*allow pendings*/);
            _reduceTxAndPush(pubkey, res.history.pending, txPendings, processedTxMap, true /*allow pendings*/);
          };

          var jobs = [
            // get pendings history
            BMA.tx.history.pending({pubkey: pubkey})
              .then(_reduceTx)
          ];

          // get TX history since
          if (fromTime !== -1) {
            var sliceTime = csSettings.data.walletHistorySliceSecond;
            fromTime = fromTime - (fromTime % sliceTime);
            for(var i = fromTime; i - sliceTime < nowInSec; i += sliceTime)  {
              var startTime = Math.max(i, fromTime);
              jobs.push(BMA.tx.history.times({pubkey: pubkey, from: i, to: i+sliceTime-1})
                .then(_reduceTx)
              );
            }

            jobs.push(BMA.tx.history.timesNoCache({pubkey: pubkey, from: nowInSec - (nowInSec % sliceTime), to: nowInSec+999999999})
              .then(_reduceTx));
          }

          // get all TX
          else {
            jobs.push(BMA.tx.history.all({pubkey: pubkey})
              .then(_reduceTx)
            );
          }

          // get UD history
          if (csSettings.data.showUDHistory) {
            /*jobs.push(
              BMA.ud.history({pubkey: pubkey})
                .then(function(res){
                  udHistory = !res.history || !res.history.history ? [] :
                    res.history.history.reduce(function(res, ud){
                      if (ud.time < fromTime) return res; // skip to old UD
                      var amount = powBase(ud.amount, ud.base);
                      return res.concat({
                        time: ud.time,
                        amount: amount,
                        isUD: true,
                        block_number: ud.block_number
                      });
                    }, []);
                }));*/
            // API extension
            jobs.push(
              api.data.raisePromise.loadUDs({
                pubkey: pubkey,
                fromTime: fromTime
              })
                .then(function(res) {
                  if (!res || !res.length) return;
                  udHistory = res.reduce(function(res, hits) {
                    return res.concat(hits);
                  }, udHistory);
                })

                .catch(function(err) {
                  console.debug('Error while loading UDs history, on extension point.');
                  console.error(err);
                })
              );
          }

          // Execute jobs
          $q.all(jobs)
            .then(function(){
              // sort by time desc
              tx.history  = txHistory.concat(udHistory).sort(function(tx1, tx2) {
                return (tx2.time - tx1.time);
              });
              tx.pendings = txPendings;

              tx.fromTime = fromTime;
              tx.toTime = tx.history.length ? tx.history[0].time /*=max(tx.time)*/: fromTime;


              resolve(tx);
            })
            .catch(function(err) {
              tx.history = [];
              tx.pendings = [];
              tx.errors = [];
              delete tx.fromTime;
              delete tx.toTime;
              reject(err);
            });
        });
      },

      powBase = function(amount, base) {
        return base <= 0 ? amount : amount * Math.pow(10, base);
      },

      addSource = function(src, sources, sourcesIndexByKey) {
        var srcKey = src.type+':'+src.identifier+':'+src.noffset;
        if (angular.isUndefined(sourcesIndexByKey[srcKey])) {
          sources.push(src);
          sourcesIndexByKey[srcKey] = sources.length - 1;
        }
      },

      addSources = function(result, sources) {
        _(sources).forEach(function(src) {
          addSource(src, result.sources, result.sourcesIndexByKey);
        });
      },

      loadSourcesAndBalance = function(pubkey) {
        return BMA.tx.sources({pubkey: pubkey})
          .then(function(res){
            var result = {
              sources: [],
              sourcesIndexByKey: [],
              balance: 0
            };
            if (res.sources && res.sources.length) {
              _.forEach(res.sources, function(src) {
                src.consumed = false;
                result.balance += powBase(src.amount, src.base);
              });
              addSources(result, res.sources);
            }
            return result;
          });
      },

      loadData = function(pubkey, fromTime) {
        var now = new Date().getTime();

        var data = {};
        return $q.all([
            // Load Sources
            loadSourcesAndBalance(pubkey),

            // Load Tx
            loadTx(pubkey, fromTime)
          ])

          .then(function(res) {
            angular.merge(data, res[0]);
            data.tx = res[1];

            var txPendings = [];
            var txErrors = [];
            var balance = data.balance;

            function _processPendingTx(tx) {
              var consumedSources = [];
              var valid = true;
              if (tx.amount > 0) { // do not check sources from received TX
                valid = false;
                // TODO get sources from the issuer ?
              }
              else {
                _.forEach(tx.inputs, function(input) {
                  var inputKey = input.split(':').slice(2).join(':');
                  var srcIndex = data.sourcesIndexByKey[inputKey];
                  if (angular.isDefined(srcIndex)) {
                    consumedSources.push(data.sources[srcIndex]);
                  }
                  else {
                    valid = false;
                    return false; // break
                  }
                });
                if (tx.sources) { // add source output
                  addSources(data, tx.sources);
                }
                delete tx.sources;
                delete tx.inputs;
              }
              if (valid) {
                balance += tx.amount; // update balance
                txPendings.push(tx);
                _.forEach(consumedSources, function(src) {
                  src.consumed=true;
                });
              }
              else {
                txErrors.push(tx);
              }
            }

            var txs = data.tx.pendings;
            var retry = true;
            while(txs && txs.length > 0) {
              // process TX pendings
              _.forEach(txs, _processPendingTx);

              // Retry once (TX could be chained and processed in a wrong order)
              if (txErrors.length > 0 && txPendings.length > 0 && retry) {
                txs = txErrors;
                txErrors = [];
                retry = false;
              }
              else {
                txs = null;
              }
            }

            data.tx.pendings = txPendings;
            data.tx.errors = txErrors;
            data.balance = balance;

            // Will add uid (+ plugin will add name, avatar, etc. if enable)
            return csWot.extendAll((data.tx.history || []).concat(data.tx.pendings||[]), 'pubkey');
          })
          .then(function() {
            console.debug('[tx] TX and sources loaded in '+ (new Date().getTime()-now) +'ms');
            return data;
          });
      };

    // Download TX history file
    downloadHistoryFile = function(pubkey, options) {

      options = options || {};
      options.fromTime = options.fromTime || -1;

      console.debug("[tx] Exporting TX history for pubkey [{0}]".format(pubkey.substr(0,8)));

      return $q.all([
        $translate(['ACCOUNT.HEADERS.TIME',
          'COMMON.UID',
          'COMMON.PUBKEY',
          'COMMON.UNIVERSAL_DIVIDEND',
          'ACCOUNT.HEADERS.AMOUNT',
          'ACCOUNT.HEADERS.COMMENT']),
        csCurrency.blockchain.current(true/*withCache*/),
        loadData(pubkey, options.fromTime)
      ])
        .then(function(result){

          var translations = result[0];

          var currentBlock = result[1];
          var currentTime = (currentBlock && currentBlock.medianTime) || moment().utc().unix();
          var currency = currentBlock && currentBlock.currency;

          result = result[2];

          // no TX
          if (!result || !result.tx || !result.tx.history) {
            return UIUtils.toast.show('INFO.EMPTY_TX_HISTORY');
          }

          return $translate('ACCOUNT.FILE_NAME', {currency: currency, pubkey: pubkey, currentTime : currentTime})
            .then(function(filename){

              var formatDecimal = $filter('formatDecimal');
              var formatPubkey = $filter('formatPubkey');
              var formatDate = $filter('formatDate');
              var formatDateForFile = $filter('formatDateForFile');
              var formatSymbol = $filter('currencySymbolNoHtml');

              var headers = [
                translations['ACCOUNT.HEADERS.TIME'],
                translations['COMMON.UID'],
                translations['COMMON.PUBKEY'],
                translations['ACCOUNT.HEADERS.AMOUNT'] + ' (' + formatSymbol(currency) + ')',
                translations['ACCOUNT.HEADERS.COMMENT']
              ];
              var content = result.tx.history.reduce(function(res, tx){
                return res.concat([
                    formatDate(tx.time),
                    tx.uid,
                    tx.pubkey,
                    formatDecimal(tx.amount/100),
                    '"' + (tx.isUD ? translations['COMMON.UNIVERSAL_DIVIDEND'] : tx.comment) + '"'
                  ].join(';') + '\n');
              }, [headers.join(';') + '\n']);

              var file = new Blob(content, {type: 'text/plain; charset=utf-8'});
              FileSaver.saveAs(file, filename);
            });
        });
    };

    // Register extension points
    api.registerEvent('data', 'loadUDs');

    return {
      id: id,
      load: loadData,
      downloadHistoryFile: downloadHistoryFile,
      // api extension
      api: api
    };
  }

  var service = factory('default', BMA);

  service.instance = factory;
  return service;
}]);


angular.module('cesium.wallet.services', ['ngApi', 'ngFileSaver', 'cesium.bma.services', 'cesium.crypto.services', 'cesium.utils.services',
  'cesium.settings.services'])


.factory('csWallet', ['$q', '$rootScope', '$timeout', '$translate', '$filter', '$ionicHistory', 'UIUtils', 'Api', 'Idle', 'localStorage', 'sessionStorage', 'Modals', 'CryptoUtils', 'BMA', 'csConfig', 'csSettings', 'FileSaver', 'Blob', 'csWot', 'csTx', 'csCurrency', function($q, $rootScope, $timeout, $translate, $filter, $ionicHistory, UIUtils,
                              Api, Idle, localStorage, sessionStorage, Modals,
                              CryptoUtils, BMA, csConfig, csSettings, FileSaver, Blob, csWot, csTx, csCurrency) {
  'ngInject';

  function factory(id, BMA) {

    var
    constants = {
      // @Deprecated
      OLD_STORAGE_KEY: 'CESIUM_DATA',
      STORAGE_PUBKEY: 'pubkey',
      STORAGE_UID: 'uid',
      STORAGE_SECKEY: 'seckey',
      /* Need for compat with old currencies (test_net and sou) */
      TX_VERSION:   csConfig.compatProtocol_0_80 ? 3 : BMA.constants.PROTOCOL_VERSION,
      IDTY_VERSION: csConfig.compatProtocol_0_80 ? 2 : BMA.constants.PROTOCOL_VERSION,
      MS_VERSION:   csConfig.compatProtocol_0_80 ? 2 : BMA.constants.PROTOCOL_VERSION,
      CERT_VERSION: csConfig.compatProtocol_0_80 ? 2 : BMA.constants.PROTOCOL_VERSION,
      REVOKE_VERSION: csConfig.compatProtocol_0_80 ? 2 : BMA.constants.PROTOCOL_VERSION,
      TX_MAX_INPUTS_COUNT: 40 // Allow to get a TX with less than 100 rows (=max row count in Duniter protocol)
    },
    data = {},
    listeners,
    started,
    startPromise,
    loadPromise,
    enableAuthIdle = false,
    api = new Api(this, 'csWallet-' + id),

    resetData = function(init) {
      data.loaded = false;
      data.pubkey= null;

      data.uid = null;
      data.isNew = null;
      data.sourcesIndexByKey = null;
      data.medianTime = null;
      data.requirements = {};
      data.blockUid = null;
      data.sigDate = null;
      data.isMember = false;
      data.events = [];

      resetKeypair();
      resetTxAndSources();

      started = false;
      startPromise = undefined;

      if (init) {
        api.data.raise.init(data);
      }
      else {
        if (!csSettings.data.useLocalStorage) {
          csSettings.reset();
        }
        api.data.raise.reset(data);
      }
    },

    resetKeypair = function(){
      data.keypair = {
        signSk: null,
        signPk: null
      };
    },

    resetTxAndSources = function(){
      // reset sources data
      data.sources = undefined;
      data.sourcesIndexByKey = undefined;
      data.balance = 0;
      // reset TX data
      data.tx = data.tx || {};
      data.tx.history = [];
      data.tx.pendings = [];
      data.tx.errors = [];
      delete data.tx.fromTime;
      delete data.tx.toTime;
    },

    addSource = function(src, sources, sourcesIndexByKey) {
      var srcKey = src.type+':'+src.identifier+':'+src.noffset;
      if (angular.isUndefined(sourcesIndexByKey[srcKey])) {
        sources.push(src);
        sourcesIndexByKey[srcKey] = sources.length - 1;
      }
    },

    addSources = function(sources) {
      data.sources = data.sources || [];
      data.sourcesIndexByKey = data.sourcesIndexByKey || {};
      _(sources).forEach(function(src) {
        addSource(src, data.sources, data.sourcesIndexByKey);
      });
    },

    // Show login modal
    login = function(options) {
      if (!started) {
        return (startPromise || start())
          .then(function () {
            return login(options); // loop
          });
      }

      var needLogin = !isLogin();
      var needAuth = options && ((options.auth && !isAuth()) || options.forceAuth);

      // user already login
      if (!needLogin && !needAuth) {
        if (!isDataLoaded(options)) {
          return loadData(options);
        }
        return $q.when(data);
      }
      var keepAuth = csSettings.data.keepAuthIdle > 0;

      var authData;
      return (options && options.authData && $q.when(options.authData) ||
        Modals.showLogin(options))
        .then(function(res){
          if (!res || !res.pubkey ||
             (!needLogin && res.pubkey !== data.pubkey) ||
             (needAuth && (!res.keypair || !res.keypair.signPk || !res.keypair.signSk))) {
            throw 'CANCELLED';
          } // invalid data

          authData = res;
          data.pubkey = res.pubkey;
          data.isNew = options && angular.isDefined(options.isNew) ? options.isNew : data.isNew;
          if (keepAuth) {
            data.keypair = res.keypair || {
                signSk: null,
                signPk: null
              };
          }

          if (needLogin) {
            // extend API to check login validity
            return api.data.raisePromise.loginCheck(data)
              .catch(function (err) {
                resetData(); // Reset data if not valid, then exit process
                throw err;
              })
              // Call extend api
              .then(function() {
                if (needLogin) {
                  return api.data.raisePromise.login(data)
                    .catch(function(err) {
                      console.warn('Error during extension call [wallet.api.data.on.login]', err);
                      // continue
                    });
                }
              });
          }
        })

        .then(function() {
          // store wallet if need
          if (csSettings.data.useLocalStorage) {
            store();
          }

          // Send auth event (if need)
          if (needAuth || isAuth()) {
            // Check if need to start/stop auth idle
            checkAuthIdle(true);

            return api.data.raisePromise.auth(keepAuth ? data : authData);
          }
        }).then(function() {
          // Load data if need
          // If user just login, force data full load (even if min data asked)
          // because the user can wait (after the login modal)
          var loadOptions = !needLogin && options && options.minData ? {minData: true} : undefined/*=load all*/;
          if (!isDataLoaded(loadOptions)) {
            return loadData(loadOptions);
          }
        })
        .then(function() {
          if (options && options.silent) {
            UIUtils.loading.hide();
          }

          return keepAuth ? data : angular.merge({}, data, authData);
        })
        .catch(function(err) {
          if (err == 'RETRY' && (!options || !options.authData)) {
            return $timeout(function(){
              return login(options);
            }, 300);
          }
          throw err;
        });
    },

    logout = function() {
      var wasAuth = isAuth();

      return $q(function(resolve, reject) {

        resetData(); // will reset keypair
        store(); // store (if local storage enable)

        // Send logout event
        api.data.raise.logout();

        if (wasAuth) {
          api.data.raise.unauth();
        }

        checkAuthIdle(false);

        $ionicHistory.clearCache();

        resolve();
      });
    },

    isLogin = function() {
      return !!data.pubkey;
    },

    auth = function(options) {
      if (!started) {
        return (startPromise || start())
          .then(function () {
            return auth(options); // loop
          });
      }

      if (isAuth() && (!options || !options.forceAuth)) {
        return $q.when(data);
      }

      options = options || {};
      options.expectedPubkey = isLogin() && data.pubkey;
      options.auth = true;
      return login(options);
    },

    unauth = function() {
      return $q(function(resolve, reject) {

        resetKeypair();
        store();

        // Send unauth event
        api.data.raise.unauth();

        checkAuthIdle(false);

        $ionicHistory.clearCache();

        resolve();
      });
    },

    isAuth = function() {
      return !!(data.pubkey && data.keypair && data.keypair.signSk);
    },

    getKeypair = function(options) {
      if (!started) {
        return (startPromise || start())
          .then(function () {
            return getKeypair(options); // loop
          });
      }

      if (isAuth()) {
        return $q.when(data.keypair);
      }
      options = options || {};
      options.silent = angular.isDefined(options.silent) ? options.silent : true;
      return auth(options)
        .then(function() {
          return data.keypair;
        });
    },

    hasSelf = function() {
      return !!data.pubkey && data.requirements && !data.requirements.needSelf;
    },

    isDataLoaded = function(options) {
      if (options && options.minData) return data.loaded;
      return data.loaded && data.sources;
    },

    isNeverUsed = function() {
      if (!data.loaded) return undefined; // undefined if not full loaded
      return !data.pubkey || !(
         // Check registration
         data.isMember ||
         data.requirements.pendingMembership ||
         !data.requirements.needSelf ||
         data.requirements.wasMember ||

         // Check TX history
         data.tx.history.length ||
         data.tx.pendings.length ||

         // Check extended data (name+avatar)
         data.name ||
         data.avatar
        );
    },

    isNew = function() {return !!data.isNew;},

    // If connected and same pubkey
    isUserPubkey = function(pubkey) {
      return isLogin() && data.pubkey === pubkey;
    },

    store = function() {
      if (csSettings.data.useLocalStorage) {

        if (isLogin() && csSettings.data.rememberMe) {

          var jobs = [];

          // Use session storage for secret key - fix #372
          if (csSettings.data.keepAuthIdle == constants.KEEP_AUTH_IDLE_SESSION && isAuth()) {
            jobs.push(sessionStorage.put(constants.STORAGE_SECKEY, CryptoUtils.base58.encode(data.keypair.signSk)));
          }
          else {
            jobs.push(sessionStorage.put(constants.STORAGE_SECKEY, null));
          }

          // Use local storage for pubkey
          jobs.push(localStorage.put(constants.STORAGE_PUBKEY, data.pubkey));

          // Use local storage for uid - fix #625
          if (data.uid) {
            jobs.push(localStorage.put(constants.STORAGE_UID, data.uid));
          }
          else {
            jobs.push(localStorage.put(constants.STORAGE_UID, null));
          }

          // Clean old storage
          jobs.push(localStorage.put(constants.OLD_STORAGE_KEY, null));

          return $q.all(jobs).then(function() {
            console.debug('[wallet] Saved locally');
          });
        }
        else {
          return $q.all([
            sessionStorage.put(constants.STORAGE_SECKEY, null),
            localStorage.put(constants.STORAGE_PUBKEY, null),
            localStorage.put(constants.STORAGE_UID, null),
            // Clean old storage
            localStorage.put(constants.OLD_STORAGE_KEY, null)
          ]);
        }
      }
      else {
        return $q.all([
          sessionStorage.put(constants.STORAGE_SECKEY, null),
          localStorage.put(constants.STORAGE_PUBKEY, null),
          localStorage.put(constants.STORAGE_UID, null),
          // Clean old storage
          localStorage.put(constants.OLD_STORAGE_KEY, null)
        ]);

      }
    },

    restore = function() {
      return  $q.all([
          sessionStorage.get(constants.STORAGE_SECKEY),
          localStorage.get(constants.STORAGE_PUBKEY),
          localStorage.get(constants.STORAGE_UID)
        ])
        .then(function(res) {
          var seckey = res[0];
          var pubkey = res[1];
          var uid = res[2];
          if (!pubkey || pubkey == 'null') return;

          var keypair;
          if (seckey && seckey.length && seckey != 'null') {
            try {
              keypair = {
                signPk: CryptoUtils.util.decode_base58(pubkey),
                signSk: CryptoUtils.util.decode_base58(seckey)
              };
            }
            catch(err) {
              console.warn('[wallet] Secret key restoration failed: ', err);
              keypair = undefined;
            }
          }

          data.pubkey = pubkey;
          data.uid = uid && uid != 'null' ? uid : undefined;
          data.keypair = keypair || {signPk: undefined, signSk: undefined};

          console.debug('[wallet] Restore \'{0}\' from local storage.'.format(pubkey.substring(0,8)));

          // Call extend api
          return  api.data.raisePromise.login(data);
        })
        .then(function(){
          return data;
        });
    },

    getData = function() {
      return data;
    },

    loadRequirements = function() {
      // Clean existing events
      cleanEventsByContext('requirements');

      // Get requirements
      return csWot.loadRequirements(data)
        .then(function(){

          if (!data.requirements.uid) return;

          // Get sigDate
          var blockParts = data.requirements.blockUid.split('-', 2);
          var blockNumber = parseInt(blockParts[0]);
          var blockHash = blockParts[1];
          // Retrieve registration date
          return BMA.blockchain.block({block: blockNumber})
            .then(function(block) {
              data.sigDate = block.medianTime;

              // Check if self has been done on a valid block
              if (!data.isMember && blockNumber !== 0 && blockHash !== block.hash) {
                addEvent({type: 'error', message: 'ERROR.WALLET_INVALID_BLOCK_HASH', context: 'requirements'});
                console.debug("Invalid membership for uid={0}: block hash changed".format(data.uid));
              }
              // Check if self expired
              else if (!data.isMember && data.requirements.expired) {
                addEvent({type: 'error', message: 'ERROR.WALLET_IDENTITY_EXPIRED', context: 'requirements'});
                console.debug("Identity expired for uid={0}.".format(data.uid));
              }
            })
            .catch(function(err){
              // Special case for currency init (root block not exists): use now
              if (err && err.ucode == BMA.errorCodes.BLOCK_NOT_FOUND && blockNumber === 0) {
                data.sigDate = Math.trunc(new Date().getTime() / 1000);
              }
              else {
                throw err;
              }
            });
        });
    },

    loadTxAndSources = function(fromTime) {
      return csTx.load(data.pubkey, fromTime)
        .then(function(res){
          resetTxAndSources();
          angular.merge(data, res);
        })
        .catch(function(err) {
          resetTxAndSources();
          throw err;
        });
    },

    // Generate events from requirements
    addEvents = function() {

      // Add user events
      if (data.requirements.revoked) {
        addEvent({type:'warn', message: 'ERROR.WALLET_REVOKED', context: 'requirements'});
      }
      else if (data.requirements.pendingRevocation) {
        addEvent({type:'pending', message: 'INFO.REVOCATION_SENT_WAITING_PROCESS', context: 'requirements'});
      }
      else {
        if (data.requirements.pendingMembership) {
          addEvent({type:'pending', message: 'ACCOUNT.WAITING_MEMBERSHIP', context: 'requirements'});
        }
        // If user has send a SELF, ask for membership - fix #625
        else if (!data.requirements.needSelf && data.requirements.needMembership){
          addEvent({type:'warn', message: 'ACCOUNT.NO_WAITING_MEMBERSHIP', context: 'requirements'});
        }
        if (data.requirements.needCertificationCount > 0) {
          addEvent({type:'warn', message: 'ACCOUNT.WAITING_CERTIFICATIONS', messageParams: data.requirements, context: 'requirements'});
        }
        if (data.requirements.willNeedCertificationCount > 0) {
          addEvent({type:'warn', message: 'ACCOUNT.WILL_MISSING_CERTIFICATIONS', messageParams: data.requirements, context: 'requirements'});
        }
        if (data.requirements.needRenew) {
          addEvent({type:'warn', message: 'ACCOUNT.WILL_NEED_RENEW_MEMBERSHIP', messageParams: data.requirements, context: 'requirements'});
        }
        else if (data.requirements.wasMember && data.requirements.needMembership) {
          addEvent({type:'warn', message: 'ACCOUNT.NEED_RENEW_MEMBERSHIP', messageParams: data.requirements, context: 'requirements'});
        }
      }
    },

    loadSigStock = function() {
      return $q(function(resolve, reject) {
        // Get certified by, then count written certification
        BMA.wot.certifiedBy({pubkey: data.pubkey})
          .then(function(res){
            data.sigStock = !res.certifications ? 0 : res.certifications.reduce(function(res, cert) {
              return cert.written === null ? res : res+1;
            }, 0);
            resolve();
          })
          .catch(function(err) {
            if (!!err && err.ucode == BMA.errorCodes.NO_MATCHING_MEMBER) {
              data.sigStock = 0;
              resolve(); // not found
            }
            else {
              reject(err);
            }
          });
      });
    },

    loadData = function(options) {

      var alertIfUnusedWallet = !csCurrency.data.initPhase && (!csSettings.data.wallet || csSettings.data.wallet.alertIfUnusedWallet) &&
        !data.loaded && (!options || !options.minData);

      // Make sure to load once at a time
      if (loadPromise) {
        return loadPromise.then(function() {
          return isDataLoaded(options) ? data : refreshData(options);
        });
      }

      if (options && options.minData) {
        loadPromise = loadMinData(options);
      }
      else if (options || data.loaded) {
        loadPromise = refreshData(options);
      }
      else  {
        loadPromise = loadFullData();
      }

      return loadPromise

        // Warn if wallet has been never used - see #167
        .then(function() {
          var unused = isNeverUsed();
          var showAlert = alertIfUnusedWallet && !isNew() && angular.isDefined(unused) && unused;
          if (!showAlert) return true;
          return UIUtils.loading.hide()
            .then(function() {
              return UIUtils.alert.confirm('CONFIRM.LOGIN_UNUSED_WALLET', 'CONFIRM.LOGIN_UNUSED_WALLET_TITLE', {
                cancelText: 'COMMON.BTN_CONTINUE',
                okText: 'COMMON.BTN_RETRY'
              });
            })
            .then(function(retry) {
              if (retry) {
                return logout().then(function() {
                  throw 'RETRY';
                });
              }
              else {
                // Remembering to not ask for confirmation
                if (csSettings.data.wallet.alertIfUnusedWallet) {
                  csSettings.data.wallet.alertIfUnusedWallet = false;
                  csSettings.store();
                }
              }
              return true;
            });
        })

        // Return wallet data
        .then(function(confirm) {
          loadPromise = null;
          if (confirm) {
            return data;
          }
          else { // cancel

            throw 'CANCELLED';
          }
        });
    },

    loadFullData = function() {
      data.loaded = false;

      return $q.all([

          // Get requirements
          loadRequirements(),

          // Get TX and sources
          loadTxAndSources(),

          // Load sigStock
          loadSigStock()
        ])
        .then(function() {

          // Load wallet events
          addEvents();

          // API extension
          return api.data.raisePromise.load(data)
            .catch(function(err) {
              console.error('[wallet] Error during load API extension point. Try to continue',err);
            });
        })
        .then(function() {
          data.loaded = true;
          return data;
        })
        .catch(function(err) {
          data.loaded = false;
          throw err;
        });
    },

    loadMinData = function(options) {
      options = options || {};
      options.requirements = angular.isDefined(options.requirements) ? options.requirements :
        (!data.requirements || angular.isUndefined(data.requirements.needSelf));
      if (!options.requirements) {
        return $q.when(data);
      }
      return refreshData(options)
        .then(function(data) {
          data.loaded = true;
          return data;
        });
    },

    refreshData = function(options) {
        options = options || {
          requirements: true,
          sources: true,
          tx: {
            enable: true,
            fromTime: data.tx ? data.tx.fromTime : undefined // keep previous time
          },
          sigStock: true,
          api: true
        };

      // Force some load (requirements) if not already loaded
      options.requirements = angular.isDefined(options.requirements) ? options.requirements : angular.isDefined(data.requirements.needSelf);

      var jobs = [];

      // Get requirements
      if (options.requirements) {
        // Reset events
        cleanEventsByContext('requirements');

        jobs.push(
          loadRequirements()

            // Add wallet events
            .then(addEvents)
        );
      }

      if (options.sources || (options.tx && options.tx.enable)) {
        // Get TX and sources
        jobs.push(loadTxAndSources(options.tx ? options.tx.fromTime: undefined));
      }

      // Load sigStock
      if (options.sigStock) jobs.push(loadSigStock());

      return (jobs.length ? $q.all(jobs) : $q.when())
      .then(function(){
        // API extension (after all other jobs)
        return api.data.raisePromise.load(data);
      })
      .then(function(){
        return data;
      });
    },

    setSelf = function(uid, blockUid){
      // Skip if same self
      if (data.uid == uid && (!blockUid || data.blockUid == blockUid)) return $q.when();

      // Data not loaded
      if (!data.loaded) {
        return !loadPromise ?
          // If no pending load: ok
          $q.when() :
          // If a load is running: force a reload
          loadPromise.then(function() {
            return setSelf(uid, blockUid); // loop
          });
      }

      data.uid = uid;
      data.blockUid = blockUid;

      // Refresh requirements
      return refreshData({requirements: true, sigStock: true})
        // Store (to remember the new uid)
        .then(store);
    },

    isBase = function(amount, base) {
      if (!base) return true; // no base
      if (amount < Math.pow(10, base)) return false; // too small
      var rest = '00000000' + amount;
      var lastDigits = parseInt(rest.substring(rest.length-base));
      return lastDigits === 0; // no rest
    },

    truncBase = function(amount, base) {
      var pow = Math.pow(10, base); // = min value in this base
      if (amount < pow) return 0;
      return Math.trunc(amount / pow ) * pow;
    },

    truncBaseOrMinBase = function(amount, base) {
      var pow = Math.pow(10, base);
      if (amount < pow) return pow; //
      return Math.trunc(amount / pow ) * pow;
    },

    powBase = function(amount, base) {
      return base <= 0 ? amount : amount * Math.pow(10, base);
    },

    getInputs = function(amount, outputBase, filterBase) {
      if (angular.isUndefined(filterBase)) {
        filterBase = outputBase;
      }
      var sourcesAmount = 0;
      var sources = [];
      var minBase = filterBase;
      var maxBase = filterBase;
      _.find(data.sources, function(source) {
        if (!source.consumed && source.base == filterBase){
          sourcesAmount += powBase(source.amount, source.base);
          sources.push(source);
        }
        // Stop if enough sources
        return (sourcesAmount >= amount);
      });

      // IF not enough sources, get add inputs from lower base (recursively)
      if (sourcesAmount < amount && filterBase > 0) {
        filterBase -= 1;
        var missingAmount = amount - sourcesAmount;
        var lowerInputs = getInputs(missingAmount, outputBase, filterBase);

        // Add lower base inputs to result
        if (lowerInputs.amount > 0) {
          minBase = lowerInputs.minBase;
          sourcesAmount += lowerInputs.amount;
          [].push.apply(sources, lowerInputs.sources);
        }
      }

      return {
        minBase: minBase,
        maxBase: maxBase,
        amount: sourcesAmount,
        sources: sources
      };
    },

    /**
    * Send a new transaction
    */
    transfer = function(destPub, amount, comments, useRelative) {
      return $q.all([
          getKeypair(),
          csCurrency.get(),
          csCurrency.blockchain.current()
        ])
        .then(function(res) {
          var keypair = res[0];
          var currency = res[1];
          var block = res[2];
          if (!BMA.regexp.PUBKEY.test(destPub)){
            throw {message:'ERROR.INVALID_PUBKEY'};
          }
          if (!BMA.regexp.COMMENT.test(comments)){
            throw {message:'ERROR.INVALID_COMMENT'};
          }
          if (!isLogin()){
            throw {message:'ERROR.NEED_LOGIN_FIRST'};
          }
          if (!amount) {
            throw {message:'ERROR.AMOUNT_REQUIRED'};
          }
          if (amount <= 0) {
            throw {message:'ERROR.AMOUNT_NEGATIVE'};
          }
          amount = Math.floor(amount); // remove decimals

          var inputs = {
            amount: 0,
            minBase: block.unitbase,
            maxBase: block.unitbase + 1,
            sources : []
          };

          var logs = [];
          logs.push("[wallet] amount=" + amount);

          // Get inputs, starting to use current base sources
          var amountBase = 0;
          while (inputs.amount < amount && amountBase <= block.unitbase) {
            inputs = getInputs(amount, block.unitbase);

            if (inputs.amount < amount) {
              // try to reduce amount (replace last digits to zero)
              amountBase++;
              if (amountBase <= block.unitbase) {
                amount = truncBase(amount, amountBase);
                logs.push("[wallet] inputs not found. Retrying with amount =" + amount + " be compatible with amountBase=" + amountBase);
              }
            }
          }

          if (inputs.amount < amount) {
            if (data.balance < amount) {
              throw {message:'ERROR.NOT_ENOUGH_CREDIT'};
            }
            else if (inputs.amount === 0) {
              throw {message:'ERROR.ALL_SOURCES_USED'};
            }
            else {
              return $translate('COMMON.UD')
                .then(function(UD) {
                  var params;
                  if(useRelative) {
                    params = {
                      amount: ($filter('formatDecimal')(inputs.amount / currency.currentUD)),
                      unit: UD,
                      subUnit: $filter('abbreviate')(currency.name)
                    };
                  }
                  else {
                    params = {
                      amount: ($filter('formatDecimal')(inputs.amount/100)),
                      unit: $filter('abbreviate')(currency.name),
                      subUnit: ''
                    };
                  }
                  return $translate('ERROR.NOT_ENOUGH_SOURCES', params)
                    .then(function(message) {
                      throw {message: message};
                    });
                });
            }
          }
          // Avoid to get outputs on lower base
          if (amountBase < inputs.minBase && !isBase(amount, inputs.minBase)) {
            amount = truncBaseOrMinBase(amount, inputs.minBase);
            console.debug("[wallet] Amount has been truncate to " + amount);
            logs.push("[wallet] Amount has been truncate to " + amount);
          }
          else if (amountBase > 0) {
            console.debug("[wallet] Amount has been truncate to " + amount);
            logs.push("[wallet] Will use amount truncated to " + amount + " (amountBase="+amountBase+")");
          }

          // Send tx
          return createAndSendTx(currency, block, keypair, destPub, amount, inputs, comments, logs)
            .then(function(res) {
              data.balance -= amount;
              _.forEach(inputs.sources, function(source) {
                source.consumed=true;
              });

              // Add new sources
              if (res && res.sources.length) {
                addSources(res.sources);
              }

              // Add TX to pendings
              var pendingTx = {
                time: (Math.floor(moment().utc().valueOf() / 1000)),
                amount: -amount,
                pubkey: destPub,
                comment: comments,
                isUD: false,
                hash: res.hash,
                locktime: 0,
                block_number: null
              };
              return csWot.extendAll([pendingTx], 'pubkey')
                .then(function() {
                  data.tx.pendings.unshift(pendingTx);

                  // API extension
                  api.data.raise.balanceChanged(data);
                  api.data.raise.newTx(data);

                  // Return TX hash (if chained TXs, return the last tx hash) - required by Cesium-API
                  return {
                    hash: res.hash
                  };
                });
            })
            .catch(function(err) {

              // Source already consumed: whould refresh wallet sources
              if (err && err.ucode === BMA.errorCodes.SOURCE_ALREADY_CONSUMED) {
                console.debug('[wallet] TX rejected by node with error [{0}]. Reloading sources then retry...'.format(err.message||'Source already consumed'));
                return $timeout(loadTxAndSources, 500)
                  .then(function() {
                    return transfer(destPub, amount, comments, useRelative);
                  });
              }

              // Error in generated TX - issue #524
              else if (err && err.ucode === BMA.errorCodes.TX_INPUTS_OUTPUTS_NOT_EQUAL) {
                // Ask user to send log to developers
                var esEnable = csSettings.data.plugins && csSettings.data.plugins.es && csSettings.data.plugins.es.enable;
                if (esEnable) {
                  UIUtils.loading.hide();
                  return UIUtils.alert.confirm('CONFIRM.ISSUE_524_SEND_LOG', 'ERROR.POPUP_TITLE', {
                    cssClass: 'warning',
                    okText: 'COMMON.BTN_OK',
                    cancelText: 'COMMON.BTN_NO'
                  })
                  .then(function(confirm) {
                    if (confirm) {
                      api.error.raise.send({
                        title: 'Issue #524 logs',
                        content: 'App version: ' +csConfig.version+'\n'+
                        'App build: ' +csConfig.build+'\n'+
                        'Logs:\n\n' + logs.join('\n')
                      });
                      return $timeout(function() {
                        throw {message: 'ERROR.ISSUE_524_TX_FAILED'};
                      }, 1500);
                    }
                    throw {message: 'ERROR.SEND_TX_FAILED'};
                  });
                }
              }
              throw err;
            });
        });
    },

    /**
     * Create TX doc and send it
     * @param block the current block
     * @param destPub
     * @param amount
     * @param inputs
     * @param comments
     * @return the hash of the sent TX
     */
    createAndSendTx = function(currency, block, keypair, destPub, amount, inputs, comments, logs) {

      // Make sure a TX in compact mode has no more than 100 lines (fix #118)
      // (If more than 100 lines, send to TX to himself first, then its result as sources for the final TX)
      if (inputs.sources.length > constants.TX_MAX_INPUTS_COUNT) {
        console.debug("[Wallet] TX has to many sources. Will chain TX...");

        // Compute a slice of sources
        var firstSlice = {
          minBase: block.unitbase,
          maxBase: 0,
          amount: 0,
          sources: inputs.sources.slice(0, constants.TX_MAX_INPUTS_COUNT) /* end index is excluded, so array length=TX_MAX_INPUTS_COUNT - issue #524 */
        };
        _.forEach(firstSlice.sources, function(source) {
          if (source.base < firstSlice.minBase) firstSlice.minBase = source.base;
          if (source.base > firstSlice.maxBase) firstSlice.maxBase = source.base;
          firstSlice.amount += powBase(source.amount, source.base);
        });

        // Send inputs first slice
        return createAndSendTx(currency, block, keypair, data.pubkey/*to himself*/, firstSlice.amount, firstSlice, undefined/*comment not need*/, logs)
          .then(function(res) {
            _.forEach(firstSlice.sources, function(source) {
              source.consumed=true;
            });
            addSources(res.sources);

            var secondSlice = {
              minBase: block.unitbase,
              maxBase: 0,
              amount: 0,
              sources: inputs.sources.slice(constants.TX_MAX_INPUTS_COUNT).concat(res.sources)
            };
            _.forEach(secondSlice.sources, function(source) {
              if (source.base < secondSlice.minBase) secondSlice.minBase = source.base;
              if (source.base > secondSlice.maxBase) secondSlice.maxBase = source.base;
              secondSlice.amount += source.amount;
            });

            // Send inputs second slice (recursive call)
            return createAndSendTx(currency, block, keypair, destPub, amount, secondSlice, comments, logs);
          });
      }

      var tx = 'Version: '+ constants.TX_VERSION +'\n' +
        'Type: Transaction\n' +
        'Currency: ' + currency.name + '\n' +
        'Blockstamp: ' + block.number + '-' + block.hash + '\n' +
        'Locktime: 0\n' + // no lock
        'Issuers:\n' +
        data.pubkey + '\n' +
        'Inputs:\n';

      _.forEach(inputs.sources, function(source) {
        // if D : AMOUNT:BASE:D:PUBLIC_KEY:BLOCK_ID
        // if T : AMOUNT:BASE:T:T_HASH:T_INDEX
        tx += [source.amount, source.base, source.type, source.identifier,source.noffset].join(':')+"\n";
      });

      tx += 'Unlocks:\n';
      for (i=0; i<inputs.sources.length; i++) {
        // INPUT_INDEX:UNLOCK_CONDITION
        tx += i + ':SIG(0)\n';
      }

      tx += 'Outputs:\n';
      // AMOUNT:BASE:CONDITIONS
      var rest = amount;
      var outputBase = inputs.maxBase;
      var outputAmount;
      var outputOffset = 0;
      var newSources = [];
      // Outputs to receiver (if not himself)
      if (destPub !== data.pubkey) {
        while(rest > 0) {
          outputAmount = truncBase(rest, outputBase);
          rest -= outputAmount;
          if (outputAmount > 0) {
            outputAmount = outputBase === 0 ? outputAmount : outputAmount / Math.pow(10, outputBase);
            tx += outputAmount + ':' + outputBase + ':SIG(' + destPub + ')\n';
            outputOffset++;
          }
          outputBase--;
        }
        rest = inputs.amount - amount;
        outputBase = inputs.maxBase;
      }
      // Outputs to himself
      while(rest > 0) {
        outputAmount = truncBase(rest, outputBase);
        rest -= outputAmount;
        if (outputAmount > 0) {
          outputAmount = outputBase === 0 ? outputAmount : outputAmount / Math.pow(10, outputBase);
          tx += outputAmount +':'+outputBase+':SIG('+data.pubkey+')\n';
          newSources.push({
            type: 'T',
            noffset: outputOffset,
            amount: outputAmount,
            base: outputBase
          });
          outputOffset++;
        }
        outputBase--;
      }

      tx += "Comment: "+ (comments||"") + "\n";

      // Append to logs (need to resolve issue #524)
      if (logs) {
        if (destPub == data.pubkey) {
          logs.push('[wallet] Creating new TX, using inputs:\n - minBase: '+inputs.minBase+'\n - maxBase: '+inputs.maxBase);
        }
        else {
          logs.push('[wallet] Creating new TX, using inputs:\n - minBase: '+inputs.minBase+'\n - maxBase: '+inputs.maxBase + '\n - sources (=TX inputs):');
        }
        _.forEach(inputs.sources, function(source) {
          logs.push([source.amount, source.base, source.type, source.identifier,source.noffset].join(':'));
        });
        logs.push("\n[wallet] generated TX document (without signature) :\n------ START ------\n" + tx + "------ END ------\n");
      }

      return CryptoUtils.sign(tx, keypair)
        .then(function(signature) {
          var signedTx = tx + signature + "\n";
          return BMA.tx.process({transaction: signedTx})
            .catch(function(err) {
              if (err && err.ucode === BMA.errorCodes.TX_ALREADY_PROCESSED) {
                // continue
                return;
              }
              throw err;
            })
            .then(function() {
              return CryptoUtils.util.hash(signedTx);
            })
            .then(function(txHash) {
              _.forEach(newSources, function(output) {
                output.identifier= txHash;
                output.consumed = false;
                output.pending = true;
              });
              return {
                tx: signedTx,
                hash: txHash,
                sources: newSources
              };
            });
        });
    },

    getIdentityDocument = function(currency, keypair, uid, blockUid) {
      uid = uid || data.uid;
      blockUid = blockUid || data.blockUid;
      if (!uid || !blockUid) {
        throw {message: 'ERROR.WALLET_HAS_NO_SELF'};
      }
      if (data.requirements && data.requirements.expired) {
        throw {message: 'ERROR.WALLET_IDENTITY_EXPIRED'};
      }

      var identity = 'Version: '+ constants.IDTY_VERSION +'\n' +
        'Type: Identity\n' +
        'Currency: ' + currency.name + '\n' +
        'Issuer: ' + data.pubkey + '\n' +
        'UniqueID: ' + uid + '\n' +
        'Timestamp: ' + blockUid + '\n';
      return CryptoUtils.sign(identity, keypair)
        .then(function(signature) {
          identity += signature + '\n';
          console.debug('Has generate an identity document:\n----\n' + identity + '----');
          return identity;
        });
    },

    /**
    * Send self identity
    */
    self = function(uid, needToLoadRequirements) {
        if (!BMA.regexp.USER_ID.test(uid)){
          return $q.reject({message: 'ERROR.INVALID_USER_ID'});
        }
        var block;
        return $q.all([
          getKeypair(),
          csCurrency.get(),
          csCurrency.blockchain.current()
        ])
        // Create identity document
        .then(function(res) {
          var keypair = res[0];
          var currency = res[1];
          block = res[2];
          return getIdentityDocument(currency, keypair, uid, block.number + '-' + block.hash);
        })

        // Send to node
        .then(function (identity) {
          return BMA.wot.add({identity: identity});
        })

        .then(function () {
          if (!!needToLoadRequirements) {
            // Refresh membership data (if need)
            return loadRequirements();
          }
          else {
            data.uid = uid;
            data.blockUid = block.number + '-' + block.hash;
          }
        })
        .catch(function (err) {
          if (err && err.ucode === BMA.errorCodes.IDENTITY_SANDBOX_FULL) {
            throw {ucode: BMA.errorCodes.IDENTITY_SANDBOX_FULL, message: 'ERROR.IDENTITY_SANDBOX_FULL'};
          }
          throw err;
        });
    },

   /**
    * Send membership (in or out)
    */
    membership = function(sideIn) {
      return function() {
        var membership;

        return $q.all([
            getKeypair(),
            csCurrency.blockchain.current()
          ])
          .then(function(res) {
            var keypair = res[0];
            var block = res[1];
            // Create membership to sign
            membership = 'Version: '+ constants.MS_VERSION +'\n' +
              'Type: Membership\n' +
              'Currency: ' + block.currency + '\n' +
              'Issuer: ' + data.pubkey + '\n' +
              'Block: ' + block.number + '-' + block.hash + '\n' +
              'Membership: ' + (!!sideIn ? "IN" : "OUT" ) + '\n' +
              'UserID: ' + data.uid + '\n' +
              'CertTS: ' + data.blockUid + '\n';

            return CryptoUtils.sign(membership, keypair);
          })
          .then(function(signature) {
            var signedMembership = membership + signature + '\n';
            // Send signed membership
            return BMA.blockchain.membership({membership: signedMembership});
          })
          .then(function() {
            return $timeout(function() {
              return loadRequirements();
            }, 1000); // waiting for node to process membership doc
          })

          // Add wallet events
          .then(addEvents);
      };
    },

    /**
    * Send identity certification
    */
    certify = function(uid, pubkey, timestamp, signature, isMember, wasMember) {
      return $q.all([
          getKeypair(),
          csCurrency.get(),
          csCurrency.blockchain.current()
        ])
        .then(function(res) {
          var keypair = res[0];
          var currency = res[1];
          var block = res[2];
          // Create the self part to sign
          var cert = 'Version: '+ constants.CERT_VERSION +'\n' +
            'Type: Certification\n' +
            'Currency: ' + currency.name + '\n' +
            'Issuer: ' + data.pubkey + '\n' +
            'IdtyIssuer: ' + pubkey + '\n' +
            'IdtyUniqueID: ' + uid + '\n' +
            'IdtyTimestamp: ' + timestamp + '\n' +
            'IdtySignature: ' + signature + '\n' +
            'CertTimestamp: ' + block.number + '-' + block.hash + '\n';

          return CryptoUtils.sign(cert, keypair)
            .then(function(signature) {
              var signedCert = cert + signature + '\n';
              return BMA.wot.certify({cert: signedCert});
            })
            .then(function() {
              var cert = {
                pubkey: pubkey,
                uid: uid,
                time: block.medianTime,
                isMember: isMember,
                wasMember: wasMember,
                expiresIn: currency.parameters.sigWindow,
                pending: true,
                block: block.number,
                valid: true
              };

              // Notify extension
              api.action.raise.certify(cert);

              return cert;
            });
        });
    },

    addEvent = function(event, insertAtFirst) {
      event = event || {};
      event.type = event.type || 'info';
      event.message = event.message || '';
      event.messageParams = event.messageParams || {};
      event.context = event.context || 'undefined';
      if (event.message.trim().length) {
        if (!insertAtFirst) {
          data.events.push(event);
        }
        else {
          data.events.splice(0, 0, event);
        }
      }
      else {
        console.debug('Event without message. Skipping this event');
      }
    },

    getkeypairSaveId = function(record) {
        var nbCharSalt = Math.round(record.answer.length / 2);
        var salt = record.answer.substr(0, nbCharSalt);
        var pwd = record.answer.substr(nbCharSalt);
        return CryptoUtils.scryptKeypair(salt, pwd)
          .then(function (keypair) {
            record.pubkey = CryptoUtils.util.encode_base58(keypair.signPk);
            record.keypair = keypair;
            return record;
          });
      },

    getCryptedId = function(record){
      return getkeypairSaveId(record)
        .then(function() {
          return CryptoUtils.util.random_nonce();
        })
        .then(function(nonce) {
          record.nonce = nonce;
          return CryptoUtils.box.pack(record.salt, record.nonce, record.keypair.boxPk, record.keypair.boxSk);
        })
        .then(function (cypherSalt) {
          record.salt = cypherSalt;
          return CryptoUtils.box.pack(record.pwd, record.nonce, record.keypair.boxPk, record.keypair.boxSk);
        })
        .then(function (cypherPwd) {
          record.pwd = cypherPwd;
          record.nonce = CryptoUtils.util.encode_base58(record.nonce);
          return record;
        });
    },

    recoverId = function(recover) {
      var nonce = CryptoUtils.util.decode_base58(recover.cypherNonce);
      return getkeypairSaveId(recover)
        .then(function (recover) {
          return CryptoUtils.box.open(recover.cypherSalt, nonce, recover.keypair.boxPk, recover.keypair.boxSk);
        })
        .then(function (salt) {
          recover.salt = salt;
          return CryptoUtils.box.open(recover.cypherPwd, nonce, recover.keypair.boxPk, recover.keypair.boxSk);
        })
        .then(function (pwd) {
          recover.pwd = pwd;
          return recover;
        })
        .catch(function(err){
          console.warn('Incorrect answers - Unable to recover passwords');
        });
    },

    getSaveIDDocument = function(record) {
      var saveId = 'Version: 10 \n' +
        'Type: SaveID\n' +
        'Questions: ' + '\n' + record.questions +
        'Issuer: ' + data.pubkey + '\n' +
        'Crypted-Nonce: '+ record.nonce + '\n'+
        'Crypted-Pubkey: '+ record.pubkey +'\n' +
        'Crypted-Salt: '+ record.salt  + '\n' +
        'Crypted-Pwd: '+ record.pwd + '\n';

      // Sign SaveId document
      return CryptoUtils.sign(saveId, data.keypair)

        .then(function(signature) {
          saveId += signature + '\n';
          console.debug('Has generate an SaveID document:\n----\n' + saveId + '----');
          return saveId;
        });

    },

    downloadSaveId = function(record){
      return getSaveIDDocument(record)
        .then(function(saveId) {
          var saveIdFile = new Blob([saveId], {type: 'text/plain; charset=utf-8'});
          FileSaver.saveAs(saveIdFile, 'saveID.txt');
        });

    },

    getRevocationDocument = function() {
      return $q.all([
          getKeypair(),
          csCurrency.get()
        ])

        .then(function(res) {
          var keypair = res[0];
          var currency = res[1];
          // get the Identity document
          return getIdentityDocument(currency, keypair)

            // Create membership document (unsigned)
            .then(function(identity){
              var identityLines = identity.trim().split('\n');
              var idtySignature = identityLines[identityLines.length-1];

              var revocation = 'Version: '+ constants.REVOKE_VERSION +'\n' +
                'Type: Revocation\n' +
                'Currency: ' + currency.name + '\n' +
                'Issuer: ' + data.pubkey + '\n' +
                'IdtyUniqueID: ' + data.uid + '\n' +
                'IdtyTimestamp: ' + data.blockUid + '\n' +
                'IdtySignature: ' + idtySignature + '\n';


              // Sign revocation document
              return CryptoUtils.sign(revocation, keypair)

              // Add revocation to document
                .then(function(signature) {
                  revocation += signature + '\n';
                  console.debug('Has generate an revocation document:\n----\n' + revocation + '----');
                  return revocation;
                });
            });
        });
    },

    /**
     * Send a revocation
     */
    revoke = function() {

      // Clear old events
      cleanEventsByContext('revocation');

      // Get revocation document
      return getRevocationDocument()
        // Send revocation document
        .then(function(revocation) {
          return BMA.wot.revoke({revocation: revocation});
        })

        // Reload requirements
        .then(function() {

          return $timeout(function() {
            return loadRequirements();
          }, 1000); // waiting for node to process membership doc
        })

        // Add wallet events
        .then(addEvents)

        .catch(function(err) {
          if (err && err.ucode == BMA.errorCodes.REVOCATION_ALREADY_REGISTERED) {
            // Already registered by node: just add an event
            addEvent({type:'pending', message: 'INFO.REVOCATION_SENT_WAITING_PROCESS', context: 'requirements'}, true);
          }
          else {
            throw err;
          }
        })
        ;
    },

    revokeWithFile = function(revocation){
      return $q.all([
          BMA.wot.revoke({revocation: revocation})
        ])
        // Reload requirements
        .then(function(res) {
          if (isLogin()) {
            return $timeout(function () {
              return loadRequirements();
            }, 1000) // waiting for node to process membership doc

             // Add wallet events
            .then(addEvents)

            .catch(function (err) {
              if (err && err.ucode == BMA.errorCodes.REVOCATION_ALREADY_REGISTERED) {
                // Already registered by node: just add an event
                addEvent({type: 'pending', message: 'INFO.REVOCATION_SENT_WAITING_PROCESS', context: 'requirements'}, true);
              }
              else {
                throw err;
              }
            });
          }
          else {
            addEvent({type: 'pending', message: 'INFO.REVOCATION_SENT_WAITING_PROCESS', context: 'requirements'}, true);
          }
        });

    },

    downloadRevocation = function(){
        return $q.all([
            csCurrency.get(),
            getRevocationDocument()
          ])
          .then(function(res) {
            var currency = res[0];
            var revocation = res[1];
            var revocationFile = new Blob([revocation], {type: 'text/plain; charset=utf-8'});
            return $translate('ACCOUNT.SECURITY.REVOCATION_FILENAME', {
              uid: data.uid,
              currency: currency.name,
              pubkey: data.pubkey
            })
            .then(function (fileName) {
              FileSaver.saveAs(revocationFile, fileName);
            });
          });
      },

    cleanEventsByContext = function(context){
      data.events = data.events.reduce(function(res, event) {
        if (event.context && event.context == context) return res;
        return res.concat(event);
      },[]);
    },

    /**
    * De-serialize from JSON string
    */
    fromJson = function(json, failIfInvalid) {
      failIfInvalid = angular.isUndefined(failIfInvalid) ? true : failIfInvalid;
      return $q(function(resolve, reject) {
        var obj = JSON.parse(json || '{}');
        // FIXME #379
        /*if (obj && obj.pubkey) {
          resolve({
            pubkey: obj.pubkey
          });
        }
        else */
        if (obj && obj.keypair && obj.keypair.signPk && obj.keypair.signSk) {
          var keypair = {};
          var i;

          // sign Pk : Convert to Uint8Array type
          var signPk = new Uint8Array(32);
          for (i = 0; i < 32; i++) signPk[i] = obj.keypair.signPk[i];
          keypair.signPk = signPk;

          var signSk = new Uint8Array(64);
          for (i = 0; i < 64; i++) signSk[i] = obj.keypair.signSk[i];
          keypair.signSk = signSk;

          // box Pk : Convert to Uint8Array type
          if (obj.version && obj.keypair.boxPk) {
            var boxPk = new Uint8Array(32);
            for (i = 0; i < 32; i++) boxPk[i] = obj.keypair.boxPk[i];
            keypair.boxPk = boxPk;
          }

          if (obj.version && obj.keypair.boxSk) {
            var boxSk = new Uint8Array(32);
            for (i = 0; i < 64; i++) boxSk[i] = obj.keypair.boxSk[i];
            keypair.boxSk = boxSk;
          }

          resolve({
            pubkey: obj.pubkey,
            keypair: keypair,
            tx: obj.tx
          });
        }
        else if (failIfInvalid) {
          reject('Not a valid Wallet.data object');
        }
        else {
          resolve();
        }
      });
    },

    checkAuthIdle = function(isAuth) {
      isAuth = angular.isDefined(isAuth) ? isAuth : isAuth();
      var enable = isAuth && csSettings.data.keepAuthIdle > 0 && csSettings.data.keepAuthIdle != csSettings.constants.KEEP_AUTH_IDLE_SESSION;
      var changed = (enableAuthIdle != enable);

      // need start/top watching
      if (changed) {
        // start idle
        if (enable) {
          console.debug("[wallet] Start idle (delay: {0}s)".format(csSettings.data.keepAuthIdle));
          Idle.setIdle(csSettings.data.keepAuthIdle);
          Idle.watch();
        }
        // stop idle, if need
        else if (enableAuthIdle){
          console.debug("[wallet] Stop idle");
          Idle.unwatch();
        }
        enableAuthIdle = enable;
      }

      // if idle time changed: apply it
      else if (enable && Idle.getIdle() !== csSettings.data.keepAuthIdle) {
        console.debug("[idle] Updating auth idle (delay: {0}s)".format(csSettings.data.keepAuthIdle));
        Idle.setIdle(csSettings.data.keepAuthIdle);
      }
    };

    function addListeners() {
      listeners = [
        // Listen if settings changed
        csSettings.api.data.on.changed($rootScope, store, this),
        csSettings.api.data.on.changed($rootScope, checkAuthIdle, this),
        // Listen if node changed
        BMA.api.node.on.restart($rootScope, restart, this)
      ];

      $rootScope.$on('IdleStart', unauth);
    }

    function removeListeners() {
      _.forEach(listeners, function(remove){
        remove();
      });
      listeners = [];
    }

    function ready() {
      if (started) return $q.when();
      return startPromise || start();
    }

    function stop() {
      console.debug('[wallet] Stopping...');
      removeListeners();
      resetData();
    }

    function restart() {
      stop();
      return $timeout(start, 200);
    }

    function start(options) {
      options = options || {};
      // By default, restore if the service is the default object
      options.restore =  angular.isDefined(options.restore) ? options.restore : (id === 'default');

      console.debug('[wallet] Starting...');
      var now = new Date().getTime();

      startPromise = $q.all([
          csSettings.ready(),
          csCurrency.ready(),
          BMA.ready()
        ]);

      // Restore
      if (options.restore) startPromise = startPromise.then(restore);

      // Emit ready event
      startPromise.then(function() {
          addListeners();

          console.debug('[wallet] Started in ' + (new Date().getTime() - now) + 'ms');

          started = true;
          startPromise = null;
        })
        .then(function(){
          return data;
        });

      return startPromise;
    }

    // Register extension points
    api.registerEvent('data', 'init');
    api.registerEvent('data', 'loginCheck'); // allow to stop the login process
    api.registerEvent('data', 'login'); // executed after login check (cannot stop the login process)
    api.registerEvent('data', 'auth');
    api.registerEvent('data', 'unauth');
    api.registerEvent('data', 'load');
    api.registerEvent('data', 'logout');
    api.registerEvent('data', 'reset');

    api.registerEvent('error', 'send');

    // Data changed : balance changed, new TX
    api.registerEvent('data', 'balanceChanged');
    api.registerEvent('data', 'newTx');

    api.registerEvent('action', 'certify');


    // init data
    resetData(true);

    return {
      id: id,
      data: data,
      ready: ready,
      start: start,
      stop: stop,
      // auth
      login: login,
      logout: logout,
      auth: auth,
      unauth: unauth,
      isLogin: isLogin,
      isAuth: isAuth,
      getKeypair: getKeypair,
      hasSelf: hasSelf,
      setSelf: setSelf,
      isMember: function() {
        return data.isMember;
      },
      isDataLoaded : isDataLoaded,
      isNeverUsed: isNeverUsed,
      isNew: isNew,
      isUserPubkey: isUserPubkey,
      getData: getData,
      loadData: loadData,
      refreshData: refreshData,
      // operations
      transfer: transfer,
      self: self,
      revoke: revoke,
      revokeWithFile: revokeWithFile,
      certify: certify,
      downloadSaveId: downloadSaveId,
      getCryptedId: getCryptedId,
      recoverId: recoverId,
      downloadRevocation: downloadRevocation,
      membership: {
        inside: membership(true),
        out: membership(false)
      },
      events: {
        add: addEvent,
        cleanByContext: cleanEventsByContext
      },
      api: api
    };
  }

  var service = factory('default', BMA);
  service.instance = factory;

  return service;
}]);



angular.module('cesium.plugin.services', [])

.provider('PluginService', function PluginServiceProvider() {
  'ngInject';

  var eagerLoadingServices = [];

  var extensionByStates = {};

  this.registerEagerLoadingService = function(serviceName) {
    eagerLoadingServices.push(serviceName);
    return this;
  };

  this.extendState = function(stateName, extension) {
    if (angular.isDefined(stateName) && angular.isDefined(extension)) {
      if (!extensionByStates[stateName]) {
        extensionByStates[stateName] = [];
      }
      extensionByStates[stateName].push(extension);
    }
    return this;
  };

  this.extendStates = function(stateNames, extension) {
    var that = this;
    stateNames.forEach(function(stateName) {
      that.extendState(stateName, extension);
    });
    return this;
  };

  this.$get = ['$injector', '$state', function($injector, $state) {

    var currentExtensionPointName;

    function start() {
      if (eagerLoadingServices.length>0) {
        _.forEach(eagerLoadingServices, function(name) {
          $injector.get(name);
        });
      }
    }

    function getActiveExtensionPointsByName(extensionPointName) {
      var extensions = _.keys(extensionByStates).reduce(function(res, stateName){
        return $state.includes(stateName) ? res.concat(extensionByStates[stateName]) : res;
      }, []);
      return extensions.reduce(function(res, extension){
        return extension.points && extension.points[extensionPointName] ? res.concat(extension.points[extensionPointName]) : res;
      }, []);
    }

    function setCurrentExtensionPointName(extensionPointName) {
      currentExtensionPointName  = extensionPointName;
    }

    function getCurrentExtensionPointName() {
      return currentExtensionPointName;
    }

    return {
      start: start,
      extensions: {
        points: {
          getActivesByName: getActiveExtensionPointsByName,
          current: {
            get: getCurrentExtensionPointName,
            set: setCurrentExtensionPointName
          }
        }
      }
    };
  }];
})
;

angular.module('cesium.help.services', [])

.constant('csHelpConstants', {
  wallet: {
    stepCount: 4
  }
})

.factory('csHelp', ['$rootScope', 'csSettings', 'UIUtils', 'csHelpConstants', '$controller', function($rootScope, csSettings, UIUtils, csHelpConstants, $controller) {
  'ngInject';


  function createHelptipScope(isTour, helpController) {
    if (!isTour && ($rootScope.tour || !csSettings.data.helptip.enable || UIUtils.screen.isSmall())) {
      return; // avoid other helptip to be launched (e.g. csWallet)
    }
    // Create a new scope for the tour controller
    var helptipScope = $rootScope.$new();
    $controller(helpController||'HelpTipCtrl', { '$scope': helptipScope});
    return helptipScope;
  }

  function startWalletHelpTip(index, isTour) {
    index = angular.isDefined(index) ? index : csSettings.data.helptip.wallet;
    isTour = angular.isDefined(isTour) ? isTour : false;

    if (index < 0 || index >= csHelpConstants.wallet.stepCount) return;

    // Create a new scope for the tour controller
    var helptipScope = createHelptipScope(isTour);
    if (!helptipScope) return; // could be undefined, if a global tour already is already started

    helptipScope.tour = isTour;

    return helptipScope.startWalletTour(index, false)
      .then(function(endIndex) {
        helptipScope.$destroy();
        if (!isTour) {
          csSettings.data.helptip.wallet = endIndex;
          csSettings.store();
        }
      });
  }

  return {
    wallet: {
      tour: function() {
        return startWalletHelpTip(0, true);
      },
      helptip: startWalletHelpTip
    }
  };

}]);

angular.module('cesium.services', [
  'cesium.settings.services',
  'cesium.http.services',
  'cesium.network.services',
  'cesium.bma.services',
  'cesium.crypto.services',
  'cesium.utils.services',
  'cesium.modal.services',
  'cesium.storage.services',
  'cesium.device.services',
  'cesium.currency.services',
  'cesium.wot.services',
  'cesium.tx.services',
  'cesium.wallet.services',
  'cesium.help.services',
  'cesium.plugin.services'
  ])
;

angular.module('cesium.api.demo.services', ['cesium.bma.services', 'cesium.crypto.services', 'cesium.utils.services', 'cesium.settings.services'])


  .factory('csDemoWallet', ['$rootScope', '$timeout', '$controller', '$state', '$q', '$translate', '$filter', 'BMA', 'CryptoUtils', function($rootScope, $timeout, $controller, $state, $q, $translate, $filter,
                                    BMA, CryptoUtils) {
    'ngInject';

    function factory(authData) {

      var demoPubkey;

      return {
        start: function() {
          return $q.when();
        },
        login: function() {
          var self = this;
          return $translate('API.TRANSFER.DEMO.PUBKEY')
            .then(function(pubkey) {
              demoPubkey = pubkey;
              if (!authData || authData.pubkey != demoPubkey) {
                throw {message: 'API.TRANSFER.DEMO.BAD_CREDENTIALS'};
              }
              self.data = {
                keypair: authData.keypair
              };
              return {
                uid: 'Demo',
                pubkey: demoPubkey
              };
            });
        },
        transfer: function(pubkey, amount, comment) {
          var self = this;
          return BMA.blockchain.current()
            .then(function(block) {
              var tx = 'Version: '+ BMA.constants.PROTOCOL_VERSION +'\n' +
                'Type: Transaction\n' +
                'Currency: ' + block.currency + '\n' +
                'Blockstamp: ' + block.number + '-' + block.hash + '\n' +
                'Locktime: 0\n' + // no lock
                'Issuers:\n' +
                demoPubkey + '\n' +
                'Inputs:\n' +
                [amount, block.unitbase, 'T', 'FakeId27jQMAf3jqL2fr75ckZ6Jgi9TZL9fMf9TR9vBvG', 0].join(':')+ '\n' +
                'Unlocks:\n' +
                '0:SIG(0)\n' +
                'Outputs:\n' +
                [amount, block.unitbase, 'SIG(' + pubkey + ')'].join(':')+'\n' +
                'Comment: '+ (comment||'') + '\n';

              return CryptoUtils.sign(tx, self.data.keypair)
                .then(function(signature) {
                  var signedTx = tx + signature + "\n";
                  return CryptoUtils.util.hash(signedTx)
                    .then(function(txHash) {
                      return $q.when({
                        tx: signedTx,
                        hash: txHash
                      });
                    });
                });
            });
        }
      };
    }

    return {
      instance: factory
    };
  }])
;



function Peer(json) {

  var that = this;

  Object.keys(json).forEach(function (key) {
    that[key] = json[key];
  });

  that.endpoints = that.endpoints || [];
  that.statusTS = that.statusTS || 0;
}


Peer.prototype.regexp = {
  BMA: /^BASIC_MERKLED_API[ ]?/,
  BMAS: /^BMAS[ ]?/,
  WS2P: /^WS2P[ ]?/,
  BMA_REGEXP: /^BASIC_MERKLED_API([ ]+([a-z_][a-z0-9-_.ğĞ]*))?([ ]+([0-9.]+))?([ ]+([0-9a-f:]+))?([ ]+([0-9]+))$/,
  BMAS_REGEXP: /^BMAS([ ]+([a-z_][a-z0-9-_.ğĞ]*))?([ ]+([0-9.]+))?([ ]+([0-9a-f:]+))?([ ]+([0-9]+))$/,
  WS2P_REGEXP: /^WS2P[ ]+([a-z0-9]+)([ ]+([a-z_][a-z0-9-_.ğĞ]*))?([ ]+([0-9.]+))?([ ]+([0-9a-f:]+))?([ ]+([0-9]+))([ ]+([a-z0-9/.&#!]+))?$/,
  LOCAL_IP_ADDRESS: /^127[.]0[.]0.|192[.]168[.]|10[.]0[.]0[.]|172[.]16[.]/
};
Peer.prototype.regex = Peer.prototype.regexp; // for backward compat

Peer.prototype.keyID = function () {
  var bma = this.bma || this.getBMA();
  if (bma.useBma) {
    return [this.pubkey || "Unknown", bma.dns, bma.ipv4, bma.ipv6, bma.port, bma.useSsl, bma.path].join('-');
  }
  return [this.pubkey || "Unknown", bma.ws2pid, bma.path].join('-');
};

Peer.prototype.copyValues = function(to) {
  var obj = this;
  ["version", "currency", "pub", "endpoints", "hash", "status", "statusTS", "block", "signature"].forEach(function (key) {
    to[key] = obj[key];
  });
};

Peer.prototype.copyValuesFrom = function(from) {
  var obj = this;
  ["version", "currency", "pub", "endpoints", "block", "signature"].forEach(function (key) {
    obj[key] = from[key];
  });
};

Peer.prototype.json = function() {
  var obj = this;
  var json = {};
  ["version", "currency", "endpoints", "status", "block", "signature"].forEach(function (key) {
    json[key] = obj[key];
  });
  json.raw = this.raw && this.getRaw();
  json.pubkey = this.pubkey;
  return json;
};

Peer.prototype.getBMA = function() {
  if (this.bma) return this.bma;
  var bma = null;
  var bmaRegex = this.regex.BMA_REGEXP;
  this.endpoints.forEach(function(ep){
    var matches = !bma && bmaRegex.exec(ep);
    if (matches) {
      bma = {
        "dns": matches[2] || '',
        "ipv4": matches[4] || '',
        "ipv6": matches[6] || '',
        "port": matches[8] || 80
      };
    }
  });
  return bma || {};
};

Peer.prototype.getEndpoints = function(regex) {
  if (!regex) return this.endpoints;
    return this.endpoints.reduce(function(res, ep){
      return ep.match(regex) ?  res.concat(ep) : res;
    }, []);
};

Peer.prototype.hasEndpoint = function(endpoint){
  //console.debug('testing if hasEndpoint:' + endpoint);
  var regExp = this.regexp[endpoint] || new RegExp('^' + endpoint);
  var endpoints = this.getEndpoints(regExp);
  if (!endpoints.length) return false;
  else return true;

};

Peer.prototype.getDns = function() {
  var bma = this.bma || this.getBMA();
  return bma.dns ? bma.dns : null;
};

Peer.prototype.getIPv4 = function() {
  var bma = this.bma || this.getBMA();
  return bma.ipv4 ? bma.ipv4 : null;
};

Peer.prototype.getIPv6 = function() {
  var bma = this.bma || this.getBMA();
  return bma.ipv6 ? bma.ipv6 : null;
};

Peer.prototype.getPort = function() {
  var bma = this.bma || this.getBMA();
  return bma.port ? bma.port : null;
};

Peer.prototype.getHost = function() {
  var bma = this.bma || this.getBMA();
  return ((bma.port == 443 || bma.useSsl) && bma.dns) ? bma.dns :
    (this.hasValid4(bma) ? bma.ipv4 :
        (bma.dns ? bma.dns :
          (bma.ipv6 ? '[' + bma.ipv6 + ']' :'')));
};

Peer.prototype.getURL = function() {
  var bma = this.bma || this.getBMA();
  var host = this.getHost();
  var protocol = (bma.port == 443 || bma.useSsl) ? 'https' : 'http';
  return protocol + '://' + host + (bma.port ? (':' + bma.port) : '');
};

Peer.prototype.getServer = function() {
  var bma = this.bma || this.getBMA();
  var host = this.getHost();
  return host + (host && bma.port ? (':' + bma.port) : '');
};

Peer.prototype.hasValid4 = function(bma) {
  return bma.ipv4 &&
    /* exclude private address - see https://fr.wikipedia.org/wiki/Adresse_IP */
    !bma.ipv4.match(this.regexp.LOCAL_IP_ADDRESS) ?
    true : false;
};

Peer.prototype.isReachable = function () {
  return !!this.getServer();
};

Peer.prototype.isSsl = function() {
  var bma = this.bma || this.getBMA();
  return bma.useSsl;
};

Peer.prototype.isTor = function() {
  var bma = this.bma || this.getBMA();
  return bma.useTor;
};


Peer.prototype.isWs2p = function() {
  var bma = this.bma || this.getBMA();
  return bma.useWs2p;
};

/**
 * Created by blavenie on 01/02/17.
 */
function Block(json, attributes) {
  "use strict";

  var that = this;

  // Copy default fields
  if (!attributes || !attributes.length) {
    ["currency", "issuer", "medianTime", "number", "version", "powMin", "dividend", "membersCount", "hash", "identities", "joiners", "actives", "leavers", "revoked", "excluded", "certifications", "transactions", "unitbase"]
      .forEach(function (key) {
      that[key] = json[key];
    });
  }
  // or just given
  else {
    _.forEach(attributes, function (key) {
      that[key] = json[key];
    });

  }

  that.identitiesCount = that.identities ? that.identities.length : 0;
  that.joinersCount = that.joiners ? that.joiners.length : 0;
  that.activesCount = that.actives ? that.actives.length : 0;
  that.leaversCount = that.leavers ? that.leavers.length : 0;
  that.revokedCount = that.revoked ? that.revoked.length : 0;
  that.excludedCount = that.excluded ? that.excluded.length : 0;
  that.certificationsCount = that.certifications ? that.certifications.length : 0;
  that.transactionsCount = that.transactions ? that.transactions.length : 0;

  that.empty = that.isEmpty();
}

Block.prototype.isEmpty = function(){
  "use strict";
  return !this.transactionsCount &&
    !this.certificationsCount &&
    !this.joinersCount &&
    !this.dividend &&
    !this.activesCount &&
    !this.identitiesCount &&
    !this.leaversCount &&
    !this.excludedCount &&
    !this.revokedCount;
};

Block.prototype.parseData = function() {
  this.identities = this.parseArrayValues(this.identities, ['pubkey', 'signature', 'buid', 'uid']);
  this.joiners = this.parseArrayValues(this.joiners, ['pubkey', 'signature', 'mBuid', 'iBuid', 'uid']);
  this.actives = this.parseArrayValues(this.actives, ['pubkey', 'signature', 'mBuid', 'iBuid', 'uid']);
  this.leavers = this.parseArrayValues(this.leavers, ['pubkey', 'signature', 'mBuid', 'iBuid', 'uid']);
  this.revoked = this.parseArrayValues(this.revoked, ['pubkey', 'signature']);
  this.excluded = this.parseArrayValues(this.excluded, ['pubkey']);

  // certifications
  this.certifications = this.parseArrayValues(this.certifications, ['from', 'to', 'block', 'signature']);
  //this.certifications = _.groupBy(this.certifications, 'to');

  // TX
  this.transactions = this.parseTransactions(this.transactions);

  delete this.raw; // not need
};

Block.prototype.cleanData = function() {
  delete this.identities;
  delete this.joiners;
  delete this.actives;
  delete this.leavers;
  delete this.revoked;
  delete this.excluded;
  delete this.certifications;
  delete this.transactions;

  delete this.raw; // not need
};

Block.prototype.parseArrayValues = function(array, itemObjProperties){
  if (!array || !array.length) return [];
  return array.reduce(function(res, raw) {
    var parts = raw.split(':');
    if (parts.length != itemObjProperties.length) {
      console.debug('[block] Bad format for \'{0}\': [{1}]. Expected {1} parts. Skipping'.format(arrayProperty, raw, itemObjProperties.length));
      return res;
    }
    var obj = {};
    for (var i=0; i<itemObjProperties.length; i++) {
      obj[itemObjProperties[i]] = parts[i];
    }
    return res.concat(obj);
  }, []);
};

function exact(regexpContent) {
  return new RegExp("^" + regexpContent + "$");
}

Block.prototype.regexp = {
  TX_OUTPUT_SIG: exact("SIG\\(([0-9a-zA-Z]{43,44})\\)")
};

Block.prototype.parseTransactions = function(transactions) {
  if (!transactions || !transactions.length) return [];
  return transactions.reduce(function (res, tx) {
    var obj = {
      issuers: tx.issuers,
      time: tx.time
    };

    obj.outputs = tx.outputs.reduce(function(res, output) {
      var parts = output.split(':');
      if (parts.length != 3) {
        console.debug('[block] Bad format a \'transactions\': [{0}]. Expected 3 parts. Skipping'.format(output));
        return res;
      }

      var amount = parts[0];
      var unitbase = parts[1];
      var unlockCondition = parts[2];

      var matches =  Block.prototype.regexp.TX_OUTPUT_SIG.exec(parts[2]);

      // Simple expression SIG(x)
      if (matches) {
        var pubkey = matches[1];
        if (!tx.issuers || tx.issuers.indexOf(pubkey) != -1) return res;
        return res.concat({
          amount: unitbase <= 0 ? amount : amount * Math.pow(10, unitbase),
          unitbase: unitbase,
          pubkey: pubkey
        });
      }

      // Parse complex unlock condition
      else {
        //console.debug('[block] [TX] Detecting unlock condition: {0}.'.format(output));
        return res.concat({
          amount: unitbase <= 0 ? amount : amount * Math.pow(10, unitbase),
          unitbase: unitbase,
          unlockCondition: unlockCondition
        });
      }
    }, []);

    // Special cas for TX to himself
    if (!obj.error && !obj.outputs.length) {
      obj.toHimself = true;
    }

    return res.concat(obj);
  }, []);
};


LoginModalController.$inject = ['$scope', '$timeout', '$q', '$ionicPopover', 'CryptoUtils', 'UIUtils', 'BMA', 'Modals', 'csSettings', 'Device', 'parameters'];
AuthController.$inject = ['$scope', '$controller'];
angular.module('cesium.login.controllers', ['cesium.services'])

  .controller('LoginModalCtrl', LoginModalController)

  .controller('AuthCtrl', AuthController)

;

function LoginModalController($scope, $timeout, $q, $ionicPopover, CryptoUtils, UIUtils, BMA, Modals, csSettings, Device, parameters) {
  'ngInject';

  parameters = parameters || {};

  $scope.computing = false;
  $scope.pubkey = null;
  $scope.formData = {};
  $scope.showPubkey = false;
  $scope.showComputePubkeyButton = false;
  $scope.autoComputePubkey = false;
  $scope.pubkeyPattern = '^' + BMA.constants.regexp.PUBKEY + '$';

  $scope.isAuth = parameters.auth;
  $scope.showMethods = angular.isDefined(parameters.showMethods) ? parameters.showMethods : true;
  $scope.expectedPubkey = parameters.expectedPubkey;

  $scope.scryptParamsValues = _.keys(CryptoUtils.constants.SCRYPT_PARAMS)
    .reduce(function(res, key) {
      return res.concat({id: key, label: 'LOGIN.SCRYPT.' + key, params: CryptoUtils.constants.SCRYPT_PARAMS[key]});
    }, [{id: 'USER', label: 'LOGIN.SCRYPT.USER', params: {}}]);

  // modal init
  $scope.init = function() {
    // Should auto-compute pubkey ?
    $scope.autoComputePubkey = ionic.Platform.grade.toLowerCase()==='a' &&
      !UIUtils.screen.isSmall();

    // Init remember me
    $scope.formData.rememberMe = csSettings.data.rememberMe;

    // Init keep auth, from idle time
    $scope.formData.keepAuthIdle = csSettings.data.keepAuthIdle;
    $scope.formData.keepAuth = ($scope.formData.keepAuthIdle == csSettings.constants.KEEP_AUTH_IDLE_SESSION);

    // Init method
    var method = parameters.method || csSettings.data.login && csSettings.data.login.method || 'SCRYPT_DEFAULT';
    var params = csSettings.data.login && csSettings.data.login.params;
    if ($scope.isAuth && method == 'PUBKEY') {
      method = 'SCRYPT_DEFAULT'; // PUBKEY not enable if auth need
    }
    $scope.changeMethod(method, params);
  };

  // modal enter
  $scope.enter = function() {
    UIUtils.loading.hide();
    // Ink effect
    UIUtils.ink({selector: '.modal-login .ink'});
  };
  $scope.$on('modal.shown', $scope.enter);

  // modal leave
  $scope.leave = function() {
    $scope.formData = {};
    $scope.computing = false;
    $scope.pubkey = null;
    $scope.methods = [];
  };
  $scope.$on('modal.hide', $scope.leave);

  // Login form submit
  $scope.doLogin = function() {
    if(!$scope.form.$valid) {
      return;
    }
    var method = $scope.formData.method;
    var keepAuthIdle = $scope.formData.keepAuthIdle;
    var promise;

    // Scrypt
    if (method === 'SCRYPT_DEFAULT' || method === 'SCRYPT_ADVANCED') {
      if (!$scope.formData.username || !$scope.formData.password) return;
      var scryptPrams = $scope.formData.scrypt && $scope.formData.scrypt.params;
      UIUtils.loading.show();
      promise = CryptoUtils.scryptKeypair($scope.formData.username, $scope.formData.password, scryptPrams)
        .then(function(keypair) {
          if (!keypair) return UIUtils.loading.hide(10);
          var pubkey = CryptoUtils.util.encode_base58(keypair.signPk);
          // Check pubkey
          if (parameters.expectedPubkey && parameters.expectedPubkey != pubkey) {
            $scope.pubkey = pubkey;
            $scope.showPubkey = true;
            $scope.pubkeyError = true;
            return UIUtils.loading.hide(10);
          }

          $scope.pubkeyError = false;

          return {
            pubkey: pubkey,
            keypair: keypair,
            params: ($scope.formData.scrypt && $scope.formData.scrypt.id != 'SCRYPT_DEFAULT') ? scryptPrams : undefined
          };
        })
        .catch(UIUtils.onError('ERROR.CRYPTO_UNKNOWN_ERROR'));
    }

    // File
    else if (method === 'FILE') {
      if (!$scope.formData.file || !$scope.formData.file.valid || !$scope.formData.file.pubkey) return;

      // If checkbox keep auth checked: set idle time to session
      keepAuthIdle = ($scope.formData.keepAuth && csSettings.constants.KEEP_AUTH_IDLE_SESSION) || keepAuthIdle;

        UIUtils.loading.show();
      promise = CryptoUtils.readKeyFile($scope.formData.file, $scope.isAuth||$scope.formData.keepAuth/*withSecret*/)
        .then(function(keypair) {
          if (!keypair) return UIUtils.loading.hide(10);
          var pubkey = CryptoUtils.util.encode_base58(keypair.signPk);

          // Check pubkey
          if (parameters.expectedPubkey && parameters.expectedPubkey != pubkey) {
            $scope.formData.file.valid = false;
            return UIUtils.loading.hide(10);
          }

          $scope.pubkeyError = false;

          return {
            pubkey: pubkey,
            keypair: keypair
          };
        })
        .catch(UIUtils.onError('ERROR.AUTH_FILE_ERROR'));
    }

    // Pubkey
    else if (method === 'PUBKEY') {
      if (!$scope.formData.pubkey) return;
      promise = $q.when({
        pubkey: $scope.formData.pubkey
      });
    }

    if (!promise) {
      console.warn('[login] unknown method: ', method);
      return;
    }

    return promise.then(function(res) {
      if (!res) return;

      // Update settings (if need)
      var rememberMeChanged = !angular.equals(csSettings.data.rememberMe, $scope.formData.rememberMe);
      var keepAuthIdleChanged = !angular.equals(csSettings.data.keepAuthIdle, keepAuthIdle);
      var methodChanged = !angular.equals(csSettings.data.login && csSettings.data.login.method, method);
      var paramsChanged = !angular.equals(csSettings.data.login && csSettings.data.login.params, res.params);
      if (rememberMeChanged || keepAuthIdleChanged || methodChanged || paramsChanged) {
        csSettings.data.rememberMe = $scope.formData.rememberMe;
        csSettings.data.keepAuthIdle = keepAuthIdle;
        csSettings.data.useLocalStorage = csSettings.data.rememberMe ? true : csSettings.data.useLocalStorage;
        csSettings.data.login = csSettings.data.login || {};
        csSettings.data.login.method = method;
        csSettings.data.login.params = res.params;
        $timeout(csSettings.store, 500);
      }

      if (parameters.success) {
        parameters.success($scope.formData);
      }

      // hide loading
      if (parameters.silent) {
        UIUtils.loading.hide();
      }

      // Return result then close
      return $scope.closeModal(res);
    });
  };

  $scope.onScryptFormChanged = function() {
    if ($scope.computing) return; // avoid multiple call
    $scope.pubkey = null;
    $scope.pubkeyError = false;
    $scope.showPubkey = !!$scope.formData.username && !!$scope.formData.password;
    if ($scope.autoComputePubkey && $scope.showPubkey) {
      $scope.computePubkey();
      $scope.showComputePubkeyButton = false;
    }
    else {
      $scope.showComputePubkeyButton = !$scope.autoComputePubkey && $scope.showPubkey;
    }
  };
  $scope.$watch('formData.username + formData.password', $scope.onScryptFormChanged, true);

  $scope.computePubkey = function() {
    $scope.showComputePubkeyButton = false;
    $scope.computing = true;
    $scope.pubkey = null;
    return $timeout(function() {
      var salt = $scope.formData.username;
      var pwd = $scope.formData.password;
      var scryptPrams = $scope.formData.scrypt && $scope.formData.scrypt.params;
      return CryptoUtils.scryptSignPk(salt, pwd, scryptPrams)
        .then(function (signPk) {

          // If model has changed before the response, then retry
          if (salt !== $scope.formData.username || pwd !== $scope.formData.password) {
            return $scope.computePubkey();
          }

          $scope.pubkey = CryptoUtils.util.encode_base58(signPk);
          if ($scope.expectedPubkey && $scope.expectedPubkey != $scope.pubkey) {
            $scope.pubkeyError = true;
          }

          $scope.computing = false;
        }
      )
      .catch(function (err) {
        UIUtils.onError('ERROR.CRYPTO_UNKNOWN_ERROR')(err);
        $scope.computing = false;
        $scope.onScryptFormChanged();
      });
    }, 100);
  };

  $scope.showJoinModal = function() {
    $scope.closeModal();
    $timeout(function() {
      Modals.showJoin();
    }, 300);
  };

  $scope.showAccountSecurityModal = function() {
    $scope.closeModal();
    $timeout(function() {
      Modals.showAccountSecurity();
    }, 300);
  };

  $scope.showHelpModal = function(parameters) {
    return Modals.showHelp(parameters);
  };

  $scope.changeMethod = function(method, params){
    $scope.hideMethodsPopover();
    if (method == $scope.formData.method) return; // same method

    console.debug("[login] method is: " + method);
    $scope.formData.method = method;

    if ($scope.form) {
      // hide form's fields errors on the form
      delete $scope.form.$submitted;
    }

    // Scrypt (advanced or not)
    if (method == 'SCRYPT_DEFAULT' || method == 'SCRYPT_ADVANCED') {
      // Search scrypt object
      var scrypt;
      if (params) {
        scrypt = _.find($scope.scryptParamsValues, function(item){
            return item.params && angular.equals(item.params, params);
          });
        if (!scrypt) {
          scrypt = _.findWhere($scope.scryptParamsValues, {id: 'USER'}) || {};
          scrypt.params = params;
        }
      }
      else {
        scrypt = _.findWhere($scope.scryptParamsValues, {id: 'DEFAULT'});
      }
      $scope.changeScrypt(scrypt);

      $scope.autoComputePubkey = $scope.autoComputePubkey && (method == 'SCRYPT_DEFAULT');
    }
    else {
      $scope.formData.username = null;
      $scope.formData.password = null;
      $scope.formData.pubkey = null;
      $scope.formData.computing = false;
    }
  };

  $scope.changeScrypt = function(scrypt) {
    // Protect params against changes
    $scope.formData.scrypt = angular.copy(scrypt||{});
    $scope.onScryptFormChanged();
  };

  $scope.fileChanged = function(event) {
    $scope.validatingFile = true;
    $scope.formData.file = event && event.target && event.target.files && event.target.files.length && event.target.files[0];
    if (!$scope.formData.file) {
      $scope.validatingFile = false;
      return;
    }

    $timeout(function() {
      console.debug("[login] key file changed: ", $scope.formData.file);
      $scope.validatingFile = true;

      return CryptoUtils.readKeyFile($scope.formData.file, false/*withSecret*/)
        .then(function(keypair) {
          if (!keypair || !keypair.signPk) {
            $scope.formData.file.valid = false;
            $scope.formData.file.pubkey = undefined;
          }
          else {
            $scope.formData.file.pubkey = CryptoUtils.util.encode_base58(keypair.signPk);
            $scope.formData.file.valid = !$scope.expectedPubkey || $scope.expectedPubkey == $scope.formData.file.pubkey;
            $scope.validatingFile = false;
          }

        })
        .catch(function(err) {
          $scope.validatingFile = false;
          $scope.formData.file.valid = false;
          $scope.formData.file.pubkey = undefined;
          UIUtils.onError('ERROR.AUTH_FILE_ERROR')(err);
        });
    });
  };

  /**
   * Recover Id
   */

  $scope.onKeyFileDrop = function(file) {
    if (!file || !file.fileData) return;

    $scope.formData.file = {
      name: file.fileData.name,
      size: file.fileData.size,
      content: file.fileContent
    };
    return CryptoUtils.readKeyFile($scope.formData.file, false/*withSecret*/)
      .then(function(keypair) {
        if (!keypair || !keypair.signPk) {
          $scope.formData.file.valid = false;
          $scope.formData.file.pubkey = undefined;
        }
        else {
          $scope.formData.file.pubkey = CryptoUtils.util.encode_base58(keypair.signPk);
          $scope.formData.file.valid = !$scope.expectedPubkey || $scope.expectedPubkey == $scope.formData.file.pubkey;
          $scope.validatingFile = false;
        }

      })
      .catch(function(err) {
        $scope.validatingFile = false;
        $scope.formData.file.valid = false;
        $scope.formData.file.pubkey = undefined;
        UIUtils.onError('ERROR.AUTH_FILE_ERROR')(err);
      });
  };

  $scope.removeKeyFile = function() {
    $scope.formData.file = undefined;
  };

  /* -- modals -- */

  $scope.showWotLookupModal = function() {
    return Modals.showWotLookup()
      .then(function(res){
        if (res && res.pubkey) {
          $scope.formData.pubkey = res.pubkey;
        }
      });
  };

  /* -- popover -- */

  $scope.showMethodsPopover = function(event) {
    if (event.defaultPrevented) return;
    if (!$scope.methodsPopover) {

      $ionicPopover.fromTemplateUrl('templates/login/popover_methods.html', {
        scope: $scope
      }).then(function(popover) {
        $scope.methodsPopover = popover;
        //Cleanup the popover when we're done with it!
        $scope.$on('$destroy', function() {
          $scope.methodsPopover.remove();
        });
        $scope.methodsPopover.show(event)
          .then(function() {
            UIUtils.ink({selector: '.popover-login-methods .item'});
          });
      });
    }
    else {
      $scope.methodsPopover.show(event);
    }
  };

  $scope.hideMethodsPopover = function() {
    if ($scope.methodsPopover) {
      $scope.methodsPopover.hide();
    }
  };



  // Default action
  $scope.init();


  // TODO : for DEV only
  /*$timeout(function() {
    $scope.formData = {
      username: 'benoit.lavenier@e-is.pro',
      password: ''
    };
    //$scope.form = {$valid:true};
  }, 900);*/
}


function AuthController($scope, $controller){

  // Initialize the super class and extend it.
  angular.extend(this, $controller('LoginModalCtrl', {$scope: $scope, parameters: {auth: true}}));

  $scope.setForm = function(form) {
    $scope.form = form;
  };

}


HelpController.$inject = ['$scope', '$state', '$timeout', '$anchorScroll', 'csSettings'];
HelpModalController.$inject = ['$scope', '$timeout', '$anchorScroll', 'csSettings', 'parameters'];
HelpTipController.$inject = ['$scope', '$state', '$window', '$ionicSideMenuDelegate', '$timeout', '$q', '$anchorScroll', 'UIUtils', 'csConfig', 'csSettings', 'csCurrency', 'csHelpConstants', 'Device', 'csWallet'];
HelpTourController.$inject = ['$scope'];
angular.module('cesium.help.controllers', ['cesium.services'])

  .config(['$stateProvider', function($stateProvider) {
    'ngInject';

    $stateProvider


      .state('app.help_tour', {
        url: "/tour",
        views: {
          'menuContent': {
            templateUrl: "templates/home/home.html",
            controller: 'HelpTourCtrl'
          }
        }
      })

      .state('app.help', {
        url: "/help?anchor",
        views: {
          'menuContent': {
            templateUrl: "templates/help/view_help.html",
            controller: 'HelpCtrl'
          }
        }
      })

      .state('app.help_anchor', {
        url: "/help/:anchor",
        views: {
          'menuContent': {
            templateUrl: "templates/help/view_help.html",
            controller: 'HelpCtrl'
          }
        }
      })

    ;


  }])

  .controller('HelpCtrl', HelpController)

  .controller('HelpModalCtrl', HelpModalController)

  .controller('HelpTipCtrl', HelpTipController)

  .controller('HelpTourCtrl', HelpTourController)


;


function HelpController($scope, $state, $timeout, $anchorScroll, csSettings) {
  'ngInject';

  $scope.$on('$ionicView.enter', function(e) {
    $scope.locale = csSettings.data.locale.id;
    if ($state.stateParams && $state.stateParams.anchor) {
      $scope.anchor = $state.stateParams.anchor;
      $timeout(function () {
        $anchorScroll($state.stateParams.anchor);
      }, 100);
    }
  });
}

function HelpModalController($scope, $timeout, $anchorScroll, csSettings, parameters) {
  'ngInject';

  $scope.itemsClass = {};
  $scope.locale = csSettings.data.locale.id;

  parameters = parameters || {};
  if (parameters && typeof parameters == "string") {
    parameters = {anchor: parameters};
  }

  if (parameters.anchor) {

    $timeout(function() {
      $anchorScroll(parameters.anchor);
    }, 100);

    // Change CSS classes
    $scope.itemsClass = {};
    $scope.itemsClass[parameters.anchor] = 'selected';
    $scope.listClass = 'selection';
  }

}


/* ----------------------------
*  Help Tip
* ---------------------------- */
function HelpTipController($scope, $state, $window, $ionicSideMenuDelegate, $timeout, $q, $anchorScroll,
                           UIUtils, csConfig, csSettings, csCurrency, csHelpConstants, Device, csWallet) {

  $scope.tour = false; // Is a tour or a helptip ?
  $scope.continue = true;

  $scope.executeStep = function(partName, steps, index) {
    index = angular.isDefined(index) ? index : 0;

    if (index >= steps.length) {
      return $q.when(true); // end
    }

    var step = steps[index];
    if (typeof step !== 'function') {
      throw new Error('[helptip] Invalid step at index {0} of \'{1}\' tour: step must be a function'.format(index, partName));
    }
    var promise = step();
    if (typeof promise === 'boolean') {
      promise = $q.when(promise);
    }
    return promise
      .then(function(next) {
        if (angular.isUndefined(next)) {
          $scope.continue = false;
          return index; // keep same index (no button press: popover just closed)
        }
        if (!next || index === steps.length - 1) {
          return next ? -1 : index+1; // last step OK, so mark has finished
        }
        return $scope.executeStep(partName, steps, index+1);
      })
      .catch(function(err) {
        if (err && err.message == 'transition prevented') {
          console.error('ERROR: in help tour [{0}], in step [{1}] -> use large if exists, to prevent [transition prevented] error'.format(partName, index));
        }
        else {
          console.error('ERROR: in help tour  [{0}], in step [{1}] : {2}'.format(partName, index, err));
        }
        $scope.continue = false;
        return index;
      });
  };

  $scope.showHelpTip = function(id, options) {
    options = options || {};
    options.bindings = options.bindings || {};
    options.bindings.value =options.bindings.value || '';
    options.bindings.hasNext = angular.isDefined(options.bindings.hasNext) ? options.bindings.hasNext : true;
    options.timeout = options.timeout || (Device.enable ? 900 : 500);
    options.autoremove = true; // avoid memory leak
    options.bindings.tour = $scope.tour;
    options.backdropClickToClose = !$scope.tour;
    return UIUtils.popover.helptip(id, options);
  };

  $scope.showHelpModal = function(helpAnchor) {
    Modals.showHelp({anchor: helpAnchor});
  };

  $scope.startHelpTour = function() {
    $scope.tour = true;
    $scope.continue = true;

    // Currency
    return $scope.startCurrencyTour(0, true)
      .then(function(endIndex){
        if (!endIndex || $scope.cancelled) return false;
        csSettings.data.helptip.currency=endIndex;
        csSettings.store();
        return $scope.continue;
      })

      // Network
      .then(function(next){
        if (!next) return false;
        return $scope.startNetworkTour(0, true)
          .then(function(endIndex){
            if (!endIndex || $scope.cancelled) return false;
            csSettings.data.helptip.network=endIndex;
            csSettings.store();
            return $scope.continue;
          });
      })

      // Wot lookup
      .then(function(next){
        if (!next) return false;
        return $scope.startWotLookupTour(0, true)
          .then(function(endIndex){
            if (!endIndex || $scope.cancelled) return false;
            csSettings.data.helptip.wotLookup=endIndex;
            csSettings.store();
            return $scope.continue;
          });
      })

      // Wot identity
      .then(function(next){
        if (!next) return false;
        return $scope.startWotTour(0, true)
          .then(function(endIndex){
            if (!endIndex || $scope.cancelled) return false;
            csSettings.data.helptip.wot=endIndex;
            csSettings.store();
            return $scope.continue;
          });
      })

      // Identity certifications
      .then(function(next){
        if (!next) return false;
        return $scope.startWotCertTour(0, true)
          .then(function(endIndex){
            if (!endIndex) return false;
            csSettings.data.helptip.wotCerts=endIndex;
            csSettings.store();
            return $scope.continue;
          });
      })

      // Wallet (if NOT login)
      .then(function(next){
        if (!next) return false;
        return $scope.startWalletNoLoginTour(0, true);
      })

      // Wallet (if login)
      .then(function(next){
        if (!next) return false;
        if (!csWallet.isLogin()) return true; // not login: continue
        return $scope.startWalletTour(0, true)
          .then(function(endIndex){
            if (!endIndex) return false;
            csSettings.data.helptip.wallet=endIndex;
            csSettings.store();
            return $scope.continue;
          });
      })

      // Wallet certifications
      .then(function(next){
        if (!next) return false;
        if (!csWallet.isLogin()) return true; // not login: continue
        return $scope.startWalletCertTour(0, true)
          .then(function(endIndex){
            if (!endIndex) return false;
            csSettings.data.helptip.walletCerts=endIndex;
            csSettings.store();
            return $scope.continue;
          });
      })

      // My operations (if login)
      .then(function(next){
        if (!next) return false;
        if (!csWallet.isLogin()) return true; // not login: continue
        return $scope.startTxTour(0, true)
          .then(function(endIndex){
            if (!endIndex) return false;
            csSettings.data.helptip.tx=endIndex;
            csSettings.store();
            return $scope.continue;
          });
      })

      // Header tour
      .then(function(next){
        if (!next) return false;
        return $scope.startHeaderTour(0, true);
      })

      // Settings tour
      .then(function(next){
        if (!next) return false;
        return $scope.startSettingsTour(0, true);
      })

      // Finish tour
      .then(function(next){
        if (!next) return false;
        return $scope.finishTour();
      });
  };

  /**
   * Features tour on currency
   * @returns {*}
   */
  $scope.startCurrencyTour = function(startIndex, hasNext) {

    var showWotTabIfNeed  = function() {
      if ($state.is('app.currency.tab_parameters')) {
        $state.go('app.currency.tab_wot');
      }
    };

    var contentParams;

    var steps = [

      function(){
        $ionicSideMenuDelegate.toggleLeft(true);
        return $scope.showHelpTip('helptip-menu-btn-currency', {
          bindings: {
            content: 'HELP.TIP.MENU_BTN_CURRENCY',
            icon: {
              position: 'left'
            }
          }
        });
      },

      function () {
        if ($ionicSideMenuDelegate.isOpen()) {
          $ionicSideMenuDelegate.toggleLeft(false);
        }
        return $state.go(UIUtils.screen.isSmall() ? 'app.currency' : 'app.currency_lg')
          .then(function () {
            return $scope.showHelpTip('helptip-currency-mass-member', {
              bindings: {
                content: 'HELP.TIP.CURRENCY_MASS',
                icon: {
                  position: 'center'
                }
              }
            });
          });
      },

      function () {
        if (!csSettings.data.useRelative) return true; //skip but continue
        return $scope.showHelpTip('helptip-currency-mass-member-unit', {
          bindings: {
            content: 'HELP.TIP.CURRENCY_UNIT_RELATIVE',
            contentParams: contentParams,
            icon: {
              position: UIUtils.screen.isSmall() ? 'right' : 'center'
            }
          }
        });
      },

      // function () {
      //   if (!csSettings.data.useRelative) return true; //skip but continue
      //   $anchorScroll('helptip-currency-change-unit');
      //   return $scope.showHelpTip('helptip-currency-change-unit', {
      //     bindings: {
      //       content: 'HELP.TIP.CURRENCY_CHANGE_UNIT',
      //       contentParams: contentParams,
      //       icon: {
      //         position: UIUtils.screen.isSmall() ? 'right' : undefined
      //       }
      //     }
      //   });
      // },
      //
      // function () {
      //   if (csSettings.data.useRelative) return true; //skip but continue
      //   $anchorScroll('helptip-currency-change-unit');
      //   return $scope.showHelpTip('helptip-currency-change-unit', {
      //     bindings: {
      //       content: 'HELP.TIP.CURRENCY_CHANGE_UNIT_TO_RELATIVE',
      //       contentParams: contentParams,
      //       icon: {
      //         position: UIUtils.screen.isSmall() ? 'right' : undefined
      //       }
      //     }
      //   });
      // },

      function () {
        $anchorScroll('helptip-currency-rules-anchor');
        return $scope.showHelpTip('helptip-currency-rules', {
          bindings: {
            content: 'HELP.TIP.CURRENCY_RULES',
            icon: {
              position: 'center',
              glyph: 'ion-information-circled'
            }
          }
        });
      },

      function () {
        showWotTabIfNeed();
        $anchorScroll('helptip-currency-newcomers-anchor');
        return $scope.showHelpTip('helptip-currency-newcomers', {
          bindings: {
            content: 'HELP.TIP.CURRENCY_WOT',
            icon: {
              position: 'center'
            },
            hasNext: hasNext
          },
          timeout: 1200 // need for Firefox
        });
      }
    ];

    // Get currency parameters, with currentUD
    return csCurrency.get().then(function(currency) {
      contentParams = currency.parameters;
      // Launch steps
      return $scope.executeStep('currency', steps, startIndex);
    });
  };

  /**
   * Features tour on network
   * @returns {*}
   */
  $scope.startNetworkTour = function(startIndex, hasNext) {

    var showNetworkTabIfNeed  = function() {
      if ($state.is('app.currency')) {
        // Select the second tabs
        $timeout(function () {
          var tabs = $window.document.querySelectorAll('ion-tabs .tabs a');
          if (tabs && tabs.length == 3) {
            angular.element(tabs[2]).triggerHandler('click');
          }
        }, 100);
      }
    };

    var contentParams;

    var steps = [

      function(){
        if (UIUtils.screen.isSmall()) return true; // skip but continue
        $ionicSideMenuDelegate.toggleLeft(true);
        return $scope.showHelpTip('helptip-menu-btn-network', {
          bindings: {
            content: 'HELP.TIP.MENU_BTN_NETWORK',
            icon: {
              position: 'left'
            }
          }
        });
      },

      function () {
        if ($ionicSideMenuDelegate.isOpen()) {
          $ionicSideMenuDelegate.toggleLeft(false);
        }
        return $state.go(UIUtils.screen.isSmall() ? 'app.currency.tab_network' : 'app.network')
          .then(function () {
            showNetworkTabIfNeed();
            return $scope.showHelpTip('helptip-network-peers', {
              bindings: {
                content: 'HELP.TIP.NETWORK_BLOCKCHAIN',
                icon: {
                  position: 'center',
                  glyph: 'ion-information-circled'
                }
              },
              timeout: 1200 // need for Firefox
            });
          });
      },

      function() {
        showNetworkTabIfNeed();
        return $scope.showHelpTip('helptip-network-peer-0', {
          bindings: {
            content: 'HELP.TIP.NETWORK_PEERS',
            icon: {
              position: UIUtils.screen.isSmall() ? undefined : 'center'
            }
          },
          timeout: 1000,
          retry: 20
        });
      },


      function() {
        showNetworkTabIfNeed();
        return $scope.showHelpTip('helptip-network-peer-0-block', {
          bindings: {
            content: 'HELP.TIP.NETWORK_PEERS_BLOCK_NUMBER',
            icon: {
              position: UIUtils.screen.isSmall() ? undefined : 'center'
            }
          }
        });
      },

      function() {
        showNetworkTabIfNeed();
        var locale = csSettings.data.locale.id;
        return $scope.showHelpTip('helptip-network-peers', {
          bindings: {
            content: 'HELP.TIP.NETWORK_PEERS_PARTICIPATE',
            contentParams: {
              installDocUrl: (csConfig.helptip && csConfig.helptip.installDocUrl) ?
                (csConfig.helptip.installDocUrl[locale] ? csConfig.helptip.installDocUrl[locale] : csConfig.helptip.installDocUrl) :
                'http://duniter.org'
            },
            icon: {
              position: 'center',
              glyph: 'ion-information-circled'
            },
            hasNext: hasNext
          }
        });
      }
    ];

    // Get currency parameters, with currentUD
    return csCurrency.parameters().then(function(parameters) {
      contentParams = parameters;
      // Launch steps
      return $scope.executeStep('network', steps, startIndex);
    });
  };

  /**
   * Features tour on WOT lookup
   * @returns {*}
   */
  $scope.startWotLookupTour = function(startIndex, hasNext) {

    var steps = [
      function() {
        $ionicSideMenuDelegate.toggleLeft(true);
        return $scope.showHelpTip('helptip-menu-btn-wot', {
          bindings: {
            content: 'HELP.TIP.MENU_BTN_WOT',
            icon: {
              position: 'left'
            }
          },
          onError: 'continue'
        });
      },

      function() {
        if ($ionicSideMenuDelegate.isOpen()) {
          $ionicSideMenuDelegate.toggleLeft(false);
        }
        return $state.go(UIUtils.screen.isSmall() ? 'app.wot_lookup.tab_search' : 'app.wot_lookup_lg')
          .then(function(){
            return $scope.showHelpTip('helptip-wot-search-text', {
              bindings: {
                content: UIUtils.screen.isSmall() ? 'HELP.TIP.WOT_SEARCH_TEXT_XS' : 'HELP.TIP.WOT_SEARCH_TEXT',
                icon: {
                  position: 'center'
                }
              }
            });
          });
      },

      function() {
        return $scope.showHelpTip('helptip-wot-search-result-0', {
          bindings: {
            content: 'HELP.TIP.WOT_SEARCH_RESULT',
            icon: {
              position: 'center'
            }
          },
          timeout: 700,
          retry: 15
        });
      },

      function() {
        var element = $window.document.getElementById('helptip-wot-search-result-0');
        if (!element) return true;
        $timeout(function() {
          angular.element(element).triggerHandler('click');
        });
        return $scope.showHelpTip('helptip-wot-view-certifications', {
          bindings: {
            content: 'HELP.TIP.WOT_VIEW_CERTIFICATIONS',
            hasNext: hasNext
          },
          timeout: 2500
        });
      }
    ];

    // Launch steps
    return $scope.executeStep('wotLookup', steps, startIndex);
  };

  /**
   * Features tour on WOT identity
   * @returns {*}
   */
  $scope.startWotTour = function(startIndex, hasNext) {
    var contentParams;

    var steps = [
      function() {
        return $scope.showHelpTip('helptip-wot-view-certifications', {
          bindings: {
            content: 'HELP.TIP.WOT_VIEW_CERTIFICATIONS_COUNT',
            contentParams: contentParams,
            icon: {
              position: 'center',
              glyph: 'ion-information-circled'
            }
          }
        });
      },

      function() {
        return $scope.showHelpTip('helptip-wot-view-certifications-count', {
          bindings: {
            content: 'HELP.TIP.WOT_VIEW_CERTIFICATIONS_CLICK',
            icon: {
              position: 'center'
            },
            hasNext: hasNext
          }
        });
      }
    ];

    // Get currency parameters, with currentUD
    return csCurrency.get().then(function(currency) {
      contentParams = currency.parameters;
      contentParams.currentUD = currency.currentUD;
      // Launch steps
      return $scope.executeStep('wot', steps, startIndex);
    });
  };

  /**
   * Features tour on wot certifications
   * @returns {*}
   */
  $scope.startWotCertTour = function(startIndex, hasNext) {
    var steps = [

      function() {
        // If on identity: click on certifications
        if ($state.is('app.wot_identity')) {
          var element = $window.document.getElementById('helptip-wot-view-certifications');
          if (!element) return true;
          $timeout(function() {
            angular.element(element).triggerHandler('click');
          });
        }
        return $scope.showHelpTip(UIUtils.screen.isSmall() ? 'fab-certify': 'helptip-certs-certify', {
          bindings: {
            content: 'HELP.TIP.WOT_VIEW_CERTIFY',
            icon: {
              position: UIUtils.screen.isSmall() ? 'bottom-right' : 'center'
            }
          },
          timeout: UIUtils.screen.isSmall() ? 2000 : 1000,
          retry: 10
        });
      },

      function() {
        return $scope.showHelpTip(UIUtils.screen.isSmall() ? 'fab-certify': 'helptip-certs-certify', {
          bindings: {
            content: 'HELP.TIP.CERTIFY_RULES',
            icon: {
              position: 'center',
              glyph: 'ion-alert-circled'
            },
            hasNext: hasNext
          }
        });
      }
    ];

    return $scope.executeStep('certs', steps, startIndex);
  };

  /**
   * Features tour on wallet (if not login)
   * @returns {*}
   */
  $scope.startWalletNoLoginTour = function(startIndex, hasNext) {
    if (csWallet.isLogin()) return $q.when(true); // skip if login

    var steps = [
      function () {
        $ionicSideMenuDelegate.toggleLeft(true);
        return $scope.showHelpTip('helptip-menu-btn-account', {
          bindings: {
            content: 'HELP.TIP.MENU_BTN_ACCOUNT',
            icon: {
              position: 'left'
            },
            hasNext: hasNext
          }
        });
      }
    ];

    return $scope.executeStep('wallet-no-login', steps, startIndex);
  };

  /**
   * Features tour on wallet screens
   * @returns {*}
   */
  $scope.startWalletTour = function(startIndex, hasNext) {
    if (!csWallet.isLogin()) return $q.when(true); // skip if not login

    var hasCertificationsItem = csWallet.data.isMember||(csWallet.data.requirements && csWallet.data.requirements.pendingMembership);
    var contentParams;

    var steps = [
      function () {
        $ionicSideMenuDelegate.toggleLeft(true);
        return $scope.showHelpTip('helptip-menu-btn-account', {
          bindings: {
            content: csWallet.data.isMember ? 'HELP.TIP.MENU_BTN_ACCOUNT_MEMBER' : 'HELP.TIP.MENU_BTN_ACCOUNT',
            icon: {
              position: 'left'
            }
          }
        });
      },

      function () {
        if ($ionicSideMenuDelegate.isOpen()) {
          $ionicSideMenuDelegate.toggleLeft(false);
        }

        // Go to wallet
        return $state.go('app.view_wallet')
          .then(function () {
            return $scope.showHelpTip(UIUtils.screen.isSmall() ? 'helptip-wallet-options-xs' : 'helptip-wallet-options', {
              bindings: {
                content: 'HELP.TIP.WALLET_OPTIONS',
                icon: {
                  position: UIUtils.screen.isSmall() ? 'right' : 'center'
                }
              }
            });
          });
      },

      // Wallet pubkey
      function () {
        $anchorScroll('helptip-wallet-pubkey');
        return $scope.showHelpTip('helptip-wallet-pubkey', {
          bindings: {
            content: 'HELP.TIP.WALLET_PUBKEY',
            icon: {
              position: 'center'
            },
            hasNext: !hasCertificationsItem && hasNext
          },
          timeout: UIUtils.screen.isSmall() ? 2000 : 500,
          retry: 10
        });
      },

      function () {
        if (!hasCertificationsItem) return hasNext;
        $anchorScroll('helptip-wallet-certifications');
        return $scope.showHelpTip('helptip-wallet-certifications', {
          bindings: {
            content: UIUtils.screen.isSmall() ? 'HELP.TIP.WALLET_RECEIVED_CERTIFICATIONS': 'HELP.TIP.WALLET_CERTIFICATIONS',
            icon: {
              position: 'center'
            },
            hasNext: hasNext
          },
          timeout: 500,
          onError: 'continue' // if simple wallet: no certification item, so continue
        });
      }
    ];

    // Check that constants are well configured
    if (steps.length != csHelpConstants.wallet.stepCount) {
      console.error("[help] Invalid value of 'csHelpConstants.wallet.stepCount'. Please update to {0}".format(steps.length));
    }

    // Get currency parameters, with currentUD
    return csCurrency.get()
      .then(function(currency) {
        contentParams = currency.parameters;
        contentParams.currentUD = currency.currentUD;
        // Launch steps
        return $scope.executeStep('wallet', steps, startIndex);
      });
  };

  /**
   * Features tour on wallet certifications
   * @returns {*}
   */
  $scope.startWalletCertTour = function(startIndex, hasNext) {
    if (!csWallet.isLogin()) return $q.when(true);

    var contentParams;
    var skipAll = false;

    var steps = [

      function() {
        // If on wallet : click on certifications
        if ($state.is('app.view_wallet')) {
          var element = $window.document.getElementById('helptip-wallet-certifications');
          if (!element) {
            skipAll = true;
            return true;
          }
          $timeout(function() {
            angular.element(element).triggerHandler('click');
          });
        }
        if (!UIUtils.screen.isSmall()) return true; // skip this helptip if not in tabs mode
        return $scope.showHelpTip('helptip-received-certs', {
          bindings: {
            content: 'HELP.TIP.WALLET_RECEIVED_CERTS'
          }
        });
      },

      function() {
        if (skipAll || !UIUtils.screen.isSmall()) return true;
        return $state.go('app.view_wallet') // go back to wallet (small device only)
          .then(function() {
            return $scope.showHelpTip('helptip-wallet-given-certifications', {
              bindings: {
                content: 'HELP.TIP.WALLET_GIVEN_CERTIFICATIONS',
                icon: {
                  position: 'center'
                }
              },
              timeout: 500
            });
        });
      },

      function() {
        if (skipAll) return true;

        // Click on given cert link (small device only)
        if ($state.is('app.view_wallet')) {
          var element = $window.document.getElementById('helptip-wallet-given-certifications');
          if (!element) {
            skipAll = true;
            return true;
          }
          $timeout(function() {
            angular.element(element).triggerHandler('click');
          }, 500);
        }
        return $scope.showHelpTip(UIUtils.screen.isSmall() ? 'fab-select-certify': 'helptip-certs-select-certify', {
          bindings: {
            content: 'HELP.TIP.WALLET_CERTIFY',
            icon: {
              position: UIUtils.screen.isSmall() ? 'bottom-right' : 'center'
            }
          },
          timeout: UIUtils.screen.isSmall() ? 2000 : 500,
          retry: 10
        });
      },

      function() {
        if ($scope.tour || skipAll) return hasNext; // skip Rules if features tour (already display)
        return $scope.showHelpTip('helptip-certs-stock', {
          bindings: {
            content: 'HELP.TIP.CERTIFY_RULES',
            icon: {
              position: 'center',
              glyph: 'ion-alert-circled'
            },
            hasNext: hasNext
          }
        });
      }

      /* FIXME : how to select the left tab ?
      ,function() {
        return $scope.showHelpTip('helptip-certs-stock', {
          bindings: {
            content: 'HELP.TIP.WALLET_CERT_STOCK',
            contentParams: contentParams,
            icon: {
              position: 'center'
            },
            hasNext: hasNext
          }
        });
      }*/
    ];

    return csCurrency.parameters().then(function(parameters) {
      contentParams = parameters;
      return $scope.executeStep('certs', steps, startIndex);
    });
  };

  /**
   * Features tour on TX screen
   * @returns {*}
   */
  $scope.startTxTour = function(startIndex, hasNext) {
    if (!csWallet.isLogin()) return $q.when(true); // skip if not login

    var contentParams;

    var steps = [
      function () {
        $ionicSideMenuDelegate.toggleLeft(true);
        return $scope.showHelpTip('helptip-menu-btn-tx', {
          bindings: {
            content: csWallet.data.isMember ? 'HELP.TIP.MENU_BTN_TX_MEMBER' : 'HELP.TIP.MENU_BTN_TX',
            icon: {
              position: 'left'
            }
          }
        });
      },

      function () {
        if ($ionicSideMenuDelegate.isOpen()) {
          $ionicSideMenuDelegate.toggleLeft(false);
        }

        // Go to wallet
        return $state.go('app.view_wallet_tx')
          .then(function () {
            return $scope.showHelpTip('helptip-wallet-balance', {
              bindings: {
                content: csSettings.data.useRelative ? 'HELP.TIP.WALLET_BALANCE_RELATIVE' : 'HELP.TIP.WALLET_BALANCE',
                contentParams: contentParams,
                icon: {
                  position: 'center'
                }
              },
              retry: 20 // 10 * 500 = 5s max
            });
          });
      },

      function () {
        return $scope.showHelpTip('helptip-wallet-balance', {
          bindings: {
            content: 'HELP.TIP.WALLET_BALANCE_CHANGE_UNIT',
            contentParams: contentParams,
            icon: {
              position: 'center',
              glyph: 'ion-information-circled'
            }
          }
        });
      }
    ];

    // Get currency parameters, with currentUD
    return csCurrency.get()
      .then(function(currency) {
        contentParams = currency.parameters;
        contentParams.currentUD = currency.currentUD;
        // Launch steps
        return $scope.executeStep('tx', steps, startIndex);
      });
  };

  /**
   * header tour
   * @returns {*}
   */
  $scope.startHeaderTour = function(startIndex, hasNext) {
    if (UIUtils.screen.isSmall()) return $q.when(true);

    function _getProfilBtnElement() {
      var elements = $window.document.querySelectorAll('#helptip-header-bar-btn-profile');
      if (!elements || !elements.length) return null;
      return _.find(elements, function(el) {return el.offsetWidth > 0;});
    }

    var steps = [
      function () {

        if (UIUtils.screen.isSmall()) return true; // skip for small screen
        var element = _getProfilBtnElement();
        if (!element) return true;
        return $scope.showHelpTip(element, {
          bindings: {
            content: 'HELP.TIP.HEADER_BAR_BTN_PROFILE',
            icon: {
              position: 'right'
            }
          }
        });
      },

      function () {
        // small screens
        if (UIUtils.screen.isSmall()) {
          $ionicSideMenuDelegate.toggleLeft(true);
          return $scope.showHelpTip('helptip-menu-btn-settings', {
            bindings: {
              content: 'HELP.TIP.MENU_BTN_SETTINGS',
              icon: {
                position: 'left'
              },
              hasNext: hasNext
            },
            timeout: 1000
          });
        }
        // wide screens
        else {
          var element = _getProfilBtnElement();
          if (!element) return true;
          $timeout(function() {
            angular.element(element).triggerHandler('click');
          });
          return $scope.showHelpTip('helptip-popover-profile-btn-settings', {
            bindings: {
              content: 'HELP.TIP.MENU_BTN_SETTINGS',
              icon: {
                position: 'center'
              },
              hasNext: hasNext
            },
            timeout: 1000
          })
            .then(function(res) {
              // close profile popover
              $scope.closeProfilePopover();
              return res;
            });
        }
      }
    ];

    return $scope.executeStep('header', steps, startIndex);
  };

  /**
   * Settings tour
   * @returns {*}
   */
  $scope.startSettingsTour = function(startIndex, hasNext) {
    var contentParams;
    var steps = [

      function () {
        if (!UIUtils.screen.isSmall()) return true;
        $ionicSideMenuDelegate.toggleLeft(true);
        return $scope.showHelpTip('helptip-menu-btn-settings', {
          bindings: {
            content: 'HELP.TIP.MENU_BTN_SETTINGS',
            icon: {
              position: 'left'
            }
          },
          timeout: 1000
        });
      },

      function () {
        if ($ionicSideMenuDelegate.isOpen()) {
          $ionicSideMenuDelegate.toggleLeft(false);
        }

        // Go to settings
        return $state.go('app.settings')
          .then(function () {
            return $scope.showHelpTip('helptip-settings-btn-unit-relative', {
              bindings: {
                content: 'HELP.TIP.SETTINGS_CHANGE_UNIT',
                contentParams: contentParams,
                icon: {
                  position: 'right',
                  style: 'margin-right: 60px'
                },
                hasNext: hasNext
              },
              timeout: 1000
            });
          });
      }
    ];

    return csCurrency.parameters()
      .then(function(parameters) {
        contentParams = parameters;
        return $scope.executeStep('settings', steps, startIndex);
      });
  };


  /**
   * Finish the features tour (last step)
   * @returns {*}
   */
  $scope.finishTour = function() {
    if ($ionicSideMenuDelegate.isOpen()) {
      $ionicSideMenuDelegate.toggleLeft(false);
    }

    // If login: redirect to wallet
    if (csWallet.isLogin()) {
      return $state.go('app.view_wallet')
        .then(function(){
          return $scope.showHelpTip('helptip-wallet-certifications', {
            bindings: {
              content: 'HELP.TIP.END_LOGIN',
              hasNext: false
            }
          });
        });
    }

    // If not login: redirect to home
    else {
      var contentParams;
      return $q.all([
        $scope.showHome(),

        csCurrency.parameters()
          .then(function(parameters) {
            contentParams = parameters;
          })
        ])
        .then(function(){
          return $scope.showHelpTip('helptip-home-logo', {
           bindings: {
             content: 'HELP.TIP.END_NOT_LOGIN',
             contentParams: contentParams,
             hasNext: false
           }
          });
        });
    }
  };
}

/* ----------------------------
 *  Help tour (auto start from home page)
 * ---------------------------- */
function HelpTourController($scope) {

  $scope.$on('$ionicView.enter', function(e, state) {
    $scope.startHelpTour();
  });

}

angular.module("cesium.templates", []).run(["$templateCache", function($templateCache) {$templateCache.put("templates/menu.html","<ion-side-menus enable-menu-with-back-views=\"true\" bind-notifier=\"{locale:$root.settings.locale.id}\">\n  <!-- HEADER -->\n  <ion-side-menu-content>\n    <ion-nav-bar class=\"bar-dark\" title-align=\"left\">\n      <ion-nav-back-button class=\"no-text\">\n      </ion-nav-back-button>\n\n      <ion-nav-buttons side=\"left\">\n        <button class=\"button button-icon button-clear icon ion-navicon visible-nomenu\" menu-toggle=\"left\"></button>\n      </ion-nav-buttons>\n      <ion-nav-buttons side=\"right\">\n\n        <!-- current node info -->\n        <button class=\"button button-clear hidden-xs hidden-sm gray icon-left\" ng-if=\"$root.settings.expertMode\" style=\"max-width: 450px !important\" ng-click=\"showNodeListPopover($event)\">\n          <small class=\"ion-locked\" ng-if=\"$root.currency.node.useSsl\">&nbsp;</small>\n          {{$root.currency.node.host}}{{$root.currency.node.port != 80 && $root.currency.node.port != 443 ? \':\'+$root.currency.node.port : \'\'}}\n          <small>&nbsp;</small>\n          <small class=\"ion-arrow-down-b\"></small>\n        </button>\n\n        <!-- Allow extension here -->\n        <cs-extension-point name=\"nav-buttons-right\"></cs-extension-point>\n\n        <!-- profile -->\n        <a id=\"helptip-header-bar-btn-profile\" class=\"button button-icon button-clear hidden-xs hidden-sm\" ng-click=\"showProfilePopover($event)\">\n          <i class=\"avatar avatar-member\" ng-if=\"!$root.walletData.avatar\" ng-class=\"{\'disable\': !login, \'royal-bg\': login}\">\n          </i>\n          <i class=\"avatar\" ng-if=\"$root.walletData.avatar\" style=\"background-image: url(\'{{$root.walletData.avatar.src}}\')\">\n          </i>\n          <span ng-if=\"login && !auth\" class=\"badge badge-button badge-secondary badge-assertive ion-locked\"> </span>\n        </a>\n      </ion-nav-buttons>\n    </ion-nav-bar>\n    <ion-nav-view name=\"menuContent\"></ion-nav-view>\n  </ion-side-menu-content>\n\n  <!-- MENU -->\n  <ion-side-menu id=\"menu\" side=\"left\" expose-aside-when=\"large\" enable-menu-with-back-views=\"false\" width=\"225\">\n    <ion-header-bar>\n      <h1 class=\"title dark hidden-sm hidden-xs\">\n        <span class=\"animate-fade-in animate-show-hide ng-hide\" ng-show=\"$root.currency.name\">\n          {{:locale:\'COMMON.APP_NAME\'|translate}} {{$root.currency.name|abbreviate}}\n        </span>\n      </h1>\n\n      <div class=\"visible-sm visible-xs hero\">\n        <div class=\"content\">\n          <i class=\"avatar avatar-member hero-icon\" ng-if=\"!$root.walletData.avatar\" ng-class=\"{\'royal-bg\': login, \'stable-bg\': !login}\" ng-click=\"!login ? showHome() : loginAndGo()\" menu-close></i>\n          <a class=\"avatar hero-icon\" ng-if=\"$root.walletData.avatar\" style=\"background-image: url(\'{{$root.walletData.avatar.src}}\')\" ui-sref=\"app.view_wallet\" menu-close></a>\n          <h4 ng-if=\"login\">\n            <a class=\"light\" ui-sref=\"app.view_wallet\" menu-close>\n              {{$root.walletData.name||$root.walletData.uid}}\n              <span ng-if=\"!$root.walletData.name && !$root.walletData.uid\"><i class=\"icon ion-key\"></i>&nbsp;{{$root.walletData.pubkey|formatPubkey}}</span>\n            </a>\n          </h4>\n          <h4 ng-if=\"!login\">\n            <a class=\"light\" ui-sref=\"app.home\" menu-close>\n              {{\'COMMON.BTN_LOGIN\'|translate}}\n              <i class=\"ion-arrow-right-b\"></i>\n            </a>\n          </h4>\n          <cs-extension-point name=\"menu-profile-user\"></cs-extension-point>\n        </div>\n        <!-- logout -->\n        <a ng-if=\"login\" class=\"button-icon\" ng-click=\"logout({askConfirm: true})\" style=\"position: absolute; top: 5px; left: 5px\">\n          <i class=\"icon stable ion-android-exit\"></i>\n        </a>\n      </div>\n    </ion-header-bar>\n\n    <ion-content scroll=\"false\">\n      <ion-list class=\"list\">\n\n        <!-- DISCOVER Section -->\n        <ion-item menu-close class=\"item-icon-left hidden-xs\" ui-sref=\"app.home\" active-link=\"active\">\n          <i class=\"icon ion-home\"></i>\n          {{:locale:\'MENU.HOME\'|translate}}\n        </ion-item>\n\n        <a menu-close class=\"item item-icon-left\" active-link=\"active\" active-link-path-prefix=\"#/app/currency\" ui-sref=\"app.currency\">\n          <i class=\"icon ion-ios-world-outline\"></i>\n          {{:locale:\'MENU.CURRENCY\'|translate}}\n        </a>\n        <a id=\"helptip-menu-btn-currency\"></a>\n\n        <a menu-close class=\"item item-icon-left hidden-xs hidden-sm\" active-link=\"active\" active-link-path-prefix=\"#/app/network\" ui-sref=\"app.network\">\n          <i class=\"icon ion-cloud\"></i>\n          {{:locale:\'MENU.NETWORK\'|translate}}\n        </a>\n        <a id=\"helptip-menu-btn-network\"></a>\n\n        <!-- Allow extension here -->\n        <cs-extension-point name=\"menu-discover\"></cs-extension-point>\n\n        <!-- MAIN Section -->\n        <div class=\"item item-divider\"></div>\n\n        <a menu-close class=\"item item-icon-left\" active-link=\"active\" active-link-path-prefix=\"#/app/wot\" ui-sref=\"app.wot_lookup.tab_search\">\n          <i class=\"icon ion-person-stalker\"></i>\n          {{:locale:\'MENU.WOT\'|translate}}\n        </a>\n        <a id=\"helptip-menu-btn-wot\"></a>\n\n        <!-- Allow extension here -->\n        <cs-extension-point name=\"menu-main\"></cs-extension-point>\n\n        <!-- USER Section -->\n        <div class=\"item item-divider\"></div>\n\n        <a menu-close class=\"item item-icon-left\" active-link=\"active\" active-link-path-prefix=\"#/app/wallet\" ui-sref=\"app.view_wallet\" ng-class=\"{\'item-menu-disable\': !login}\">\n          <i class=\"icon ion-person\"></i>\n          {{:locale:\'MENU.ACCOUNT\'|translate}}\n        </a>\n        <a id=\"helptip-menu-btn-account\"></a>\n\n        <a menu-close class=\"item item-icon-left\" active-link=\"active\" active-link-path-prefix=\"#/app/history\" ui-sref=\"app.view_wallet_tx\" ng-class=\"{\'item-menu-disable\': !login}\">\n          <i class=\"icon ion-card\"></i>\n          {{:locale:\'MENU.TRANSACTIONS\'|translate}}\n        </a>\n        <a id=\"helptip-menu-btn-tx\"></a>\n\n        <div class=\"item item-divider visible-xs visible-sm\"></div>\n\n        <!-- Allow extension here -->\n        <cs-extension-point name=\"menu-user\"></cs-extension-point>\n\n        <a menu-close class=\"item item-icon-left visible-xs visible-sm\" active-link=\"active\" active-link-path-prefix=\"#/app/settings\" ui-sref=\"app.settings\">\n          <i class=\"icon ion-android-settings\"></i>\n          {{:locale:\'MENU.SETTINGS\'|translate}}\n        </a>\n        <a id=\"helptip-menu-btn-settings\"></a>\n\n        <!-- actions divider -->\n        <div class=\"item item-divider\" ng-if=\"login\"></div>\n\n        <!-- transfer -->\n        <ion-item menu-close class=\"item item-button-right hidden-xs hidden-sm\" ng-if=\"login\">\n          {{:locale:\'MENU.TRANSFER\'|translate}}\n          <button class=\"button button-positive ink-dark\" ng-click=\"showTransferModal()\">\n          <i class=\"icon ion-paper-airplane\"></i>\n          </button>\n        </ion-item>\n\n        <!-- scan QR code -->\n        <ion-item menu-close class=\"item item-button-right\" ng-if=\"$root.device.barcode.enable\">\n          {{:locale:\'MENU.SCAN\'|translate}}\n          <button class=\"button button-stable ink\" ng-click=\"scanQrCodeAndGo()\">\n            <i class=\"icon ion-qr-scanner\"></i>\n          </button>\n        </ion-item>\n\n        <cs-extension-point name=\"menu-actions\"></cs-extension-point>\n\n      </ion-list>\n\n    </ion-content>\n\n    <!-- removeIf(device) -->\n    <ion-footer-bar class=\"bar-stable footer hidden-xs hidden-sm\">\n      <a class=\"pull-left icon-help\" menu-toggle=\"left\" title=\"{{:locale:\'HOME.BTN_HELP\'|translate}}\" ui-sref=\"app.help\"></a>\n\n      <a class=\"title gray\" ng-click=\"showAboutModal()\">\n\n        <!-- version -->\n        <span title=\"{{:locale:\'HOME.BTN_ABOUT\'|translate}}\" ng-class=\"{\'assertive\': $root.newRelease}\">\n          <!-- warning icon, if new version available -->\n          <i ng-if=\"$root.newRelease\" class=\"ion-alert-circled assertive\"></i>\n\n          {{:locale:\'COMMON.APP_VERSION\'|translate:{version: config.version} }}\n        </span>\n        |\n        <!-- about -->\n        <span title=\"{{:locale:\'HOME.BTN_ABOUT\'|translate}}\">\n          {{:locale:\'HOME.BTN_ABOUT\'|translate}}\n        </span>\n      </a>\n\n\n    </ion-footer-bar>\n    <!-- endRemoveIf(device) -->\n  </ion-side-menu>\n\n\n</ion-side-menus>\n");
$templateCache.put("templates/modal_about.html","<ion-modal-view class=\"about\">\n  <ion-header-bar class=\"bar-positive\">\n    <button class=\"button button-clear visible-xs\" ng-click=\"closeModal()\" translate>COMMON.BTN_CLOSE\n    </button>\n    <h1 class=\"title\" translate>ABOUT.TITLE</h1>\n  </ion-header-bar>\n\n  <ion-content class=\"text-center\" scroll=\"true\">\n\n    <ion-list class=\"item-wrap-text\">\n      <ion-item class=\"item-icon-left item-text-wrap\">\n        {{\'COMMON.APP_NAME\'|translate}}&nbsp;<b>{{\'COMMON.APP_VERSION\'|translate:$root.config}}</b>\n        <i ng-if=\"$root.newRelease\" class=\"assertive ion-alert-circled\"></i>\n        <h3 ng-if=\"$root.config.build\" class=\"gray\">{{\'COMMON.APP_BUILD\'|translate:$root.config}}</h3>\n        <span translate>ABOUT.LICENSE</span>\n      </ion-item>\n\n      <!-- new version -->\n      <ion-item class=\"item-icon-left\" ng-if=\"$root.newRelease\">\n        <i class=\"item-image icon ion-alert-circled assertive\"></i>\n\n        <span ng-if=\"!$root.device.isWeb()\" ng-bind-html=\"\'ABOUT.PLEASE_UPDATE\' | translate:$root.newRelease \"></span>\n        <span ng-if=\"$root.device.isWeb()\" ng-bind-html=\"\'ABOUT.LATEST_RELEASE\' | translate:$root.newRelease \"></span>\n\n        <!-- link to release page -->\n        <h3 ng-if=\"!$root.device.enable\">\n          <a ng-click=\"openLink($event, $root.newRelease.url)\" translate>{{::$root.newRelease.url}}</a>\n        </h3>\n      </ion-item>\n\n      <!-- report issue -->\n      <ion-item class=\"item-icon-left item-text-wrap\">\n        <i class=\"item-image icon ion-bug\"></i>\n        <span translate>ABOUT.PLEASE_REPORT_ISSUE</span>\n        <h3>\n          <a ng-click=\"openLink($event, $root.settings.newIssueUrl)\" translate>ABOUT.REPORT_ISSUE</a>\n        </h3>\n      </ion-item>\n\n\n      <!-- source code -->\n      <ion-item class=\"item-icon-left\">\n        <i class=\"item-image icon ion-network\"></i>\n        {{\'ABOUT.CODE\' | translate}}\n        <h3><a ng-click=\"openLink($event, \'https://git.duniter.org/clients/cesium/cesium\')\">https://git.duniter.org/clients/cesium/cesium</a></h3>\n      </ion-item>\n\n      <!-- forum -->\n      <ion-item class=\"item-icon-left\">\n        <i class=\"item-image icon ion-chatbubbles\"></i>\n        {{\'ABOUT.FORUM\' | translate}}\n        <h3><a ng-click=\"openLink($event, $root.settings.userForumUrl)\">{{::$root.settings.userForumUrl}}</a></h3>\n      </ion-item>\n\n      <!-- team -->\n      <ion-item class=\"item-icon-left\">\n        <i class=\"item-image icon ion-person-stalker\"></i>\n        {{\'ABOUT.DEVELOPERS\' | translate}}\n        <h3>\n          <a href=\"https://github.com/c-geek\" target=\"_system\">cgeek</a>,\n          <a href=\"https://github.com/devingfx\" target=\"_system\">DiG</a>,\n          <a href=\"https://github.com/blavenie\" target=\"_system\">Benoit Lavenier</a>\n        </h3>\n      </ion-item>\n\n      <div class=\"padding hidden-xs text-center\">\n        <button class=\"button button-stable icon-left ink\" type=\"submit\" ng-click=\"closeModal()\" ui-sref=\"app.help\">\n          <i class=\"icon ion-ios-help-outline\"></i>\n          {{\'HOME.BTN_HELP\' | translate}}\n        </button>\n\n        <button class=\"button button-positive ink\" type=\"submit\" ng-click=\"closeModal()\">\n          {{\'COMMON.BTN_CLOSE\' | translate}}\n        </button>\n      </div>\n\n    \n  </ion-list></ion-content>\n</ion-modal-view>\n");
$templateCache.put("templates/api/doc.html","\n\n  <!-- transfer -->\n  <h2 class=\"padding\" translate>API.DOC.TRANSFER.TITLE</h2>\n\n\n  <div class=\"list padding no-padding-xs no-padding-top\">\n\n    <div class=\"item item-divider no-border\">\n      <p translate>API.DOC.DESCRIPTION_DIVIDER</p>\n    </div>\n\n    <div class=\"item item-text-wrap\">\n      <p translate>API.DOC.TRANSFER.DESCRIPTION</p>\n    </div>\n\n    <div class=\"item item-divider no-border\">\n      <p translate>API.DOC.URL_DIVIDER</p>\n    </div>\n\n    <div class=\"item item-text-wrap\">\n      <p class=\"gray text-right\">\n        {{$root.rootPath}}#/v1/payment/:pubkey?amount=<span class=\"text-italic\" translate>API.DOC.TRANSFER.PARAM_AMOUNT</span>\n      </p>\n    </div>\n\n    <div class=\"item item-divider no-border\">\n      <p translate>API.DOC.PARAMETERS_DIVIDER</p>\n    </div>\n\n    <div class=\"item item-text-wrap\">\n      <p translate>API.DOC.AVAILABLE_PARAMETERS</p>\n      <div class=\"row\">\n        <div class=\"col col-20 text-italic\">pubkey</div>\n        <div class=\"col gray\" translate>API.DOC.TRANSFER.PARAM_PUBKEY_HELP</div>\n      </div>\n      <div class=\"row stable-bg\">\n        <div class=\"col col-20 text-italic dark\">amount</div>\n        <div class=\"col gray\" translate>API.DOC.TRANSFER.PARAM_AMOUNT_HELP</div>\n      </div>\n      <div class=\"row\">\n        <div class=\"col col-20 text-italic\">comment</div>\n        <div class=\"col gray\" translate>API.DOC.TRANSFER.PARAM_COMMENT_HELP</div>\n      </div>\n      <div class=\"row stable-bg\">\n        <div class=\"col col-20 text-italic dark\">name</div>\n        <div class=\"col gray\" translate>API.DOC.TRANSFER.PARAM_NAME_HELP</div>\n      </div>\n      <div class=\"row\">\n        <div class=\"col col-20 text-italic\">redirect_url</div>\n        <div class=\"col gray\" translate>API.DOC.TRANSFER.PARAM_REDIRECT_URL_HELP</div>\n      </div>\n      <div class=\"row stable-bg\">\n        <div class=\"col col-20 text-italic dark\">cancel_url</div>\n        <div class=\"col gray\" translate>API.DOC.TRANSFER.PARAM_CANCEL_URL_HELP</div>\n      </div>\n    </div>\n\n    <!-- demo -->\n    <div class=\"item item-divider no-border\">\n      <p translate>API.DOC.DEMO_DIVIDER</p>\n    </div>\n    <div class=\"item item-button-right item-text-wrap padding-bottom\">\n      <p class=\"item-icon-right-padding\" translate>API.DOC.DEMO_HELP</p>\n      <span class=\"badge\" ng-if=\"loading\">\n        <ion-spinner class=\"icon\" icon=\"android\"></ion-spinner>\n      </span>\n      <a ng-if=\"!loading\" href=\"{{transferDemoUrl}}\" class=\"button button-raised button-positive icon ion-play\"></a>\n    </div>\n    <div class=\"item item-text-wrap\" ng-if=\"result.type === \'payment\' && !result.cancelled\">\n      <h2 class=\"text-right balanced\" translate>API.DOC.DEMO_SUCCEED</h2>\n      <h4 class=\"gray\" translate>API.DOC.DEMO_RESULT</h4>\n      <p class=\"balanced-100-bg padding dark text-keep-lines\">{{result.content}}</p>\n    </div>\n    <div class=\"item item-text-wrap\" ng-if=\"result.type === \'payment\' && result.cancelled\">\n      <h2 class=\"text-right assertive\" translate>API.DOC.DEMO_CANCELLED</h2>\n    </div>\n\n    <div class=\"item item-divider no-border\">\n      <p translate>API.DOC.INTEGRATE_DIVIDER</p>\n    </div>\n\n    <div class=\"item item-text-wrap\">\n      <p translate>API.DOC.TRANSFER.EXAMPLES_HELP</p>\n\n      <div class=\"row responsive-sm\">\n        <div class=\"col col-20 text-italic\">\n          <span translate>API.DOC.TRANSFER.EXAMPLE_BUTTON</span>\n        </div>\n\n        <!-- no text on button -->\n        <div class=\"col gray no-border\">\n\n          <!-- code -->\n          <p>\n            <i class=\"icon ion-code\"></i>\n            <span translate>API.DOC.INTEGRATE_CODE</span>\n          </p>\n          <div class=\"item item-input\">\n            <textarea class=\"gray\" select-on-click rows=\"5\" ng-model=\"transferButton.html\" ng-model-options=\"{ debounce: 650 }\"></textarea>\n          </div>\n\n          <!-- preview -->\n          <p class=\"padding-top\">\n            <i class=\"icon ion-eye\"></i>\n            <span translate>API.DOC.INTEGRATE_RESULT</span>\n          </p>\n          <div class=\"padding-left\" bind-notifier=\"{ notifierKey:watchedExpression }\">\n            <ng-bind-html ng-bind-html=\"transferButton.html|trustAsHtml\"></ng-bind-html>\n          </div>\n        </div>\n      </div>\n      <div class=\"row responsive-sm\">\n\n        <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;</div>\n\n        <!-- HTML button parameters -->\n        <div class=\"col gray no-border\">\n          <p class=\"padding-top\">\n            <a class=\"positive\" ng-click=\"transferButton.showParameters=!transferButton.showParameters\">\n              <i class=\"icon ion-wrench\"></i>\n              <span translate>API.DOC.INTEGRATE_PARAMETERS</span>\n              <i class=\"icon\" ng-class=\"{\'ion-arrow-down-b\': !transferButton.showParameters, \'ion-arrow-up-b\': transferButton.showParameters}\"></i>\n            </a>\n          </p>\n\n          <div ng-if=\"transferButton.showParameters\" class=\"padding-left\">\n\n            <form id=\"transferForm\" class=\"stable-bg padding\">\n              <p class=\"padding-top\">\n                <i class=\"icon ion-key\"></i>\n                {{\'API.DOC.TRANSFER.PARAM_PUBKEY\' | translate}} :\n              </p>\n              <label class=\"item item-input\">\n                <input type=\"text\" ng-model=\"transferData.pubkey\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.PARAM_PUBKEY_HELP\'|translate}}\">\n              </label>\n\n              <p class=\"padding-top\">\n                <i class=\"icon ion-pricetag\"></i>\n                {{\'API.DOC.TRANSFER.PARAM_AMOUNT\' | translate}} :\n              </p>\n              <label class=\"item item-input\">\n                <input type=\"text\" ng-model=\"transferData.amount\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.PARAM_AMOUNT_HELP\'|translate}}\">\n              </label>\n\n              <p class=\"padding-top\">\n                <i class=\"icon ion-flag\"></i>\n                {{\'API.DOC.TRANSFER.PARAM_COMMENT\' | translate}} :\n              </p>\n              <label class=\"item item-input\">\n                <input type=\"text\" ng-model=\"transferData.comment\" placeholder=\"{{\'API.DOC.TRANSFER.PARAM_COMMENT\'|translate}}\">\n              </label>\n\n              <p class=\"padding-top\">\n                <i class=\"icon ion-ios-world-outline\"></i>\n                {{\'API.DOC.TRANSFER.PARAM_NAME\' | translate}} :\n              </p>\n              <label class=\"item item-input\">\n                <input type=\"text\" ng-model=\"transferData.name\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.PARAM_NAME\'|translate}}\">\n              </label>\n\n              <p class=\"padding-top\">\n                <i class=\"icon ion-arrow-return-left\"></i>\n                {{\'API.DOC.TRANSFER.PARAM_REDIRECT_URL\' | translate}} :\n              </p>\n              <label class=\"item item-input\">\n                <input type=\"text\" ng-model=\"transferData.redirect_url\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.PARAM_REDIRECT_URL\'|translate}}\">\n              </label>\n\n              <p class=\"padding-top\">\n                <i class=\"icon ion-backspace\"></i>\n                {{\'API.DOC.TRANSFER.PARAM_CANCEL_URL\' | translate}} :\n              </p>\n              <label class=\"item item-input\">\n                <input type=\"text\" ng-model=\"transferData.cancel_url\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.PARAM_CANCEL_URL\'|translate}}\">\n              </label>\n\n              <p class=\"padding-top\">\n                <a class=\"positive\" ng-click=\"transferButton.style.enable=!transferButton.style.enable\">\n                  <i class=\"icon\" ng-class=\"{\'ion-android-checkbox-outline\': transferButton.style.enable, \'ion-android-checkbox-outline-blank\': !transferButton.style.enable}\"></i>\n                  <span translate>API.DOC.TRANSFER.EXAMPLE_BUTTON_DEFAULT_STYLE</span>\n                </a>\n              </p>\n\n              <div ng-if=\"transferButton.style.enable\">\n\n                <!-- button icon -->\n                <p class=\"padding-top\">\n                  <i class=\"icon ion-image\"></i>\n                  {{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_TEXT_ICON\' | translate}} :\n                </p>\n                <label class=\"item item-input item-select\">\n                  <select class=\"stable-bg dark\" ng-model=\"transferButton.style.icon\" style=\"width: 100%; max-width: 100%\" ng-options=\"l as (l.label | translate) for l in transferButton.icons track by l.filename\">\n                  </select>\n                </label>\n\n                <!-- button text -->\n                <p class=\"padding-top\">\n                  <i class=\"icon ion-quote\"></i>\n                  {{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_TEXT_HELP\' | translate}} :\n                </p>\n                <label class=\"item item-input\">\n                  <input type=\"text\" ng-model=\"transferButton.style.text\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_TEXT_HELP\'|translate}}\">\n                </label>\n\n\n                <!-- button bg color -->\n                <p class=\"padding-top\">\n                  <i class=\"icon ion-paintbucket\"></i>\n                  {{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_BG_COLOR\' | translate}} :\n                </p>\n                <label class=\"item item-input\">\n                  <input type=\"text\" ng-model=\"transferButton.style.bgColor\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_BG_COLOR_HELP\'|translate}}\">\n                </label>\n\n                <!-- button font color -->\n                <p class=\"padding-top\">\n                  <i class=\"icon ion-paintbrush\"></i>\n                  {{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_FONT_COLOR\' | translate}} :\n                </p>\n                <label class=\"item item-input\">\n                  <input type=\"text\" ng-model=\"transferButton.style.fontColor\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_FONT_COLOR_HELP\'|translate}}\">\n                </label>\n\n                <!-- button width -->\n                <p class=\"padding-top\">\n                  <i class=\"icon ion-ios-crop-strong\"></i>\n                  {{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_TEXT_WIDTH\' | translate}} :\n                </p>\n                <label class=\"item item-input\">\n                  <input type=\"text\" ng-model=\"transferButton.style.width\" ng-model-options=\"{ debounce: 650 }\" placeholder=\"{{\'API.DOC.TRANSFER.EXAMPLE_BUTTON_TEXT_WIDTH_HELP\'|translate}}\">\n                </label>\n              </div>\n            </form></div>\n          \n\n        </div>\n      </div>\n    </div>\n\n  </div>\n");
$templateCache.put("templates/api/home.html","<ion-view class=\"circle-bg-dark\">\n  <ion-nav-title>\n    <span class=\"title visible-xs visible-sm\" translate>API.HOME.TITLE</span>\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"right\">\n    <!-- locales -->\n    <button class=\"button button-clear hidden-xs hidden-sm gray icon-left\" style=\"max-width: 450px !important\" ng-click=\"showLocalesPopover($event)\">\n      <i class=\"icon ion-earth\"></i>\n      {{$root.settings.locale.label}}\n      <small class=\"ion-arrow-down-b\"></small>\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content class=\"has-header no-padding-xs positive-900-bg\">\n\n    <br class=\"hidden-xs\">\n\n    <div class=\"light text-center\">\n      <h4 class=\"hidden-xs\" translate>API.HOME.MESSAGE</h4>\n      <h4 class=\"visible-xs padding\" translate>API.HOME.MESSAGE_SHORT</h4>\n    </div>\n\n    <br class=\"hidden-xs\">\n\n    <div class=\"row no-padding-xs\">\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;</div>\n      <div class=\"col\">\n\n        <div class=\"light-bg no-padding\">\n          <!-- include documentation -->\n          <ng-include src=\"\'templates/api/doc.html\'\"></ng-include>\n        </div>\n      </div>\n\n      <div class=\"col col-20 hidden-xs hidden-sm text-center\" id=\"home\">\n        <div style=\"display: block; width: 100%\">\n          <div class=\"logo\"></div>\n          <small class=\"gray padding-top\">v{{$root.config.version}}</small>\n        </div>\n      </div>\n    </div>\n\n    <p class=\"visible-xs visible-sm light padding-top text-center\">\n      {{\'COMMON.APP_NAME\'|translate}} API\n      - <a href=\"#\" ng-click=\"showAboutModal($event)\">v{{$root.config.version}}</a>\n    </p>\n\n    <p class=\"hidden-xs hidden-sm gray padding-top text-center\">\n      {{\'COMMON.APP_NAME\'|translate}} API v{{$root.config.version}}\n      - <a href=\"#\" ng-click=\"showAboutModal($event)\" title=\"{{\'HOME.BTN_ABOUT\'|translate}}\">{{\'HOME.BTN_ABOUT\'|translate}}</a>\n      - <a href=\"../\" title=\"{{\'API.COMMON.LINK_STANDARD_APP_HELP\'|translate}}\">{{\'API.COMMON.LINK_STANDARD_APP\'|translate}}</a>\n    </p>\n\n  </ion-content>\n\n</ion-view>\n\n");
$templateCache.put("templates/api/locales_popover.html","<ion-popover-view class=\"fit popover-locales\" style=\"height: {{locales.length*48}}px\">\n  <ion-content scroll=\"false\">\n    <div class=\"list item-text-wrap block\">\n\n      <a ng-repeat=\"l in locales track by l.id\" class=\"item item-icon-left ink\" ng-click=\"changeLanguage(l.id)\">\n        {{l.label | translate}}\n      </a>\n\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/api/menu.html","\n<ion-nav-bar class=\"bar-dark\" title-align=\"left\">\n  <ion-nav-back-button class=\"no-text\">\n  </ion-nav-back-button>\n\n  <ion-nav-buttons side=\"left\">\n  </ion-nav-buttons>\n\n  <ion-nav-buttons side=\"right\">\n  </ion-nav-buttons>\n</ion-nav-bar>\n<ion-nav-view name=\"menuContent\"></ion-nav-view>\n\n");
$templateCache.put("templates/api/transfer.html","<ion-view class=\"circle-bg-dark\">\n\n  <ion-nav-buttons side=\"left\">\n    <button class=\"button button-clear visible-xs\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL\n    </button>\n  </ion-nav-buttons>\n\n  <ion-nav-title>\n    <span class=\"title visible-xs\" translate>API.TRANSFER.TITLE_SHORT</span>\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"right\">\n    <!-- locales -->\n    <button class=\"button button-clear hidden-xs hidden-sm gray icon-left\" style=\"max-width: 450px !important\" ng-click=\"showLocalesPopover($event)\">\n      <i class=\"icon ion-earth\"></i>\n      {{$root.settings.locale.label}}\n      <small class=\"ion-arrow-down-b\"></small>\n    </button>\n\n    <button class=\"button button-positive button-icon button-clear icon ion-android-done visible-xs\" ng-click=\"doLogin()\">\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content class=\"has-header no-padding-xs positive-900-bg\">\n\n    <br class=\"hidden-xs\">\n\n    <div class=\"row no-padding-xs\">\n      <div class=\"col col-20 hidden-xs hidden-sm\">\n        &nbsp;\n      </div>\n      <div class=\"col no-padding-xs\">\n\n        <div class=\"light-bg\">\n\n          <h2 class=\"padding-top text-center hidden-xs\" translate>API.TRANSFER.TITLE</h2>\n\n          <div class=\"no-padding energized-100-bg\" ng-if=\"demo\">\n            <div class=\"item item-icon-left item-text-wrap no-border\">\n              <i class=\"icon ion-information-circled positive\"></i>\n              <p translate>API.TRANSFER.DEMO.HELP</p>\n            </div>\n          </div>\n\n          <ng-include src=\"\'templates/login/form_login.html\'\"></ng-include>\n\n        </div>\n        <br class=\"hidden-xs\">\n        <br class=\"hidden-xs\">\n        <div class=\"list padding no-padding-xs light-bg expert-mode\">\n          <div class=\"item\">\n            <p translate>API.TRANSFER.SUMMARY</p>\n          </div>\n\n          <div class=\"item item-icon-left-padding item-tx no-border\">\n            <h2 translate>API.TRANSFER.AMOUNT</h2>\n            <div class=\"badge item-note badge-calm\" ng-bind-html=\"transferData.amount|formatAmount:{useRelative: false, currency: $root.currency.name}\"></div>\n            <div class=\"badge badge-secondary\" ng-bind-html=\"transferData.amount|formatAmount:{useRelative: true, currency: $root.currency.name}\"></div>\n          </div>\n          <div class=\"item item-icon-left-padding\" ng-if=\"transferData.name\">\n            <h2 translate>API.TRANSFER.NAME</h2>\n            <div class=\"badge item-note\">\n              {{transferData.name}}\n            </div>\n          </div>\n          <div class=\"item item-icon-left-padding item-text-wrap\">\n            <h2 translate>API.TRANSFER.PUBKEY</h2>\n            <div class=\"badge\">\n              <span class=\"hidden-xs\"><br class=\"visible-sm visible-md\"><i class=\"icon ion-key\"> </i>{{transferData.pubkey}}</span>\n              <span class=\"visible-xs\" copy-on-click=\"{{transferData.pubkey}}\"><br class=\"visible-xs\"><i class=\"icon ion-key\"></i> {{transferData.pubkey|formatPubkey}}</span>\n            </div>\n          </div>\n\n          <div class=\"item item-icon-left-padding\">\n            <h2 translate>API.TRANSFER.COMMENT</h2>\n            <div class=\"badge item-note\">\n              <span class=\"hidden-xs\"><br class=\"visible-sm visible-md\">{{transferData.comment}}</span>\n              <span class=\"visible-xs\" copy-on-click=\"{{transferData.comment}}\"><br>{{transferData.comment}}</span>\n            </div>\n          </div>\n          <!-- spacer in small screen -->\n          <div class=\"padding-bottom visible-xs\">&nbsp;</div>\n        </div>\n      </div>\n      <div class=\"col col-20 hidden-xs hidden-sm text-center\" id=\"home\">\n        <div style=\"display: block; width: 100%\">\n          <div class=\"logo text-center\"></div>\n          <small class=\"gray padding-top\">v{{$root.config.version}}</small>\n        </div>\n      </div>\n    </div>\n\n    <p class=\"visible-xs visible-sm light padding-top text-center\">\n      {{\'COMMON.APP_NAME\'|translate}}\n      - <a href=\"#\" ng-click=\"showAboutModal($event)\">v{{$root.config.version}}</a>\n    </p>\n\n    <p class=\"hidden-xs hidden-sm gray padding-top text-center\">\n      {{\'COMMON.APP_NAME\'|translate}} API v{{$root.config.version}}\n      - <a href=\"#\" ng-click=\"showAboutModal($event)\" title=\"{{\'HOME.BTN_ABOUT\'|translate}}\">{{\'HOME.BTN_ABOUT\'|translate}}</a>\n      - <a ui-sref=\"app.home\" target=\"_system\" title=\"{{\'API.COMMON.LINK_DOC_HELP\'|translate}}\">{{\'API.COMMON.LINK_DOC\'|translate}}</a>\n      - <a href=\"../\" title=\"{{\'API.COMMON.LINK_STANDARD_APP_HELP\'|translate}}\">{{\'API.COMMON.LINK_STANDARD_APP\'|translate}}</a>\n    </p>\n\n  </ion-content>\n\n</ion-view>");
$templateCache.put("templates/blockchain/item_block.html","<a name=\"block-{{:rebind:block.number}}\"></a>\n<ion-item id=\"block-{{:rebind:block.number}}\" class=\"item item-icon-left item-block {{::ionItemClass}}\" ng-class=\"{\'ink\': !block.empty||!block.compacted, \'item-block-empty\': block.empty, \'compacted\': block.compacted && compactMode}\" ng-click=\"selectBlock(block)\">\n\n  <i class=\"icon ion-cube stable\" ng-if=\":rebind:(!block.empty && !block.avatar)\"></i>\n  <i class=\"avatar\" ng-if=\":rebind:!block.empty && block.avatar\" style=\"background-image: url(\'{{:rebind:block.avatar.src}}\')\"></i>\n\n  <div class=\"row no-padding\" ng-if=\":rebind:!block.compacted || !compactMode\">\n    <div class=\"col\">\n      <h4 ng-class=\"{\'gray\': block.compacted, \'dark\': !block.compacted}\">\n        <i class=\"ion-clock\"></i>\n        {{:rebind:block.medianTime|formatDate}}\n      </h4>\n      <h4 ng-if=\"!block.empty\">\n        <!-- joiners/leavers -->\n        <ng-if ng-if=\":rebind:block.joinersCount||(block.excludedCount-block.revokedCount)\">\n          <i class=\"dark ion-person\"></i>\n          <span class=\"dark\" ng-if=\":rebind:block.joinersCount\">+{{:rebind:block.joinersCount}}</span>\n          <span class=\"dark\" ng-if=\":rebind:block.excludedCount\">-{{:rebind:block.excludedCount-block.revokedCount}}</span>\n        </ng-if>\n        <span class=\"dark\" ng-if=\":rebind:block.revokedCount\" class=\"assertive\"><i class=\"ion-minus-circled\"></i> {{:rebind:block.revokedCount}}&nbsp;&nbsp;</span>\n        <span class=\"dark\" ng-if=\":rebind:block.activesCount\" class=\"gray\"><i class=\"gray ion-refresh\"></i> {{:rebind:block.activesCount}}&nbsp;&nbsp;</span>\n        <span class=\"dark\" ng-if=\":rebind:block.certificationsCount\"><i class=\"ion-ribbon-a\"></i> {{:rebind:block.certificationsCount}}&nbsp;&nbsp;</span>\n        <span class=\"dark\" ng-if=\":rebind:block.dividend\" class=\"gray\"><i class=\"gray ion-arrow-up-c\"></i> {{\'COMMON.UD\'|translate}}&nbsp;&nbsp;</span>\n        <span class=\"dark\" ng-if=\":rebind:block.transactionsCount\"><i class=\"ion-card\"> {{:rebind:block.transactionsCount}}</i></span>\n      </h4>\n    </div>\n\n    <div class=\"col col-33 positive hidden-md\">\n      <h4><i class=\"ion-person\"></i> {{:rebind:block.name||block.uid}}</h4>\n    </div>\n\n    <div class=\"col col-20\">\n      <span class=\"badge\" ng-class=\"{\'badge-balanced\': !$index , \'badge-calm\': $index && !block.compacted && !block.empty}\">{{:rebind:block.number|formatInteger}}</span>\n    </div>\n\n  </div>\n</ion-item>\n");
$templateCache.put("templates/blockchain/item_block_empty_lg.html","<a name=\"block-{{::block.number}}\"></a>\n<div id=\"block-{{::block.number}}\" class=\"item item-block item-icon-left item-block-empty\" ng-class=\"{\'compacted\': block.compacted && compactMode}\" ng-click=\"selectBlock(block)\">\n  <div class=\"row no-padding\" ng-if=\":rebind:!block.compacted || !compactMode\">\n    <div class=\"col\">\n      <h3 class=\"gray\">\n        <i class=\"ion-clock\"></i>\n        {{:rebind:block.medianTime|formatDate}}\n      </h3>\n    </div>\n\n    <div class=\"col\">\n      <h3>\n        <span class=\"gray\" ng-if=\":rebind:expertMode\">\n          <i class=\"ion-key\"></i> {{:rebind:block.issuer|formatPubkey}}\n        </span>\n        <span class=\"positive\">\n          <i class=\"ion-person\"></i> {{:rebind:block.name||block.uid}}\n        </span>\n      </h3>\n    </div>\n\n    <div class=\"col col-20\"></div>\n\n    <div class=\"col col-20\">\n      <span class=\"badge\" ng-class=\"{\'badge-balanced\': !$index && search.type==\'last\'}\">{{block.number|formatInteger}}</span>\n    </div>\n\n  </div>\n</div>\n");
$templateCache.put("templates/blockchain/item_block_lg.html","<ion-item id=\"block-{{::block.number}}\" class=\"item item-block item-icon-left ink {{::ionItemClass}}\" ng-class=\"{{::ionItemClass}}\" ng-click=\"selectBlock(block)\">\n\n  <i class=\"icon ion-cube stable\" ng-if=\":rebind:!block.avatar\"></i>\n  <i class=\"avatar\" ng-if=\":rebind:!block.empty && block.avatar\" style=\"background-image: url(\'{{:rebind:block.avatar.src}}\')\"></i>\n\n  <div class=\"row no-padding\">\n    <div class=\"col\">\n      <h3 class=\"dark\"><i class=\"ion-clock\"></i> {{:rebind:block.medianTime|formatDate}}</h3>\n      <h4 class=\"gray\">{{:rebind:\'BLOCKCHAIN.HASH\'|translate}} {{:rebind:block.hash|formatHash}}</h4>\n    </div>\n\n    <div class=\"col\">\n      <h3>\n        <span class=\"gray\" ng-if=\":rebind:expertMode\">\n          <i class=\"ion-key\"></i> {{:rebind:block.issuer|formatPubkey}}\n        </span>\n        <span class=\"positive\">\n          <i class=\"ion-person\"></i> {{:rebind:block.name||block.uid}}\n        </span>\n      </h3>\n    </div>\n\n    <div class=\"col col-20\">\n      <small>\n        <ng-if ng-if=\":rebind:block.joinersCount||(block.excludedCount-block.revokedCount)\">\n          <i class=\"ion-person\"></i>\n          <span ng-if=\":rebind:block.joinersCount\">+{{:rebind:block.joinersCount}}</span>\n          <span ng-if=\":rebind:block.excludedCount\">-{{:rebind:block.excludedCount-block.revokedCount}}</span>\n          &nbsp;&nbsp;\n        </ng-if>\n        <span ng-if=\":rebind:block.revokedCount\" class=\"assertive\"><i class=\"ion-minus-circled\"></i> -{{:rebind:block.revokedCount}}&nbsp;&nbsp;</span>\n        <span ng-if=\":rebind:block.activesCount\"><i class=\"ion-refresh\"></i> {{:rebind:block.activesCount}}&nbsp;&nbsp;</span>\n        <span ng-if=\":rebind:block.certificationsCount\"><i class=\"ion-ribbon-a\"></i> {{:rebind:block.certificationsCount}}&nbsp;&nbsp;</span>\n        <span ng-if=\":rebind:block.dividend\"><i class=\"gray ion-arrow-up-c\"></i> {{:rebind:\'COMMON.UD\'|translate}}&nbsp;&nbsp;</span>\n        <span ng-if=\":rebind:block.transactionsCount\"><i class=\"ion-card\"> {{:rebind:block.transactionsCount}}</i>&nbsp;&nbsp;</span>\n\n      </small>\n    </div>\n\n    <div class=\"col col-20\">\n      <span class=\"badge\" ng-class=\"{\'badge-balanced\': !$index && search.type==\'last\', \'badge-calm\': ($index  || search.type!=\'last\')&& !block.compacted && !block.empty}\">{{:rebind:block.number}}</span>\n    </div>\n\n  </div>\n</ion-item>\n");
$templateCache.put("templates/blockchain/items_blocks.html","  <div class=\"center padding\" ng-if=\"search.loading\">\n    <ion-spinner icon=\"android\"></ion-spinner>\n  </div>\n\n  <ion-list class=\"animate-ripple padding padding-xs\">\n    <div class=\"padding gray\" ng-if=\"!search.loading && !search.results.length\" translate>\n      BLOCKCHAIN.LOOKUP.NO_BLOCK\n    </div>\n    <!-- blocks -->\n    <ng-repeat ng-repeat=\"block in :rebind:search.results track by block.number\" ng-include=\"\'templates/blockchain/item_block.html\'\">\n    </ng-repeat>\n  </ion-list>\n\n  <ion-infinite-scroll ng-if=\"search.hasMore\" icon=\"android\" on-infinite=\"showMore()\" distance=\"1%\">\n  </ion-infinite-scroll>\n");
$templateCache.put("templates/blockchain/link_identity.html","<a ui-sref=\"app.wot_identity({pubkey: identity.pubkey, uid: identity.uid})\">\n  <span class=\"positive\" ng-if=\"identity.uid\"><i class=\"icon ion-person\"></i> {{::identity.uid}}&nbsp;</span>\n  <span class=\"gray\" ng-class=\"{\'hidden-xs hidden-sm\': identity.uid}\"><i class=\"icon ion-key\"></i>&nbsp;{{::identity.pubkey|formatPubkey}}</span>\n</a>\n");
$templateCache.put("templates/blockchain/list_blocks.html","\n  <div class=\"center padding\" ng-if=\"search.loading\">\n    <ion-spinner icon=\"android\"></ion-spinner>\n  </div>\n\n  <ion-list class=\"padding padding-xs list-blocks\" ng-class=\"::motion.ionListClass\">\n    <div class=\"padding gray\" ng-if=\"!search.loading && !search.results.length\" translate>\n      BLOCKCHAIN.LOOKUP.NO_BLOCK\n    </div>\n    <!-- blocks -->\n    <ng-repeat ng-repeat=\"block in :rebind:search.results track by block.number\" ng-include=\"\'templates/blockchain/item_block.html\'\">\n    </ng-repeat>\n  </ion-list>\n\n  <ion-infinite-scroll ng-if=\"search.hasMore\" spinner=\"android\" on-infinite=\"showMore()\" distance=\"1%\">\n  </ion-infinite-scroll>\n");
$templateCache.put("templates/blockchain/list_blocks_lg.html","  <div class=\"padding padding-xs\" style=\"display: block; height: 100px\">\n    <h4 translate>BLOCKCHAIN.LOOKUP.LAST_BLOCKS</h4>\n\n    <div class=\"pull-right\">\n      <a class=\"button button-text button-small ink\" ng-class=\"{\'button-text-positive\': compactMode, \'button-text-stable\': !compactMode}\" ng-click=\"toggleCompactMode()\">\n        <i class=\"icon ion-navicon\"></i>\n        <b class=\"ion-arrow-down-b\" style=\"position: absolute; top: -2px; left: 4px; font-size: 8px\"></b>\n        <b class=\"ion-arrow-up-b\" style=\"position: absolute; top: 10px; left: 4px; font-size: 8px\"></b>\n        <span>{{\'BLOCKCHAIN.LOOKUP.BTN_COMPACT\'|translate}}</span>\n      </a>\n    </div>\n  </div>\n\n  <div class=\"center padding\" ng-if=\"search.loading\">\n    <ion-spinner icon=\"android\"></ion-spinner>\n  </div>\n\n  <ion-list class=\"padding padding-xs list-blocks\" ng-class=\"::motion.ionListClass\">\n    <div class=\"padding gray\" ng-if=\"!search.loading && !search.results.length\" translate>\n      BLOCKCHAIN.LOOKUP.NO_BLOCK\n    </div>\n    <!-- blocks -->\n    <ng-repeat ng-repeat=\"block in search.results\" ng-include=\"!block.empty ? \'templates/blockchain/item_block_lg.html\' : \'templates/blockchain/item_block_empty_lg.html\'\">\n    </ng-repeat>\n  </ion-list>\n\n  <ion-infinite-scroll ng-if=\"search.hasMore\" spinner=\"android\" on-infinite=\"showMore()\" distance=\"1%\">\n  </ion-infinite-scroll>\n");
$templateCache.put("templates/blockchain/lookup.html","<ion-view>\n  <ion-nav-title>\n    <span translate>BLOCKCHAIN.LOOKUP.TITLE</span>\n  </ion-nav-title>\n\n\n  <ion-content class=\"padding no-padding-xs\" scroll=\"true\">\n    <ng-include src=\"\'templates/blockchain/list_blocks.html\'\"></ng-include>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/blockchain/lookup_lg.html","<ion-view>\n  <ion-nav-title>\n    <span translate>BLOCKCHAIN.LOOKUP.TITLE</span>\n  </ion-nav-title>\n\n  <ion-content class=\"padding no-padding-xs\" scroll=\"true\">\n    <ng-include src=\"\'templates/blockchain/list_blocks_lg.html\'\"></ng-include>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/blockchain/unlock_condition_popover.html","<ion-popover-view class=\"fit\">\n  <ion-header-bar>\n    <h1 class=\"title\" translate>BLOCKCHAIN.VIEW.TX_OUTPUT_UNLOCK_CONDITIONS</h1>\n  </ion-header-bar>\n  <ion-content scroll=\"true\">\n    <div class=\"row\" ng-repeat=\"condition in popoverData.unlockConditions track by $index\" ng-style=\"::condition.style\">\n      <span class=\"gray\" ng-if=\"::condition.operator\">{{::\'BLOCKCHAIN.VIEW.TX_OUTPUT_OPERATOR.\'+condition.operator|translate}}&nbsp;</span>\n      <div ng-if=\"::condition.type==\'SIG\'\">\n          <i class=\"icon ion-key dark\"></i>\n        <span class=\"dark\" ng-bind-html=\"::\'BLOCKCHAIN.VIEW.TX_OUTPUT_FUNCTION.SIG\' | translate\"></span>\n        <a ng-click=\"goState(\'app.wot_identity\', {pubkey:condition.value})\" style=\"text-decoration: none\" class=\"positive\">\n          {{condition.value|formatPubkey}}\n        </a>\n      </div>\n      <div ng-if=\"::condition.type==\'XHX\'\">\n        <i class=\"icon ion-lock-combination dark\"></i>\n        <span class=\"dark\" ng-bind-html=\"::\'BLOCKCHAIN.VIEW.TX_OUTPUT_FUNCTION.XHX\' | translate\"></span>\n        <a copy-on-click=\"{{::condition.value}}\" class=\"positive\">\n          {{::condition.value|formatPubkey}}...\n        </a>\n      </div>\n      <div ng-if=\"condition.type==\'CSV\'\">\n        <i class=\"icon ion-clock dark\"></i>\n        <span class=\"dark\" ng-bind-html=\"::\'BLOCKCHAIN.VIEW.TX_OUTPUT_FUNCTION.CSV\' | translate\"></span>\n        {{::condition.value|formatDuration}}\n      </div>\n      <div ng-if=\"condition.type==\'CLTV\'\">\n        <i class=\"icon ion-clock dark\"></i>\n        <span class=\"dark\" ng-bind-html=\"::\'BLOCKCHAIN.VIEW.TX_OUTPUT_FUNCTION.CLTV\' | translate\"></span>\n        {{::condition.value|formatDate}}\n      </div>\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/blockchain/view_block.html","<ion-view>\n  <ion-nav-title>\n    <span class=\"title visible-xs visible-sm\" ng-if=\"number==\'current\'\">{{\'BLOCKCHAIN.VIEW.TITLE_CURRENT\'|translate}}</span>\n    <span class=\"title visible-xs visible-sm\" ng-if=\"number!=\'current\'\">{{\'BLOCKCHAIN.VIEW.TITLE\'|translate:formData}}</span>\n  </ion-nav-title>\n\n  <ion-content class=\"no-padding-xs\" scroll=\"true\">\n\n    <div class=\"row no-padding\">\n      <div class=\"col col-15 hidden-xs hidden-sm\">&nbsp;</div>\n\n      <div class=\"col no-padding\">\n\n        <div class=\"center padding\" ng-if=\"loading\">\n          <ion-spinner icon=\"android\"></ion-spinner>\n        </div>\n\n        <!-- animate-fade-slide-in -->\n        <ion-list class=\"item-text-wrap no-padding-xs\" ng-if=\"!loading\">\n\n          <!-- header -->\n          <div class=\"item item-text-wrap\">\n\n            <h1 class=\"padding-top hidden-xs hidden-sm\">\n              <span ng-if=\"number!=\'current\'\">{{\'BLOCKCHAIN.VIEW.TITLE\'|translate:formData}}</span>\n              <span ng-if=\"number==\'current\'\">{{\'BLOCKCHAIN.VIEW.TITLE_CURRENT\'|translate}}</span>\n            </h1>\n\n            <h3>\n              <span class=\"dark\">\n                <i class=\"icon ion-clock\"></i>\n                {{formData.medianTime | formatDate}}\n              </span>\n              <span class=\"gray\">\n                |\n                {{formData.medianTime | formatFromNow}}\n              </span>\n            </h3>\n\n            <h3>\n              <span class=\"dark\">\n                <i class=\"icon ion-lock-combination\"></i>\n                {{\'BLOCKCHAIN.VIEW.COMPUTED_BY\'|translate}}\n              </span>\n              <a class=\"positive\" ui-sref=\"app.wot_identity({pubkey:issuer.pubkey, uid: issuer.uid})\">\n                <i class=\"icon ion-person positive\"></i>\n                {{issuer.name||issuer.uid}}\n                <span class=\"gray\" ng-if=\"issuer.name\">\n                  ({{issuer.uid}})\n                </span>\n              </a>\n            </h3>\n\n            <h3>\n              <a ng-click=\"openRawBlock($event)\">\n                <i class=\"icon ion-share\"></i> {{\'BLOCKCHAIN.VIEW.SHOW_RAW\'|translate}}\n              </a>\n            </h3>\n\n          </div>\n\n          <!-- button bar-->\n          <div class=\"item hidden-xs hidden-sm padding text-center\">\n            <div class=\"pull-right\">\n              <a class=\"button button-text button-small ink\" ng-class=\"{\'button-text-positive\': compactMode, \'button-text-stable\': !compactMode}\" ng-click=\"toggleCompactMode()\">\n                <i class=\"icon ion-navicon\"></i>\n                <b class=\"ion-arrow-down-b\" style=\"position: absolute; top: -2px; left: 4px; font-size: 8px\"></b>\n                <b class=\"ion-arrow-up-b\" style=\"position: absolute; top: 10px; left: 4px; font-size: 8px\"></b>\n                <span>{{\'BLOCKCHAIN.LOOKUP.BTN_COMPACT\'|translate}}</span>\n              </a>\n            </div>\n\n            <!-- Allow extension here -->\n            <cs-extension-point name=\"buttons\"></cs-extension-point>\n          </div>\n\n          <span class=\"item item-divider\">\n            {{\'BLOCKCHAIN.VIEW.TECHNICAL_DIVIDER\' | translate}}\n          </span>\n\n          <!-- protocole -->\n          <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"!compactMode || $root.settings.expertMode\">\n            <i class=\"icon ion-gear-b\"></i>\n            {{\'BLOCKCHAIN.VIEW.VERSION\'|translate}}\n            <span class=\"badge badge-stable\">\n              {{::formData.version}}\n            </span>\n          </ion-item>\n\n          <!-- difficulty -->\n          <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"!compactMode || $root.settings.expertMode\" copy-on-click=\"{{::formData.powMin}}\">\n            <i class=\"icon ion-lock-combination\"></i>\n            {{\'BLOCKCHAIN.VIEW.POW_MIN\'|translate}}\n            <h4 class=\"gray\">{{\'BLOCKCHAIN.VIEW.POW_MIN_HELP\'|translate}}</h4>\n            <span class=\"badge badge-stable\">\n              {{::formData.powMin}}\n            </span>\n          </ion-item>\n\n          <!-- hash -->\n          <ion-item class=\"item-icon-left item-text-wrap\" copy-on-click=\"{{::formData.hash}}\">\n            <i class=\"icon ion-pound\"></i>\n            {{\'BLOCKCHAIN.VIEW.HASH\'|translate}}\n            <span class=\"item-note hidden-xs hidden-sm dark\">\n              {{::formData.hash}}\n            </span>\n            <h5 class=\"visible-xs visible-sm dark\">\n              {{::formData.hash}}\n            </h5>\n          </ion-item>\n\n          <!-- spacer -->\n          <ion-item class=\"hidden-sm hidden-xs\"></ion-item>\n\n          <span class=\"item item-divider\">\n            {{\'BLOCKCHAIN.VIEW.DATA_DIVIDER\' | translate}}\n          </span>\n\n          <!-- If Empty & compact -->\n          <ion-item ng-if=\"compactMode && formData.empty\" class=\"item-icon-left item-text-wrap\">\n            {{\'BLOCKCHAIN.VIEW.EMPTY\'|translate}}\n          </ion-item>\n\n          <!-- DU -->\n          <ion-item ng-if=\"!compactMode || formData.dividend\" class=\"item-icon-left item-text-wrap\" copy-on-click=\"{{::formData.dividend/100}}\">\n            <i class=\"icon ion-arrow-up-c\"></i>\n            <div class=\"col col-60\">\n              {{\'COMMON.UNIVERSAL_DIVIDEND\'|translate}}\n              <h4 class=\"gray\">{{\'BLOCKCHAIN.VIEW.UNIVERSAL_DIVIDEND_HELP\'|translate: {membersCount: formData.membersCount} }}</h4>\n            </div>\n            <span class=\"badge badge-balanced\" ng-if=\"formData.dividend\">\n              +1 <span ng-bind-html=\"formData.currency|currencySymbol: {useRelative: true} \"></span> / {{\'COMMON.MEMBER\'|translate|lowercase}}\n            </span>\n            <span class=\"badge badge-stable\" ng-if=\"!formData.dividend\">0</span>\n            <span class=\"badge badge-secondary\" ng-if=\"formData.dividend\">+ {{formData.dividend| formatAmount: {currency: formData.currency, useRelative: false} }} / {{\'COMMON.MEMBER\'|translate|lowercase}}</span>\n          </ion-item>\n\n          <!-- identities -->\n          <ng-if ng-if=\"!compactMode || formData.identitiesCount\">\n            <ion-item class=\"item-icon-left\">\n              <i class=\"icon ion-person\"></i>\n              <b class=\"ion-clock\" style=\"position: absolute; top: 16px; left: 39px; font-size: 12px\"></b>\n              {{\'BLOCKCHAIN.VIEW.IDENTITIES_COUNT\'|translate}}\n              <span class=\"badge badge-balanced\" ng-if=\"formData.identitiesCount\">+{{::formData.identitiesCount}}</span>\n              <span class=\"badge badge-stable\" ng-if=\"!formData.identitiesCount\">0</span>\n            </ion-item>\n\n            <div class=\"padding-bottom item-icon-left-padding item-icon-right-padding\" ng-if=\"formData.identitiesCount\">\n              <ion-item ng-repeat=\"identity in ::formData.identities\" class=\"item-border-large item-small-height\" ng-include=\"\'templates/blockchain/link_identity.html\'\">\n              </ion-item>\n            </div>\n          </ng-if>\n\n          <!-- joiners -->\n          <ng-if ng-if=\"!compactMode || formData.joinersCount\">\n            <ion-item class=\"item-icon-left\">\n              <i class=\"icon ion-person-add\"></i>\n              {{\'BLOCKCHAIN.VIEW.JOINERS_COUNT\'|translate}}\n              <span class=\"badge badge-balanced\" ng-if=\"formData.joinersCount\">+{{::formData.joinersCount}}</span>\n              <span class=\"badge badge-stable\" ng-if=\"!formData.joinersCount\">0</span>\n            </ion-item>\n\n            <div class=\"padding-bottom item-icon-left-padding item-icon-right-padding\" ng-if=\"formData.joinersCount\">\n              <ion-item ng-repeat=\"identity in ::formData.joiners\" class=\"item-border-large item-small-height\" ng-include=\"\'templates/blockchain/link_identity.html\'\">\n              </ion-item>\n            </div>\n          </ng-if>\n\n          <!-- actives -->\n          <ng-if ng-if=\"!compactMode || formData.activesCount\">\n            <ion-item class=\"item-icon-left\">\n              <i class=\"icon ion-person\"></i>\n              <b class=\"ion-refresh\" style=\"position: absolute; top: 25px; left: 39px; font-size: 12px\"></b>\n              {{\'BLOCKCHAIN.VIEW.ACTIVES_COUNT\'|translate}}\n              <h4 class=\"gray\">{{\'BLOCKCHAIN.VIEW.ACTIVES_COUNT_HELP\'|translate}}</h4>\n              <span class=\"badge badge-balanced\" ng-if=\"formData.activesCount\">{{::formData.activesCount}}</span>\n              <span class=\"badge badge-stable\" ng-if=\"!formData.activesCount\">0</span>\n            </ion-item>\n\n            <div class=\"padding-bottom item-icon-left-padding item-icon-right-padding\" ng-if=\"formData.activesCount\">\n              <ion-item ng-repeat=\"identity in ::formData.actives\" class=\"item-border-large item-small-height\" ng-include=\"\'templates/blockchain/link_identity.html\'\">\n              </ion-item>\n            </div>\n          </ng-if>\n\n          <!-- excluded -->\n          <ng-if ng-if=\"!compactMode || (formData.excludedCount-formData.revokedCount)\">\n            <ion-item class=\"item-icon-left\">\n              <i class=\"icon ion-person\"></i>\n              <b class=\"ion-close dark\" style=\"position: absolute; top: 25px; left: 39px; font-size: 12px\"></b>\n              {{\'BLOCKCHAIN.VIEW.EXCLUDED_COUNT\'|translate}}\n              <h4 class=\"gray\">{{\'BLOCKCHAIN.VIEW.EXCLUDED_COUNT_HELP\'|translate}}</h4>\n              <span class=\"badge badge-assertive\" ng-if=\"formData.excludedCount-formData.revokedCount\">-{{::formData.excludedCount-formData.revokedCount}}</span>\n              <span class=\"badge badge-stable\" ng-if=\"!(formData.excludedCount-formData.revokedCount)\">0</span>\n            </ion-item>\n\n            <div class=\"padding-bottom item-icon-left-padding item-icon-right-padding\" ng-if=\"formData.excludedCount\">\n              <ion-item ng-repeat=\"identity in ::formData.excluded\" class=\"item-border-large item-small-height\" ng-include=\"\'templates/blockchain/link_identity.html\'\">\n              </ion-item>\n            </div>\n          </ng-if>\n\n          <!-- leavers -->\n          <ng-if ng-if=\"!compactMode || formData.leaversCount\">\n            <ion-item class=\"item-icon-left\" ng-if=\"!compactMode || formData.leaversCount\">\n              <i class=\"icon ion-person\"></i>\n              <b class=\"ion-minus\" style=\"position: absolute; top: 25px; left: 39px; font-size: 12px\"></b>\n              {{\'BLOCKCHAIN.VIEW.LEAVERS_COUNT\'|translate}}\n              <h4 class=\"gray\">{{\'BLOCKCHAIN.VIEW.LEAVERS_COUNT_HELP\'|translate}}</h4>\n              <span class=\"badge badge-assertive\" ng-if=\"formData.leaversCount\">-{{::formData.leaversCount}}</span>\n              <span class=\"badge badge-stable\" ng-if=\"!formData.leaversCount\">0</span>\n            </ion-item>\n\n            <div class=\"padding-bottom item-icon-left-padding item-icon-right-padding\" ng-if=\"formData.leaversCount\">\n              <ion-item ng-repeat=\"identity in ::formData.leavers\" class=\"item-border-large item-small-height\" ng-include=\"\'templates/blockchain/link_identity.html\'\">\n              </ion-item>\n            </div>\n          </ng-if>\n\n          <!-- revoked -->\n          <ng-if ng-if=\"!compactMode || formData.revokedCount\">\n            <ion-item class=\"item-icon-left\">\n              <i class=\"icon ion-person\"></i>\n              <b class=\"ion-minus-circled assertive\" style=\"position: absolute; top: 25px; left: 39px; font-size: 12px\"></b>\n              {{\'BLOCKCHAIN.VIEW.REVOKED_COUNT\'|translate}}\n              <h4 class=\"gray\">{{\'BLOCKCHAIN.VIEW.REVOKED_COUNT_HELP\'|translate}}</h4>\n              <span class=\"badge badge-balanced\" ng-if=\"formData.revokedCount\">-{{::formData.revokedCount}}</span>\n              <span class=\"badge badge-stable\" ng-if=\"!formData.revokedCount\">0</span>\n            </ion-item>\n\n            <div class=\"padding-bottom item-icon-left-padding item-icon-right-padding\" ng-if=\"formData.revokedCount\">\n              <ion-item ng-repeat=\"identity in ::formData.revoked\" class=\"item-border-large item-small-height\" ng-include=\"\'templates/blockchain/link_identity.html\'\">\n              </ion-item>\n            </div>\n          </ng-if>\n\n          <!-- cert -->\n          <ng-if ng-if=\"!compactMode || formData.certificationsCount\">\n            <ion-item class=\"item-icon-left\">\n              <i class=\"icon ion-ribbon-a\"></i>\n              {{\'BLOCKCHAIN.VIEW.CERT_COUNT\'|translate}}\n              <span class=\"badge badge-stable\" ng-class=\"{\'badge-positive\':formData.certificationsCount}\">{{::formData.certificationsCount}}</span>\n            </ion-item>\n\n            <div class=\"padding-bottom item-icon-left-padding item-icon-right-padding no-padding-xs\" ng-if=\"formData.certificationsCount\">\n              <div ng-repeat=\"(key, certs) in formData.certifications\" class=\"item item-border-large item-small-height\">\n                <div class=\"row no-padding\">\n                  <div class=\"col col-center no-padding\">\n                    <ng-repeat ng-repeat=\"cert in certs\">\n                      <ng-include src=\"\'templates/blockchain/link_identity.html\'\" onload=\"identity=cert.from\"></ng-include>\n                      <br>\n                    </ng-repeat>\n                  </div>\n                  <div class=\"col col-10 col-center gray text-center no-padding\">\n                    <h2><i class=\"icon ion-arrow-right-a\"></i></h2>\n                  </div>\n                  <div class=\"col col-40 col-center no-padding\" ng-include=\"\'templates/blockchain/link_identity.html\'\" onload=\"identity=certs[0].to\">\n                  </div>\n                </div>\n              </div>\n            </div>\n          </ng-if>\n\n          <!-- TX -->\n          <ng-if ng-if=\"!compactMode || formData.transactionsCount\">\n            <ion-item class=\"item-icon-left\">\n              <i class=\"icon ion-card\"></i>\n              {{\'BLOCKCHAIN.VIEW.TX_COUNT\'|translate}}\n              <span class=\"badge badge-stable\" ng-class=\"{\'badge-positive\':formData.transactionsCount}\">{{::formData.transactionsCount}}</span>\n            </ion-item>\n\n            <div class=\"padding-bottom item-icon-left-padding item-icon-right-padding no-padding-xs\" ng-if=\"formData.transactionsCount\">\n              <div ng-repeat=\"tx in ::formData.transactions\" class=\"item item-small-height item-border-large\">\n                <div class=\"row no-padding\" style=\"padding-top: 3px\">\n                  <div class=\"col col-40 col-center no-padding list no-margin\">\n                    <div ng-repeat=\"identity in ::tx.issuers\" class=\"item no-padding item-small-height\">\n                      <ng-include src=\"\'templates/blockchain/link_identity.html\'\"></ng-include>\n                    </div>\n                  </div>\n                  <div class=\"col col-10 col-center gray text-center no-padding\">\n                    <h2><i class=\"icon ion-arrow-right-a\"></i></h2>\n                  </div>\n                  <!-- recipients -->\n                  <div class=\"col no-padding padding-right no-padding-xs col-text-wrap list no-margin\">\n                    <span class=\"gray\" class=\"gray\" ng-if=\"tx.toHimself\" translate>BLOCKCHAIN.VIEW.TX_TO_HIMSELF</span>\n                    <div ng-repeat=\"output in ::tx.outputs\" class=\"item no-padding item-small-height\">\n\n                      <!-- simple SIG expression -->\n                      <ng-include ng-if=\"::output.pubkey\" src=\"\'templates/blockchain/link_identity.html\'\" onload=\"identity=output\"></ng-include>\n\n                      <!-- complex unlock condition -->\n                      <span ng-if=\"::!output.pubkey && output.unlockFunctions\">\n                        <i class=\"icon ion-locked\"></i>\n                        (<a ng-click=\"showUnlockConditionPopover(output, $event)\">\n                          <i ng-repeat=\"unlockFunction in ::output.unlockFunctions\" ng-class=\"::{\'ion-key\': (unlockFunction==\'SIG\'), \'ion-clock\': (unlockFunction==\'CSV\' || unlockFunction==\'CLTV\'), \'ion-lock-combination\': (unlockFunction==\'XHX\') }\" class=\"icon\"></i>\n                        </a>)\n                      </span>\n                      <span class=\"badge badge-balanced\" ng-bind-html=\"::output.amount | formatAmount:{currency: formData.currency, useRelative: false} \"></span>\n                    </div>\n                  </div>\n                </div>\n              </div>\n            </div>\n          </ng-if>\n\n        </ion-list>\n      </div>\n\n      <div class=\"col col-15 hidden-xs\"></div>\n    </div>\n\n  </ion-content>\n\n</ion-view>\n");
$templateCache.put("templates/common/badge_certification_count.html","<span ng-attr-id=\"{{$ctrl.csId}}\" class=\"badge badge-balanced\" ng-class=\"{\'badge-energized\': $ctrl.requirements.willNeedCertificationCount || ($ctrl.requirements.needCertificationCount + $ctrl.requirements.pendingCertificationCount >= $ctrl.parameters.sigQty),\n               \'badge-assertive\': ($ctrl.requirements.needCertificationCount + $ctrl.requirements.pendingCertificationCount < $ctrl.parameters.sigQty)}\">\n  <span ng-if=\"$ctrl.requirements.certificationCount || !$ctrl.requirements.pendingCertificationCount\">\n    <i ng-if=\"!$ctrl.requirements.needCertificationCount\" class=\"ion-android-done\"></i>\n    {{$ctrl.requirements.certificationCount}}\n    <i ng-if=\"$ctrl.requirements.willNeedCertificationCount\" class=\"ion-android-warning\"></i>\n  </span>\n  <span ng-if=\"$ctrl.requirements.pendingCertificationCount\">\n    <ng-if ng-if=\"$ctrl.requirements.certificationCount\">+ </ng-if>\n    <i class=\"ion-clock\"></i>\n    {{$ctrl.requirements.pendingCertificationCount}}\n  </span>\n</span>\n");
$templateCache.put("templates/common/badge_given_certification_count.html","<div ng-attr-id=\"{{$ctrl.csId}}\" class=\"badge badge-calm\" ng-class=\"{\'badge-assertive\': $ctrl.identity.given_cert.length >= $ctrl.parameters.sigStock}\">\n        <span>\n          <i ng-if=\"$ctrl.identity.given_cert.length\" class=\"ion-android-done\"></i>\n          {{$ctrl.identity.given_cert.length}}\n        </span>\n  <span ng-if=\"$ctrl.identity.given_cert_pending.length\">\n          (<ng-if ng-if=\"$ctrl.identity.given_cert.length\">+ </ng-if>\n          <i class=\"ion-clock\"></i>\n          {{$ctrl.identity.given_cert_pending.length}})\n        </span>\n  <small>/ {{$ctrl.parameters.sigStock}}</small>\n</div>\n");
$templateCache.put("templates/common/form_error_messages.html","  <div class=\"form-error\" ng-message=\"minlength\">\n    <span translate=\"ERROR.FIELD_TOO_SHORT\"></span>\n  </div>\n  <div class=\"form-error\" ng-message=\"maxlength\">\n    <span translate=\"ERROR.FIELD_TOO_LONG\"></span>\n  </div>\n  <div class=\"form-error\" ng-message=\"pattern\">\n    <span translate=\"ERROR.FIELD_ACCENT\"></span>\n  </div>\n  <div class=\"form-error\" ng-message=\"required\">\n    <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n  </div>\n");
$templateCache.put("templates/common/popover_copy.html","<ion-popover-view class=\"popover-copy\" style=\"height: {{(!rows || rows &lt;= 1) ? 50 : rows*22}}px\">\n  <ion-content scroll=\"false\">\n    <div class=\"list\">\n      <div class=\"item item-input\">\n        <input type=\"text\" ng-if=\"!rows || rows &lt;= 1\" ng-model=\"value\">\n        <textarea ng-if=\"rows && rows > 1\" ng-model=\"value\" rows=\"{{rows}}\" cols=\"10\">\n      </textarea></div>\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/common/popover_helptip.html","<ion-popover-view class=\"popover-helptip\">\n  <ion-content scroll=\"false\" class=\"list\">\n    <p>\n      <i ng-if=\"icon.position && !icon.position.startsWith(\'bottom-\')\" class=\"{{icon.class}} icon-{{icon.position}} hidden-xs\" style=\"{{icon.style}}\"></i>\n\n      <!-- close button-->\n      <a ng-click=\"closePopover()\" class=\"pull-right button-close\" ng-class=\"{\'pull-left\': icon.position === \'right\', \'pull-right\': icon.position !== \'right\'}\">\n        <i class=\"ion-close\"></i>\n      </a>\n\n      <span>&nbsp;</span>\n    </p>\n\n    <p class=\"padding light\">\n      <ng-bind-html ng-bind-html=\"content | translate:contentParams\"></ng-bind-html>\n      <ng-bind-html ng-bind-html=\"trustContent\"></ng-bind-html>\n    </p>\n\n    <!-- buttons (if helptip) -->\n    <div class=\"text-center\" ng-if=\"!tour\">\n      <button class=\"button button-small button-stable\" ng-if=\"!hasNext\" ng-click=\"closePopover(true)\" translate>COMMON.BTN_UNDERSTOOD</button>\n      <button class=\"button button-small button-stable\" id=\"helptip-btn-ok\" ng-if=\"hasNext\" ng-click=\"closePopover(false)\" translate>COMMON.BTN_UNDERSTOOD</button>\n      <button id=\"helptip-btn-ok\" class=\"button button-small button-positive icon-right ink\" ng-if=\"hasNext\" ng-click=\"closePopover(true)\">\n              <i class=\"icon ion-chevron-right\"></i>\n      </button>\n    </div>\n\n    <!-- buttons (if feature tour) -->\n    <div class=\"text-center\" ng-if=\"tour\">\n      <button class=\"button button-small button-positive\" id=\"helptip-btn-ok\" ng-if=\"!hasNext\" ng-click=\"closePopover(false)\" translate>COMMON.BTN_CLOSE</button>\n      <button id=\"helptip-btn-ok\" class=\"button button-small button-positive icon-right ink\" ng-if=\"hasNext\" ng-click=\"closePopover(true)\">\n        {{\'COMMON.BTN_CONTINUE\'|translate}}\n        <i class=\"icon ion-chevron-right\"></i>\n      </button>\n    </div>\n\n    <p>\n      <i ng-if=\"icon.position && icon.position.startsWith(\'bottom-\')\" class=\"{{icon.class}} icon-{{icon.position}} hidden-xs\"></i>\n    </p>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/common/popover_profile.html","<ion-popover-view class=\"fit has-header popover-profile hidden-xs hidden-sm\">\n  <ion-content scroll=\"false\">\n    <div class=\"row\">\n      <div class=\"col col-33\">\n        <i class=\"avatar avatar-member\" ng-if=\"!$root.walletData.avatar\" ng-class=\"{\'royal-bg\': login, \'stable-bg\': !login}\"></i>\n        <i class=\"avatar\" ng-if=\"$root.walletData.avatar\" style=\"background-image: url(\'{{$root.walletData.avatar.src}}\')\"></i>\n      </div>\n      <div class=\"col col-66\" ng-if=\"login\">\n        <h4>{{$root.walletData.name||$root.walletData.uid}}</h4>\n        <h4 class=\"gray\" ng-if=\"!$root.walletData.name && !$root.walletData.uid\" copy-on-click=\"{{$root.walletData.pubkey}}\">\n          <i class=\"icon ion-key\"></i> {{$root.walletData.pubkey|formatPubkey}}\n        </h4>\n        <span class=\"gray\" ng-if=\"$root.walletData.name||$root.walletData.uid\" copy-on-click=\"{{$root.walletData.pubkey}}\">\n          <i class=\"icon ion-key\"></i> {{$root.walletData.pubkey|formatPubkey}}\n        </span>\n      </div>\n    </div>\n\n    <div class=\"row\" ng-show=\"login\">\n      <div class=\"col col-66 col-offset-33\">\n\n        <!-- auth -->\n        <button ng-show=\"!auth\" class=\"button button-assertive button-small ink\" ng-click=\"doAuth()\">\n          {{\'AUTH.BTN_AUTH\' | translate}}\n        </button>\n\n        <!-- Allow extension here -->\n        <cs-extension-point name=\"profile-popover-user\"></cs-extension-point>\n      </div>\n    </div>\n\n    <div class=\"row\" ng-show=\"!login\">\n      <div class=\"col col-66 col-offset-33\">\n        <div class=\"text-center no-padding gray\">\n          {{\'LOGIN.NO_ACCOUNT_QUESTION\'|translate}}\n          <br class=\"visible-xs\">\n          <b>\n            <button class=\"button button-calm button-small ink\" ng-click=\"showJoinModal()\">\n              {{\'LOGIN.CREATE_ACCOUNT\'|translate}}\n            </button>\n          </b>\n        </div>\n      </div>\n    </div>\n  </ion-content>\n  <ion-footer-bar class=\"stable-bg row\">\n    <div class=\"col\">\n      <!-- settings -->\n      <button class=\"button button-raised button-block button-stable ink ink-dark\" id=\"helptip-popover-profile-btn-settings\" ng-click=\"showSettings()\" ui-sref=\"app.settings\">\n        <i class=\"icon ion-android-settings\"></i>\n        {{\'MENU.SETTINGS\' | translate}}\n      </button>\n    </div>\n    <div class=\"col\">\n      <button class=\"button button-raised button-block button-stable ink ink-dark\" ng-show=\"login\" ng-click=\"logout()\" translate>COMMON.BTN_LOGOUT</button>\n      <button class=\"button button-raised button-block button-positive ink\" ng-show=\"!login\" ng-click=\"loginAndGo(\'app.view_wallet\')\" translate>COMMON.BTN_LOGIN</button>\n    </div>\n  </ion-footer-bar>\n</ion-popover-view>\n");
$templateCache.put("templates/common/popover_share.html","<ion-popover-view class=\"popover-share\">\n  <ion-content scroll=\"false\">\n    <div class=\"bar bar-header\">\n      <h1 class=\"title\">{{titleKey|translate:titleValues}}</h1>\n      <span class=\"gray pull-right\">{{time|formatDate}}</span>\n    </div>\n    <div class=\"list no-margin no-padding has-header has-footer block\">\n      <div class=\"item item-input\">\n        <input type=\"text\" ng-model=\"value\">\n      </div>\n    </div>\n\n    <div class=\"bar bar-footer\">\n      <div class=\"button-bar\">\n\n        <a class=\"button button-icon positive icon ion-social-facebook\" href=\"https://www.facebook.com/sharer/sharer.php?u={{postUrl|formatEncodeURI}}&amp;title={{postMessage|formatEncodeURI}}\" onclick=\"window.open(this.href, \'facebook-share\',\'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,width=580,height=296\');return false;\" title=\"{{\'COMMON.POPOVER_SHARE.SHARE_ON_FACEBOOK\'|translate}}\">\n        </a>\n\n        <a class=\"button button-icon positive icon ion-social-twitter\" href=\"https://twitter.com/intent/tweet?url={{postUrl|formatEncodeURI}}&amp;text={{postMessage|formatEncodeURI}}\" onclick=\"window.open(this.href, \'twitter-share\',\'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,width=580,height=296\');return false;\" title=\"{{\'COMMON.POPOVER_SHARE.SHARE_ON_TWITTER\'|translate}}\">\n        </a>\n\n        <a class=\"button button-icon positive icon ion-social-googleplus\" href=\"https://plus.google.com/share?url={{postUrl|formatEncodeURI}}\" onclick=\"window.open(this.href, \'google-plus-share\', \'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=296,width=580\');return false;\" title=\"{{\'COMMON.POPOVER_SHARE.SHARE_ON_GOOGLEPLUS\'|translate}}\">\n        </a>\n\n        <a class=\"button button-icon positive icon ion-social-diaspora\" href=\"https://sharetodiaspora.github.io/?title={{postMessage|formatEncodeURI}}&amp;url={{postUrl|formatEncodeURI}}\" onclick=\"window.open(this.href, \'diaspora-share\',\'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,width=580,height=296\');return false;\" title=\"{{\'COMMON.POPOVER_SHARE.SHARE_ON_DIASPORA\'|translate}}\">\n        </a>\n\n        <a class=\"button-close\" title=\"{{\'COMMON.BTN_CLOSE\'|translate}}\" ng-click=\"closePopover()\">\n          <i class=\"icon ion-close\"></i>\n        </a>\n        </div>\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/common/view_passcode.html","<ion-view left-buttons=\"leftButtons\">\n  <ion-nav-title>\n    <span class=\"visible-xs visible-sm\" translate>COMMON.PASSCODE.TITLE</span>\n  </ion-nav-title>\n\n  <ion-content scroll=\"false\">\n\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/currency/items_network.html","\n  <ion-item id=\"helptip-network-blockchain\" class=\"item-icon-left item-text-wrap\">\n    <i class=\"icon ion-clock\"></i>\n    <span class=\"col col-60\" translate>CURRENCY.VIEW.MEDIAN_TIME</span>\n    <span class=\"badge badge-stable\">{{formData.medianTime | formatDate}}</span>\n  </ion-item>\n\n\n  <ion-item class=\"item-icon-left item-text-wrap\">\n    <i class=\"icon ion-lock-combination\"></i>\n    <span class=\"col col-75\" translate>CURRENCY.VIEW.POW_MIN</span>\n    <span class=\"badge badge-stable\">{{formData.difficulty | formatInteger}}</span>\n  </ion-item>\n\n  <!-- Allow extension here -->\n  <cs-extension-point name=\"network-actual\"></cs-extension-point>\n\n  <div class=\"item item-divider\">\n    <span translate>CURRENCY.VIEW.NETWORK_RULES_DIVIDER</span>\n  </div>\n\n  <ion-item class=\"item-icon-left item-text-wrap\">\n    <i class=\"icon ion-clock\" style=\"position: absolute; font-size: 20px; left: 16px; margin-top: 11px\"></i>\n    <b class=\"icon-secondary ion-lock-combination\" style=\"left: 14px; margin-top: -4px\"></b>\n    <b class=\"icon-secondary ion-arrow-right-c\" style=\"font-size: 12px; left: 28px; margin-top: -4px\"></b>\n    <b class=\"icon-secondary ion-lock-combination\" style=\"left: 38px; margin-top: -4px\"></b>\n\n    <span class=\"col col-75\" translate>CURRENCY.VIEW.AVG_GEN_TIME</span>\n    <span class=\"badge badge-stable\">{{formData.avgGenTime | formatDuration}}</span>\n  </ion-item>\n\n\n    <div id=\"helptip-network-peers\" class=\"item item-divider\">\n      <div class=\"pull-left\">\n        <span ng-if=\"search.type==\'member\'\" translate>PEER.MEMBERS</span>\n        <span ng-if=\"search.type==\'mirror\'\" translate>PEER.MIRRORS</span>\n        <span ng-if=\"search.type==\'offline\'\" translate>PEER.OFFLINE</span>\n        <span ng-if=\"!search.type\" translate>PEER.PEERS</span>\n        <span ng-if=\"!search.loading\">({{search.results.length}})</span>\n      </div>\n\n      <div class=\"buttons pull-right\">\n        <ion-spinner class=\"icon\" icon=\"android\" ng-if=\"search.loading\"></ion-spinner>\n        <a class=\"button button-icon button-small-padding icon ion-loop gray hidden-xs hidden-sm ink\" ng-if=\"!search.loading\" ng-click=\"refresh()\">\n        </a>\n        <a class=\"button button-icon button-small-padding icon ion-android-more-vertical gray hidden-xs hidden-sm ink\" ng-if=\"!search.loading\" ng-click=\"showActionsPopover($event)\">\n        </a>\n\n      </div>\n    </div>\n\n    <ng-include src=\"\'templates/network/items_peers.html\'\"></ng-include>\n");
$templateCache.put("templates/currency/items_parameters.html","<div bind-notifier=\"{ rebind:formData.useRelative }\">\n\n  <ion-item class=\"item-icon-left item-text-wrap visible-xs visible-sm\">\n    <i class=\"icon ion-android-bookmark\"></i>\n    <span translate>CURRENCY.VIEW.CURRENCY_NAME</span>\n    <div class=\"item-note dark\" ng-if=\"!loading\">\n      {{formData.currency}} (<span ng-bind-html=\":rebind:formData.currency | currencySymbol:formData.useRelative\"></span>)\n    </div>\n  </ion-item>\n\n\n  <ion-item class=\"item-icon-left item-text-wrap\">\n    <i class=\"icon ion-record\"></i>\n    <div class=\"col col-60\">\n      <span translate>CURRENCY.VIEW.MASS</span>\n      <span class=\"gray\">(M<sub>t</sub>)</span>\n    </div>\n    <span class=\"badge badge-calm\" ng-if=\"!loading\" ng-bind-html=\":rebind:formData.M | formatAmount:{currency: formData.currency, useRelative: formData.useRelative, currentUD: formData.currentUD}\"></span>\n  </ion-item>\n\n  <ion-item id=\"helptip-currency-mass-member\" class=\"item-icon-left item-text-wrap\">\n    <i class=\"icon ion-pie-graph\"></i>\n    <div class=\"col col-60\">\n      <span translate>CURRENCY.VIEW.SHARE</span>\n      <span class=\"gray\">(M<sub>t</sub>/N<sub>t</sub>)</span>\n    </div>\n    <span id=\"helptip-currency-mass-member-unit\" ng-if=\"!loading\" class=\"badge badge-energized\" ng-bind-html=\":rebind:formData.MoverN | formatAmount:{currency: formData.currency, useRelative: formData.useRelative, currentUD: formData.currentUD}\">\n    </span>\n  </ion-item>\n\n\n  <!-- Allow extension here -->\n  <cs-extension-point name=\"parameters-actual\"></cs-extension-point>\n\n\n  <ion-item class=\"item-icon-left item-text-wrap\">\n    <i class=\"icon ion-arrow-graph-up-right\"></i>\n    <div class=\"col col-60\">\n      <span translate>CURRENCY.VIEW.C_ACTUAL</span>\n      <span class=\"gray\">(c<sub>{{\'CURRENCY.VIEW.CURRENT\'|translate}}</sub>)</span>\n    </div>\n    <span class=\"badge badge-stable\">{{formData.cactual | formatNumeral: \'0,0.00\'}} %&nbsp;/&nbsp;{{formData.dt | formatPeriod}}</span>\n  </ion-item>\n\n\n  <ion-item class=\"item-icon-left item-text-wrap\">\n    <i class=\"icon ion-load-c\"></i>\n    <div class=\"col col-60\">\n      <span translate>CURRENCY.VIEW.UD</span>\n      <span class=\"gray\">({{\'COMMON.UD\'|translate}}<sub>t</sub>)</span>\n    </div>\n    <div class=\"badge badge-royal\" ng-if=\"!loading\">\n      <span ng-if=\"formData.useRelative\">\n        1 <ng-bind-html ng-bind-html=\":rebind:formData.currency| currencySymbol:true\"></ng-bind-html>\n      </span>\n      <span ng-if=\"!formData.useRelative\" ng-bind-html=\":rebind:formData.currentUD | formatAmount:{currency: formData.currency, useRelative: formData.useRelative, currentUD: formData.currentUD}\">\n      </span>\n      &nbsp;/&nbsp;{{formData.dt | formatPeriod}}\n    </div>\n  </ion-item>\n\n  <div class=\"item item-toggle dark\">\n    <div class=\"item-label text-right gray\" translate>COMMON.BTN_RELATIVE_UNIT</div>\n    <label class=\"toggle toggle-royal\" id=\"helptip-currency-change-unit\">\n      <input type=\"checkbox\" ng-model=\"formData.useRelative\">\n      <div class=\"track\">\n        <div class=\"handle\"></div>\n      </div>\n    </label>\n  </div>\n\n\n  <a name=\"helptip-currency-rules-anchor\"></a>\n  <div class=\"item item-divider\" id=\"helptip-currency-rules\">\n    <span translate>CURRENCY.VIEW.MONEY_RULES_DIVIDER</span>\n  </div>\n\n  <ion-item class=\"item-icon-left item-text-wrap\">\n    <i class=\"icon ion-arrow-graph-up-right\"></i>\n    <div class=\"col col-60\">\n      <span translate>CURRENCY.VIEW.C_RULE</span>\n      <span class=\"gray\">(c)</span>\n    </div>\n    <!-- compat with Duniter 0.90 -->\n    <span class=\"item-note dark\" ng-if=\"!loading && !formData.udReevalTime0\">{{formData.c*100 | formatNumeral: \'0,0.00\'}} %&nbsp;/&nbsp;{{formData.dt | formatPeriod}}</span>\n    <!-- Duniter 1.0+ -->\n    <span class=\"badge badge-stable\" ng-if=\"!loading && formData.udReevalTime0\">{{formData.c*100 | formatNumeral: \'0,0.00\'}} %&nbsp;/&nbsp;{{formData.dtReeval | formatDuration}}</span>\n  </ion-item>\n\n  <!-- only Duniter 1.0+ -->\n  <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.udReevalTime0 && formData.allRules\">\n    <i class=\"icon ion-load-c\"></i>\n    <b class=\"ion-clock icon-secondary\" style=\"font-size: 18px; left: 36px; top: -12px\"></b>\n    <div class=\"col col-60\">\n      <span translate>CURRENCY.VIEW.DT_REEVAL</span>\n      <span class=\"gray\">(dt<sub>{{\'CURRENCY.VIEW.REEVAL_SYMBOL\'|translate}}</sub>)</span>\n    </div>\n    <span class=\"item-note dark\" ng-if=\"!loading\" translate=\"CURRENCY.VIEW.DT_REEVAL_VALUE\" translate-values=\"formData\">\n    </span>\n  </ion-item>\n\n  <!-- only Duniter 1.0+ -->\n  <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.udReevalTime0 && formData.allRules\">\n    <i class=\"icon ion-load-c\"></i>\n    <b class=\"ion-calendar icon-secondary\" style=\"font-size: 18px; left: 36px; top: -12px\"></b>\n    <div class=\"col col-60\">\n      <span translate>CURRENCY.VIEW.UD_REEVAL_TIME0</span>\n      <span class=\"gray\">(t0<sub>{{\'CURRENCY.VIEW.REEVAL_SYMBOL\'|translate}}</sub>)</span>\n    </div>\n    <span class=\"item-note dark\" ng-if=\"!loading\">{{formData.udReevalTime0|formatDate}}\n    </span>\n  </ion-item>\n\n  <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allRules\">\n    <i class=\"icon ion-load-c\"></i>\n    <b class=\"ion-calculator icon-secondary\" style=\"font-size: 18px; left: 36px; top: -12px\"></b>\n    <div class=\"col col-60\">\n      <span translate>CURRENCY.VIEW.UD_RULE</span>\n      <span class=\"gray\" ng-if=\"formData.udReevalTime0\">- {{\'COMMON.UD\'|translate}}<sub>{{formData.dt|formatPeriod}}</sub>(t<sub>{{\'CURRENCY.VIEW.REEVAL_SYMBOL\'|translate}}</sub>)</span>\n    </div>\n    <!-- compat with Duniter 0.90 -->\n    <span class=\"item-note dark\" ng-if=\"!loading && !formData.udReevalTime0\">{{\'COMMON.UD\'|translate}}<sub>t-1</sub> + c<sup>2</sup> * M<sub>t-1</sub>/N<sub>t-1</sub></span>\n    <!-- Duniter 1.0+ -->\n    <span class=\"item-note dark\" ng-if=\"!loading && formData.udReevalTime0\">{{\'COMMON.UD\'|translate}}<sub>{{formData.dt|formatPeriod}}</sub>(t<sub>{{\'CURRENCY.VIEW.REEVAL_SYMBOL\'|translate}}</sub> - dt<sub>{{\'CURRENCY.VIEW.REEVAL_SYMBOL\'|translate}}</sub>)+ c<sup>2</sup> * (M/N)(t<sub>{{\'CURRENCY.VIEW.REEVAL_SYMBOL\'|translate}}</sub> - dt<sub>{{\'CURRENCY.VIEW.REEVAL_SYMBOL\'|translate}}</sub>) / dt<sub>{{\'CURRENCY.VIEW.REEVAL_SYMBOL\'|translate}}</sub></span>\n  </ion-item>\n\n  <div class=\"item item-toggle dark\">\n    <div class=\"item-label text-right gray\" translate>CURRENCY.VIEW.DISPLAY_ALL_RULES</div>\n    <label class=\"toggle toggle-royal\">\n      <input type=\"checkbox\" ng-model=\"formData.allRules\">\n      <div class=\"track\">\n        <div class=\"handle\"></div>\n      </div>\n    </label>\n  </div>\n\n</div>\n");
$templateCache.put("templates/currency/items_wot.html","\n  <div bind-notifier=\"{ rebind:formData.useRelative }\">\n\n    <a name=\"helptip-currency-newcomers-anchor\"></a>\n    <ion-item class=\"item-icon-left item-text-wrap\">\n      <i class=\"icon ion-person-stalker\"></i>\n      <div class=\"col col-60\">\n        <span translate>CURRENCY.VIEW.MEMBERS</span>\n        <span class=\"gray\">(N<sub>{{\'CURRENCY.VIEW.CURRENT\'|translate}}</sub>)</span>\n      </div>\n      <span class=\"badge badge-calm\" ng-if=\"!loading\">{{formData.N | formatInteger}}</span>\n    </ion-item>\n\n    <ion-item id=\"helptip-currency-newcomers\" class=\"item-icon-left item-text-wrap\">\n      <i class=\"icon ion-arrow-graph-up-right\"></i>\n      <div class=\"col col-75\">\n        <span translate=\"CURRENCY.VIEW.MEMBERS_VARIATION\" translate-values=\"{duration: formData.durationFromLastUD}\"></span>\n        <span class=\"gray\">(&#916;N)</span>\n      </div>\n      <div class=\"badge\" ng-if=\"!loading\" ng-class=\"{\'badge-balanced\': (formData.N>formData.Nprev), \'badge-stable\': (formData.N==formData.Nprev) ,\'badge-assertive\': (formData.Nprev>formData.N)}\">\n        {{formData.N > formData.Nprev ? \'+\' : \'\'}}{{formData.N - formData.Nprev}}\n      </div>\n    </ion-item>\n\n    <!-- Allow extension here -->\n    <cs-extension-point name=\"wot-actual\"></cs-extension-point>\n\n    <div class=\"item item-divider\">\n      <span translate>CURRENCY.VIEW.WOT_RULES_DIVIDER</span>\n    </div>\n\n    <ion-item class=\"item-icon-left item-text-wrap\">\n      <i class=\"icon ion-ribbon-b\"></i>\n      <span class=\"col col-75\" translate>CURRENCY.VIEW.SIG_QTY_RULE</span>\n      <span class=\"badge badge-balanced\" ng-if=\"!loading\">{{formData.sigQty}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\">\n      <i class=\"icon ion-person\"></i>\n      <b class=\"ion-clock icon-secondary\" style=\"font-size: 18px; left: 33px; top: -12px\"></b>\n      <span class=\"col col-60\" translate>CURRENCY.VIEW.MS_WINDOW</span>\n      <span class=\"badge badge-assertive\" ng-if=\"!loading\">{{formData.msWindow | formatDuration}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\">\n      <i class=\"icon ion-person\"></i>\n      <b class=\"ion-calendar icon-secondary\" style=\"font-size: 18px; left: 33px; top: -12px\"></b>\n      <span class=\"col col-60\" translate>CURRENCY.VIEW.MS_VALIDITY</span>\n      <span class=\"badge badge-balanced\" ng-if=\"!loading\">{{formData.msValidity | formatDuration}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allWotRules\">\n      <i class=\"icon ion-ribbon-b\"></i>\n      <b class=\"ion-clock icon-secondary\" style=\"font-size: 18px; left: 33px; top: -12px\"></b>\n      <span class=\"col col-60\" translate>CURRENCY.VIEW.SIG_WINDOW</span>\n      <span class=\"badge badge-stable\" ng-if=\"!loading\">{{formData.sigWindow | formatDuration}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allWotRules\">\n      <i class=\"icon ion-ribbon-b\"></i>\n      <b class=\"ion-calendar icon-secondary\" style=\"font-size: 18px; left: 33px; top: -12px\"></b>\n      <span class=\"col col-60\" translate>CURRENCY.VIEW.SIG_VALIDITY</span>\n      <span class=\"badge badge-balanced\" ng-if=\"!loading\">{{formData.sigValidity | formatDuration}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allWotRules\">\n      <i class=\"icon ion-ribbon-a\"></i>\n      <span class=\"col col-75\" translate>CURRENCY.VIEW.SIG_STOCK</span>\n      <span class=\"badge badge-stable\" ng-if=\"!loading\">{{formData.sigStock}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allWotRules\">\n      <i class=\"icon ion-clock\" style=\"position: absolute; font-size: 20px; left: 16px\"></i>\n      <b class=\"ion-ribbon-a icon-secondary\" style=\"left: 16px; top: -15px\"></b>\n      <b class=\"ion-arrow-right-c icon-secondary\" style=\"left: 28px; top: -15px\"></b>\n      <b class=\"ion-ribbon-a icon-secondary\" style=\"left: 40px; top: -15px\"></b>\n      <span class=\"col col-75\" translate>CURRENCY.VIEW.SIG_PERIOD</span>\n      <span class=\"badge badge-stable\" ng-if=\"!loading\">{{formData.sigPeriod | formatDuration}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allWotRules\">\n      <i class=\"icon ion-steam\"></i>\n      <b class=\"ion-person icon-secondary\" style=\"left: 38px; top: -17px\"></b>\n      <div class=\"col col-75\">\n        <span ng-bind-html=\"\'CURRENCY.VIEW.STEP_MAX\'|translate\"></span>\n        <span class=\"gray\">(stepMax)</span>\n      </div>\n      <span class=\"badge badge-assertive\" ng-if=\"!loading\">{{formData.stepMax}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allWotRules\">\n      <i class=\"icon ion-ribbon-b\"></i>\n      <b class=\"ion-star icon-secondary\" style=\"color: yellow; font-size: 16px; left: 25px; top: -7px\"></b>\n      <span class=\"col col-75\" translate>CURRENCY.VIEW.SENTRIES</span>\n      <span class=\"badge badge-stable\" ng-if=\"!loading\">{{formData.sentries}}</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allWotRules\">\n      <i class=\"icon ion-ribbon-b\"></i>\n      <b class=\"ion-star icon-secondary\" style=\"color: yellow; font-size: 16px; left: 25px; top: -7px\"></b>\n      <span class=\"col col-75\" translate>CURRENCY.VIEW.SENTRIES_FORMULA</span>\n      <span class=\"item-note dark\" ng-if=\"!loading\">{{\'CURRENCY.VIEW.MATH_CEILING\'| translate}}( N<sub>t</sub><sup>^ (1 / stepMax)</sup>)</span>\n    </ion-item>\n\n    <ion-item class=\"item-icon-left item-text-wrap\" ng-if=\"formData.allWotRules\">\n      <i class=\"icon ion-pull-request\"></i>\n      <span class=\"col col-75\" translate>CURRENCY.VIEW.XPERCENT</span>\n      <span class=\"badge badge-stable\" ng-if=\"!loading\">{{formData.xpercent*100| formatNumeral: \'0,0\'}} %</span>\n    </ion-item>\n\n    <div class=\"item item-toggle dark\">\n      <div class=\"item-label text-right gray\" translate>CURRENCY.VIEW.DISPLAY_ALL_RULES</div>\n      <label class=\"toggle toggle-royal\">\n        <input type=\"checkbox\" ng-model=\"formData.allWotRules\">\n        <div class=\"track\">\n          <div class=\"handle\"></div>\n        </div>\n      </label>\n    </div>\n\n  </div>\n");
$templateCache.put("templates/currency/lookup.html","<ion-view view-title=\"{{\'CURRENCY.SELECT.TITLE\' | translate}}\">\n  <ion-content class=\"padding no-padding-xs\">\n    <h4 class=\"content double-padding-x\" translate>CURRENCY.SELECT.CURRENCIES</h4>\n\n    <ng-include src=\"\'templates/currency/lookup_form.html\'\">\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/currency/lookup_form.html","<ion-list>\n\n  <div class=\"item center\" ng-if=\"search.loading\">\n    <ion-spinner icon=\"android\"></ion-spinner>\n  </div>\n\n  <div ng-repeat=\"currency in search.results\" ng-class=\"{ selected: selectedCurrency == currency }\">\n    <a class=\"item card card-item stable-bg padding ink\" ng-click=\"selectCurrency(currency)\" ng-class=\"{ selected: selectedCurrency && selectedCurrency.name == currency.name }\">\n      <h2>{{currency.name}}</h2>\n      <h4 class=\"gray\">{{currency.peer.server}}</h4>\n      <span class=\"badge badge-royal\">{{\'CURRENCY.SELECT.MEMBERS_COUNT\'|translate:currency}}</span>\n    </a>\n  </div>\n</ion-list>\n");
$templateCache.put("templates/currency/modal_license.html","<ion-modal-view class=\"modal-full-height modal-license\">\n  <ion-header-bar class=\"bar-positive\">\n    <button class=\"button button-clear visible-xs\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL</button>\n    <h1 class=\"title\" translate>CURRENCY.LICENSE.TITLE</h1>\n\n    <button class=\"button button-icon button-clear icon ion-android-send visible-xs\" ng-click=\"doTransfer()\">\n    </button>\n  </ion-header-bar>\n\n  <ion-content scroll=\"false\" style=\"bottom: 0px\">\n\n    <p ng-if=\"!licenseUrl && !loading\">\n      translate>CURRENCY.LICENSE.NO_LICENSE_FILE\n    </p>\n    <iframe ng-if=\"licenseUrl && !loading\" class=\"padding-left padding-right no-padding-xs iframe-license\" id=\"iframe-license\" ng-src=\"{{licenseUrlHtml||licenseUrl}}\">\n    </iframe>\n\n    <div class=\"padding hidden-xs text-center\">\n      <button class=\"button button-stable ink\" ng-click=\"downloadFile()\">\n        {{\'CURRENCY.LICENSE.BTN_DOWNLOAD\' | translate}}\n      </button>\n\n      <button class=\"button button-positive ink\" type=\"submit\" ng-click=\"closeModal()\">\n        {{\'COMMON.BTN_CLOSE\' | translate}}\n      </button>\n    </div>\n  </ion-content>\n</ion-modal-view>\n");
$templateCache.put("templates/currency/popover_actions.html","<ion-popover-view class=\"fit has-header popover-wallet-actions\">\n  <ion-header-bar>\n    <h1 class=\"title\" translate>COMMON.POPOVER_ACTIONS_TITLE</h1>\n  </ion-header-bar>\n  <ion-content scroll=\"false\">\n    <div class=\"list item-text-wrap\">\n\n      <!-- help tour -->\n      <a class=\"item item-icon-left ink hidden-sm hidden-xs\" ng-click=\"startCurrencyTour()\">\n        <i class=\"icon ion-easel\"></i>\n        {{\'COMMON.BTN_HELP_TOUR_SCREEN\' | translate}}\n      </a>\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/currency/view_currency.html","<ion-view left-buttons=\"leftButtons\" cache-view=\"false\">\n\n  <ion-tabs class=\"tabs-positive tabs-icon-top\">\n\n    <ion-tab title=\"{{\'CURRENCY.VIEW.TAB_CURRENCY\'|translate}}\" icon=\"ion-stats-bars\" ui-sref=\"app.currency.tab_parameters\">\n      <ion-nav-view name=\"tab-parameters\"></ion-nav-view>\n    </ion-tab>\n\n    <ion-tab title=\"{{\'CURRENCY.VIEW.TAB_WOT\'|translate}}\" icon=\"ion-person-stalker\" ui-sref=\"app.currency.tab_wot\">\n      <ion-nav-view name=\"tab-wot\"></ion-nav-view>\n    </ion-tab>\n\n    <ion-tab id=\"helptip-currency-tab-peers\" title=\"{{\'CURRENCY.VIEW.TAB_NETWORK\'|translate}}\" icon=\"ion-cloud\" ui-sref=\"app.currency.tab_network\">\n      <ion-nav-view name=\"tab-network\"></ion-nav-view>\n    </ion-tab>\n\n    <ion-tab title=\"{{\'CURRENCY.VIEW.TAB_BLOCKS\'|translate}}\" icon=\"ion-lock-combination\" ui-sref=\"app.currency.tab_blocks\">\n      <ion-nav-view name=\"tab-blocks\"></ion-nav-view>\n    </ion-tab>\n\n  </ion-tabs>\n</ion-view>\n");
$templateCache.put("templates/currency/view_currency_lg.html","<ion-view left-buttons=\"leftButtons\" cache-view=\"false\">\n    <ion-nav-title bind-notifier=\"{ rebind:formData.useRelative }\">\n      <span ng-if=\"!loading\">\n         {{\'CURRENCY.VIEW.TITLE\' | translate}} {{formData.currency|abbreviate}}\n      </span>\n    </ion-nav-title>\n\n    <ion-nav-buttons side=\"secondary\">\n        <button class=\"button button-icon button-clear icon ion-loop visible-xs visible-sm\" ng-click=\"refreshPeers()\">\n        </button>\n\n        <!-- Allow extension here -->\n        <cs-extension-point name=\"nav-buttons\"></cs-extension-point>\n    </ion-nav-buttons>\n\n    <ion-content>\n\n      <!-- Buttons bar-->\n      <div class=\"hidden-xs hidden-sm padding text-center\">\n\n\n        <button class=\"button button-stable icon-right ink\" ng-if=\"formData.licenseUrl\" ng-click=\"showLicenseModal()\">\n          <i class=\"icon ion-document-text\"></i>&nbsp;\n          {{\'CURRENCY.VIEW.BTN_SHOW_LICENSE\' | translate}}\n        </button>\n\n        <button class=\"button button-stable button-small-padding icon ion-loop ink\" ng-click=\"refresh()\" title=\"{{\'COMMON.BTN_REFRESH\' | translate}}\">\n        </button>\n\n        <button class=\"button button-stable button-small-padding icon ion-android-more-vertical ink\" ng-click=\"showActionsPopover($event)\">\n        </button>\n\n      </div>\n\n      <div class=\"item item-text-wrap no-border no-padding pull-left\">\n        <div class=\"item-icon-left card padding stable-900-bg\">\n          <ion-spinner class=\"icon\" icon=\"android\" ng-if=\"loading\"></ion-spinner>\n          <i class=\"icon ion-help-circled calm\" ng-if=\"!loading\"></i>\n          <div class=\"item-icon-left-padding\" style=\"min-height: 26px\">\n            <span ng-if=\"!loading\" trust-as-html=\"\'CURRENCY.VIEW.CURRENCY_SHORT_DESCRIPTION\'|translate:formData\">\n            </span>\n          </div>\n        </div>\n      </div>\n\n      <div class=\"row responsive-sm\">\n        <!-- currency -->\n        <div class=\"col list\">\n          <div class=\"item item-divider\">\n            <span translate>CURRENCY.VIEW.MONEY_DIVIDER</span>\n          </div>\n          <ng-include src=\"\'templates/currency/items_parameters.html\'\"></ng-include>\n\n        </div>\n\n        <!-- wot -->\n        <div class=\"col list\">\n          <div class=\"item item-divider\">\n            <span translate>CURRENCY.VIEW.WOT_DIVIDER</span>\n          </div>\n          <ng-include src=\"\'templates/currency/items_wot.html\'\"></ng-include>\n\n        </div>\n      </div>\n    </ion-content>\n</ion-view>\n");
$templateCache.put("templates/help/help.html","\n  <a name=\"join\"></a>\n  <h2 translate>HELP.JOIN.SECTION</h2>\n\n    <a name=\"join-salt\"></a>\n    <div class=\"row responsive-sm\" ng-class=\"itemsClass[\'join-salt\']\">\n      <div class=\"col col-20\" translate>LOGIN.SALT</div>\n      <div class=\"col\" translate>HELP.JOIN.SALT</div>\n    </div>\n\n    <a name=\"join-password\"></a>\n    <div class=\"row responsive-sm\" ng-class=\"itemsClass[\'join-password\']\">\n      <div class=\"col col-20\" translate>LOGIN.PASSWORD</div>\n      <div class=\"col\" translate>HELP.JOIN.PASSWORD</div>\n    </div>\n\n    <a name=\"join-pseudo\"></a>\n    <div class=\"row responsive-sm\" ng-class=\"itemsClass[\'join-pseudo\']\">\n      <div class=\"col col-20\" translate>ACCOUNT.NEW.PSEUDO</div>\n      <div class=\"col\" translate>HELP.JOIN.PSEUDO</div>\n    </div>\n\n  <a name=\"login\"></a>\n  <h2 translate>HELP.LOGIN.SECTION</h2>\n\n  <a name=\"login-pubkey\"></a>\n  <div class=\"row responsive-sm\" ng-class=\"itemsClass[\'login-pubkey\']\">\n    <div class=\"col col-20\" translate>HELP.LOGIN.PUBKEY</div>\n    <div class=\"col\" translate>HELP.LOGIN.PUBKEY_DEF</div>\n  </div>\n\n  <a name=\"login-method\"></a>\n  <div class=\"row responsive-sm\" ng-class=\"itemsClass[\'login-method\']\">\n    <div class=\"col col-20\" translate>HELP.LOGIN.METHOD</div>\n    <div class=\"col\" translate>HELP.LOGIN.METHOD_DEF</div>\n  </div>\n\n  <a name=\"glossary\"></a>\n  <h2 translate>HELP.GLOSSARY.SECTION</h2>\n\n    <a name=\"pubkey\"></a>\n    <div class=\"row responsive-sm\" ng-class=\"itemsClass.pubkey\">\n      <div class=\"col col-20\" translate>COMMON.PUBKEY</div>\n      <div class=\"col\" translate>HELP.GLOSSARY.PUBKEY_DEF</div>\n    </div>\n\n    <a name=\"blockchain\"></a>\n    <div class=\"row responsive-sm\" ng-class=\"itemsClass.blockchain\">\n      <div class=\"col col-20\" translate>HELP.GLOSSARY.BLOCKCHAIN</div>\n      <div class=\"col\" translate>HELP.GLOSSARY.BLOCKCHAIN_DEF</div>\n    </div>\n\n    <a name=\"universal_dividend\"></a>\n    <a name=\"ud\"></a>\n    <div class=\"row responsive-sm\" ng-class=\"itemsClass.ud\">\n      <div class=\"col col-20\" translate>COMMON.UNIVERSAL_DIVIDEND</div>\n      <div class=\"col\" translate>HELP.GLOSSARY.UNIVERSAL_DIVIDEND_DEF</div>\n    </div>\n\n    <a name=\"member\"></a>\n    <div class=\"row responsive-sm\" ng-class=\"itemsClass.member\">\n      <div class=\"col col-20\" translate>HELP.GLOSSARY.MEMBER</div>\n      <div class=\"col\" translate>HELP.GLOSSARY.MEMBER_DEF</div>\n    </div>\n\n    <a name=\"currency_rules\"></a>\n    <div class=\"row responsive-sm\" ng-class=\"itemsClass.currency_rules\">\n      <div class=\"col col-20\" translate>HELP.GLOSSARY.CURRENCY_RULES</div>\n      <div class=\"col\" translate>HELP.GLOSSARY.CURRENCY_RULES_DEF</div>\n    </div>\n");
$templateCache.put("templates/help/modal_help.html","<ion-modal-view class=\"modal-full-height modal-help\">\n\n    <ion-header-bar class=\"bar-positive\">\n      <button class=\"button button-clear\" ng-click=\"closeModal()\" translate>COMMON.BTN_CLOSE\n      </button>\n\n      <h1 class=\"title\" translate>HELP.TITLE</h1>\n    </ion-header-bar>\n\n    <ion-content scroll=\"true\" class=\"padding no-padding-xs\">\n\n      <div ng-class=\"listClass\">\n        <ng-include src=\"\'templates/help/help.html\'\"></ng-include>\n      </div>\n\n      <div class=\"padding hidden-xs text-center\">\n        <button class=\"button button-positive ink\" type=\"submit\" ng-click=\"closeModal()\">\n          {{\'COMMON.BTN_CLOSE\' | translate}}\n        </button>\n      </div>\n\n    </ion-content>\n</ion-modal-view>\n");
$templateCache.put("templates/help/view_help.html","<ion-view left-buttons=\"leftButtons\">\n  <ion-nav-title>\n    <span class=\"visible-xs visible-sm\" translate>HELP.TITLE</span>\n  </ion-nav-title>\n\n  <ion-content scroll=\"true\" class=\"padding\">\n\n    <h1 class=\"hidden-xs hidden-sm\" translate>HELP.TITLE</h1>\n\n    <ng-include src=\"\'templates/help/help.html\'\"></ng-include>\n\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/home/home.html","<ion-view id=\"home\" class=\"\">\n  <ion-nav-title>\n\n  </ion-nav-title>\n\n  <ion-content class=\"has-header text-center padding-xs positive-900-bg circle-bg-dark\">\n\n    <div id=\"helptip-home-logo\" class=\"logo\"></div>\n\n    <h4>\n      <span class=\"hidden-xs\" translate>HOME.WELCOME</span>\n      <span ng-show=\"!loading\" translate-values=\":currency:{currency: $root.currency.name}\" translate>HOME.MESSAGE</span>\n    </h4>\n\n    <div class=\"center padding\">\n\n      <ion-spinner icon=\"android\" ng-if=\"loading\"></ion-spinner>\n\n      <div class=\"animate-fade-in animate-show-hide ng-hide\" ng-show=\"!loading && error\">\n        <div class=\"card card-item padding\">\n          <p class=\"item-content item-text-wrap\">\n              <span class=\"dark\" trust-as-html=\"\'HOME.CONNECTION_ERROR\'|translate:node\"></span>\n          </p>\n\n          <!-- Retry -->\n          <button type=\"button\" class=\"button button-positive icon icon-left ion-refresh ink\" ng-click=\"reload()\">{{\'COMMON.BTN_REFRESH\'|translate}}</button>\n        </div>\n      </div>\n    </div>\n\n    <div class=\"center animate-fade-in animate-show-hide ng-hide\" ng-show=\"!loading && !error\">\n\n      <!-- Help tour (NOT ready yet for small device) -->\n      <button type=\"button\" class=\"button button-block button-stable button-raised icon-left icon ion-easel ink-dark hidden-xs\" ng-click=\"startHelpTour()\">\n        {{\'COMMON.BTN_HELP_TOUR\'|translate}}\n      </button>\n\n      <!-- Currency-->\n      <button type=\"button\" class=\"item button button-block button-stable button-raised icon icon-left ion-ios-world-outline ink-dark hidden-sm hidden-xs\" ui-sref=\"app.currency\">{{\'HOME.BTN_CURRENCY\'|translate:$root.currency }}</button>\n\n      <button type=\"button\" class=\"item button button-block button-positive button-raised icon icon-left ion-locked ink-dark\" ui-sref=\"app.view_wallet\" ng-show=\"!login\" translate>COMMON.BTN_LOGIN</button>\n\n      <button type=\"button\" class=\"item button button-block button-positive button-raised icon icon-left ion-person ink-dark\" ui-sref=\"app.view_wallet\" ng-show=\"login\" translate>MENU.ACCOUNT</button>\n\n      <button type=\"button\" class=\"item button button-block button-positive button-raised icon icon-left ion-card ink-dark visible-xs\" ui-sref=\"app.view_wallet_tx\" ng-show=\"login\" translate>MENU.TRANSACTIONS</button>\n\n      <br class=\"visible-xs visible-sm\">\n\n      <!-- join link -->\n      <div class=\"text-center no-padding\" ng-show=\"!login\">\n        <br class=\"visible-xs visible-sm\">\n        {{\'LOGIN.NO_ACCOUNT_QUESTION\'|translate}}\n        <b>\n          <a class=\"assertive hidden-xs hidden-sm\" ng-click=\"showJoinModal()\" translate>\n            LOGIN.CREATE_ACCOUNT\n          </a>\n        </b>\n      </div>\n\n      <!-- disconnect link -->\n      <div class=\"text-center no-padding\" ng-show=\"login\">\n        <br class=\"visible-xs visible-sm\">\n        <span ng-bind-html=\"\'HOME.NOT_YOUR_ACCOUNT_QUESTION\'|translate:$root.walletData\"></span>\n        <br>\n        <b>\n          <a class=\"assertive hidden-xs hidden-sm\" ng-click=\"logout()\" translate>\n            HOME.BTN_CHANGE_ACCOUNT\n          </a>\n        </b>\n      </div>\n\n      <button type=\"button\" class=\"button button-block button-calm button-raised icon icon-left ion-wand ink-dark visible-xs visible-sm\" ng-click=\"showJoinModal()\" ng-if=\"!login\" translate>LOGIN.CREATE_ACCOUNT</button>\n      <button type=\"button\" class=\"button button-block button-assertive button-raised icon icon-left ion-wand ink-dark visible-xs visible-sm\" ng-click=\"logout()\" ng-if=\"login\" translate>COMMON.BTN_LOGOUT</button>\n\n\n      <div class=\"text-center no-padding visible-xs stable\">\n        <br>\n        <!-- version -->\n        {{\'COMMON.APP_VERSION\'|translate:{version: config.version} }}\n        |\n        <!-- about -->\n        <a href=\"#\" ng-click=\"showAboutModal()\" translate>HOME.BTN_ABOUT</a>\n      </div>\n\n    </div>\n  </ion-content>\n\n</ion-view>\n\n");
$templateCache.put("templates/join/modal_choose_account_type.html","<ion-modal-view class=\"modal-full-height\">\n\n  <ion-header-bar class=\"bar-positive\">\n\n    <button class=\"button button-clear visible-xs\" ng-if=\"!slides.slider.activeIndex\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL\n    </button>\n    <button class=\"button button-icon button-clear icon ion-ios-arrow-back buttons header-item\" ng-click=\"slidePrev()\" ng-if=\"slides.slider.activeIndex\">\n    </button>\n\n    <h1 class=\"title\" translate>ACCOUNT.NEW.TITLE</h1>\n\n    <button class=\"button button-clear icon-right visible-xs\" ng-if=\"slides.slider.activeIndex === 0\" ng-click=\"slideNext()\">\n      <span translate>COMMON.BTN_NEXT</span>\n      <i class=\"icon ion-ios-arrow-right\"></i>\n    </button>\n  </ion-header-bar>\n\n\n    <ion-slides options=\"slides.options\" slider=\"slides.slider\">\n\n      <!-- STEP 1: currency -->\n      <ion-slide-page>\n        <ion-content class=\"has-header padding\">\n          <div class=\"center padding\" ng-if=\"loading\">\n            <ion-spinner class=\"icon\" icon=\"android\"></ion-spinner>\n          </div>\n\n          <div ng-if=\"!loading\">\n\n\n            <p ng-bind-html=\"\'ACCOUNT.NEW.INTRO_WARNING_TIME\'|translate:currency\"></p>\n\n            <div class=\"row responsive-sm\">\n              <div class=\"col\">\n                <div class=\"item card item-icon-left padding item-text-wrap stable-bg\">\n                  <i class=\"icon ion-android-warning assertive\"></i>\n\n                  <p class=\"item-content item-icon-left-padding\">\n                    <span class=\"dark\" translate>ACCOUNT.NEW.INTRO_WARNING_SECURITY</span><br>\n                    <small translate>ACCOUNT.NEW.INTRO_WARNING_SECURITY_HELP</small>\n                  </p>\n                </div>\n              </div>\n\n              <div class=\"col\">\n                <div class=\"item card item-icon-left padding item-text-wrap stable-bg\">\n                  <i class=\"icon ion-information-circled positive\"></i>\n                  <p class=\"item-content item-icon-left-padding\">\n                    <span class=\"dark\" trust-as-html=\"\'ACCOUNT.NEW.REGISTRATION_NODE\'|translate:currency.node\"></span><br>\n                    <small trust-as-html=\"\'ACCOUNT.NEW.REGISTRATION_NODE_HELP\'|translate:currency.node\"></small>\n                  </p>\n                </div>\n              </div>\n            </div>\n\n\n          </div>\n\n          <p class=\"hidden-xs hidden-sm\" ng-bind-html=\"\'ACCOUNT.NEW.INTRO_HELP\'|translate\"></p>\n\n          <div class=\"padding hidden-xs text-right\">\n            <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n            </button>\n            <button class=\"button button-positive icon-right ion-chevron-right ink\" ng-click=\"slideNext()\" ng-disabled=\"loading\" type=\"button\" translate>\n              COMMON.BTN_START\n            </button>\n          </div>\n        </ion-content>\n      </ion-slide-page>\n\n      <!-- STEP 2: account type -->\n      <ion-slide-page>\n        <ion-content class=\"has-header padding\">\n          <p translate>ACCOUNT.NEW.SELECT_ACCOUNT_TYPE</p>\n          <div class=\"list\">\n            <!-- member account -->\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink\" ng-click=\"selectAccountTypeAndClose(\'member\')\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image icon dark ion-person\"></i>\n                <h2 translate>ACCOUNT.NEW.MEMBER_ACCOUNT</h2>\n                <h4 class=\"gray\" ng-bind-html=\"\'ACCOUNT.NEW.MEMBER_ACCOUNT_HELP\'|translate:currency\"></h4>\n                <i class=\"icon dark ion-ios-arrow-right\"></i>\n              </div>\n            </div>\n\n            <!-- Allow extension here -->\n            <cs-extension-point name=\"select-account-type\"></cs-extension-point>\n\n            <!-- simple wallet -->\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink\" ng-click=\"selectAccountTypeAndClose(\'wallet\')\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image icon dark ion-card\"></i>\n                <h2 translate>ACCOUNT.NEW.WALLET_ACCOUNT</h2>\n                <h4 class=\"gray\" translate>ACCOUNT.NEW.WALLET_ACCOUNT_HELP</h4>\n                <i class=\"icon dark ion-ios-arrow-right\"></i> </div>\n            </div>\n          </div>\n          <div class=\"padding hidden-xs text-right\">\n            <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n            </button>\n          </div>\n        </ion-content>\n      </ion-slide-page>\n    \n</ion-slides></ion-modal-view>\n");
$templateCache.put("templates/join/modal_join_member.html","<ion-modal-view class=\"modal-full-height\">\n\n  <ion-header-bar class=\"bar-positive\">\n\n    <button class=\"button button-clear visible-xs\" ng-if=\"!slides.slider.activeIndex\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL\n    </button>\n    <button class=\"button button-icon button-clear icon ion-ios-arrow-back buttons header-item\" ng-click=\"slidePrev()\" ng-if=\"slides.slider.activeIndex && slideBehavior.hasPreviousButton\">\n    </button>\n    <button class=\"button button-icon button-clear icon ion-ios-help-outline visible-xs\" ng-if=\"slideBehavior.helpAnchor\" ng-click=\"showHelpModal(slideBehavior.helpAnchor)\">\n    </button>\n\n    <h1 class=\"title\" translate>ACCOUNT.NEW.MEMBER_ACCOUNT_TITLE</h1>\n\n    <!-- next -->\n    <button class=\"button button-clear icon-right visible-xs\" ng-if=\"slideBehavior.hasNextButton\" ng-click=\"doNext()\">\n      <span translate>COMMON.BTN_NEXT</span>\n      <i class=\"icon ion-ios-arrow-right\"></i>\n    </button>\n    <!-- accept -->\n    <button class=\"button button-clear icon-right visible-xs\" ng-class=\"{\'button-text-stable\': !isLicenseRead}\" ng-if=\"slideBehavior.hasAcceptButton\" ng-click=\"isLicenseRead ? doNext() : undefined\">\n      <span translate>ACCOUNT.NEW.BTN_ACCEPT</span>\n      <i class=\"icon ion-ios-arrow-right\"></i>\n    </button>\n    <!-- send -->\n    <button class=\"button button-clear icon-right visible-xs\" ng-if=\"slideBehavior.hasSendButton\" ng-click=\"doNewAccount()\">\n      <i class=\"icon ion-android-send\"></i>\n    </button>\n  </ion-header-bar>\n\n\n    <ion-slides options=\"slides.options\" slider=\"slides.slider\" ng-init=\"console.log(\'START SLIDE\');\">\n\n      <!-- STEP 1: license -->\n      <ion-slide-page ng-if=\"licenseFileUrl\">\n        <ion-content class=\"has-header\" scroll=\"false\">\n            <div class=\"padding\" translate>ACCOUNT.NEW.INFO_LICENSE</div>\n\n            <div class=\"center padding\" ng-if=\"loading\">\n              <ion-spinner class=\"icon\" icon=\"android\"></ion-spinner>\n            </div>\n\n            <iframe ng-if=\"!loading\" class=\"padding-left padding-right no-padding-xs iframe-license\" id=\"iframe-license\" ng-src=\"{{licenseFileUrl}}\">\n            </iframe>\n\n            <div class=\"padding hidden-xs text-right\">\n              <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>\n                COMMON.BTN_CANCEL\n              </button>\n              <button class=\"button button-calm icon-right ion-chevron-right ink\" ng-click=\"doNext(\'licenceForm\')\" ng-disabled=\"!isLicenseRead\" type=\"button\" translate>\n                ACCOUNT.NEW.BTN_ACCEPT_LICENSE\n              </button>\n            </div>\n        </ion-content>\n      </ion-slide-page>\n\n      <!-- STEP 2: pseudo-->\n      <ion-slide-page>\n        <ion-content class=\"has-header\" scroll=\"false\">\n          <form name=\"pseudoForm\" novalidate=\"\" ng-submit=\"doNext(\'pseudoForm\')\">\n\n            <div class=\"item item-text-wrap text-center padding\">\n              <a class=\"pull-right icon-help hidden-xs\" ng-click=\"showHelpModal(\'join-pseudo\')\"></a>\n              <span translate>ACCOUNT.NEW.PSEUDO_WARNING</span>\n            </div>\n\n            <div class=\"list\" ng-init=\"setForm(pseudoForm, \'pseudoForm\')\">\n\n              <!-- pseudo -->\n              <div class=\"item item-input\" ng-class=\"{\'item-input-error\': (pseudoForm.$submitted && pseudoForm.pseudo.$invalid) || (uiAlreadyUsed && formData.pseudo)}\">\n                <span class=\"input-label\" translate>ACCOUNT.NEW.PSEUDO</span>\n                <input id=\"pseudo\" name=\"pseudo\" type=\"text\" placeholder=\"{{\'ACCOUNT.NEW.PSEUDO_HELP\' | translate}}\" ng-model=\"formData.pseudo\" autocomplete=\"off\" ng-minlength=\"3\" ng-maxlength=\"100\" ng-pattern=\"userIdPattern\" ng-model-options=\"{ debounce: 250 }\" required>\n              </div>\n              <div class=\"form-errors\" ng-show=\"pseudoForm.$submitted && pseudoForm.pseudo.$error\" ng-messages=\"pseudoForm.pseudo.$error\">\n                <div class=\"form-error\" ng-message=\"minlength\">\n                  <span translate=\"ERROR.FIELD_TOO_SHORT_WITH_LENGTH\" translate-values=\"{minLength: 3}\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"maxlength\">\n                  <span translate=\"ERROR.FIELD_TOO_LONG_WITH_LENGTH\" translate-values=\"{maxLength: 100}\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"required\">\n                  <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"pattern\">\n                  <span translate=\"ERROR.INVALID_USER_ID\"></span>\n                </div>\n              </div>\n\n              <!-- Show if valid pseudo-->\n              <div class=\"text-right\" style=\"min-height: 18px\">\n                <div class=\"form-error gray\" ng-if=\"formData.computing && formData.pseudo\">\n                  <ion-spinner class=\"icon ion-spinner-small\" icon=\"android\" ng-if=\"formData.computing && formData.pseudo\"></ion-spinner>\n                  <span translate>ACCOUNT.NEW.CHECKING_PSEUDO</span>\n                </div>\n\n                <ng-if ng-if=\"!formData.computing && formData.pseudo\">\n                  <div class=\"form-error balanced\" ng-if=\"!uiAlreadyUsed \">\n                    <i class=\"icon ion-checkmark balanced\"></i>\n                    <span translate>ACCOUNT.NEW.PSEUDO_AVAILABLE</span>\n                  </div>\n                  <div class=\"form-error\" ng-if=\"uiAlreadyUsed\">\n                    <i class=\"icon ion-close-circled assertive\"></i>\n                    <span translate>ACCOUNT.NEW.PSEUDO_NOT_AVAILABLE</span>\n                  </div>\n                </ng-if>\n\n              </div>\n\n              <div class=\"padding hidden-xs text-right\">\n                <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n                </button>\n                <button class=\"button button-calm icon-right ion-chevron-right ink\" type=\"submit\" ng-disabled=\"UIDFound\" translate>\n                  COMMON.BTN_NEXT\n                </button>\n              </div>\n            </div>\n          </form>\n        </ion-content>\n      </ion-slide-page>\n\n\n      <!-- STEP 3: salt -->\n      <ion-slide-page>\n        <ion-content class=\"has-header\" scroll=\"false\">\n          <form name=\"saltForm\" novalidate=\"\" ng-submit=\"doNext(\'saltForm\')\">\n\n            <div class=\"list\" ng-init=\"setForm(saltForm, \'saltForm\')\">\n\n              <div class=\"item item-text-wrap text-center padding hidden-xs\">\n                <a class=\"pull-right icon-help\" ng-click=\"showHelpModal(\'join-salt\')\"></a>\n                <span translate>ACCOUNT.NEW.SALT_WARNING</span>\n              </div>\n\n              <!-- salt -->\n              <div class=\"item item-input\" ng-class=\"{ \'item-input-error\': saltForm.$submitted && saltForm.username.$invalid}\">\n                <span class=\"input-label\" translate>LOGIN.SALT</span>\n                <input ng-if=\"!showUsername\" name=\"username\" type=\"password\" placeholder=\"{{\'LOGIN.SALT_HELP\' | translate}}\" ng-change=\"formDataChanged()\" ng-model=\"formData.username\" autocomplete=\"off\" ng-minlength=\"8\" different-to=\"formData.pseudo\" required>\n                <!-- different-to=\"formData.pseudo\" -->\n                <input ng-if=\"showUsername\" name=\"username\" type=\"text\" placeholder=\"{{\'LOGIN.SALT_HELP\' | translate}}\" ng-change=\"formDataChanged()\" ng-model=\"formData.username\" autocomplete=\"off\" ng-minlength=\"8\" different-to=\"formData.pseudo\" required>\n              </div>\n              <div class=\"form-errors\" ng-show=\"saltForm.$submitted && saltForm.username.$error\" ng-messages=\"saltForm.username.$error\">\n                <div class=\"form-error\" ng-message=\"minlength\">\n                  <span translate=\"ERROR.FIELD_TOO_SHORT_WITH_LENGTH\" translate-values=\"{minLength: 8}\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"required\">\n                  <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"differentTo\">\n                  <span translate=\"ERROR.EQUALS_TO_PSEUDO\"></span>\n                </div>\n              </div>\n\n              <!-- confirm salt -->\n              <div class=\"item item-input\" ng-class=\"{ \'item-input-error\': saltForm.$submitted && saltForm.confirmSalt.$invalid}\">\n                <span class=\"input-label pull-right\" translate>ACCOUNT.NEW.SALT_CONFIRM</span>\n                <input ng-if=\"!showUsername\" name=\"confirmUsername\" type=\"password\" placeholder=\"{{\'ACCOUNT.NEW.SALT_CONFIRM_HELP\' | translate}}\" ng-model=\"formData.confirmUsername\" autocomplete=\"off\" compare-to=\"formData.username\">\n                <input ng-if=\"showUsername\" name=\"confirmUsername\" type=\"text\" placeholder=\"{{\'ACCOUNT.NEW.SALT_CONFIRM_HELP\' | translate}}\" ng-model=\"formData.confirmUsername\" autocomplete=\"off\" compare-to=\"formData.username\">\n              </div>\n              <div class=\"form-errors\" ng-show=\"saltForm.$submitted && saltForm.confirmUsername.$error\" ng-messages=\"saltForm.confirmUsername.$error\">\n                <div class=\"form-error\" ng-message=\"compareTo\">\n                  <span translate=\"ERROR.SALT_NOT_CONFIRMED\"></span>\n                </div>\n              </div>\n\n              <!-- Show values -->\n              <div class=\"item item-toggle dark\">\n                <span translate>COMMON.SHOW_VALUES</span>\n                <label class=\"toggle toggle-royal\">\n                  <input type=\"checkbox\" ng-model=\"showUsername\">\n                  <div class=\"track\">\n                    <div class=\"handle\"></div>\n                  </div>\n                </label>\n              </div>\n\n              <div class=\"padding hidden-xs text-right\">\n                <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n                </button>\n                <button class=\"button button-calm icon-right ion-chevron-right ink\" type=\"submit\" translate>\n                  COMMON.BTN_NEXT\n                  <i class=\"icon ion-arrow-right-a\"></i>\n                </button>\n              </div>\n            </div>\n          </form>\n        </ion-content>\n      </ion-slide-page>\n\n      <!-- STEP 4: password-->\n      <ion-slide-page>\n        <ion-content class=\"has-header\" scroll=\"false\">\n          <form name=\"passwordForm\" novalidate=\"\" ng-submit=\"doNext(\'passwordForm\')\">\n\n            <div class=\"item item-text-wrap text-center padding hidden-xs\">\n              <a class=\"pull-right icon-help\" ng-click=\"showHelpModal(\'join-password\')\"></a>\n              <span translate>ACCOUNT.NEW.PASSWORD_WARNING</span>\n            </div>\n\n            <div class=\"list\" ng-init=\"setForm(passwordForm, \'passwordForm\')\">\n\n              <!-- password -->\n              <div class=\"item item-input\" ng-class=\"{ \'item-input-error\': passwordForm.$submitted && passwordForm.password.$invalid}\">\n                <span class=\"input-label\" translate>LOGIN.PASSWORD</span>\n                <input ng-if=\"!showPassword\" name=\"password\" type=\"password\" placeholder=\"{{\'LOGIN.PASSWORD_HELP\' | translate}}\" ng-model=\"formData.password\" autocomplete=\"off\" ng-change=\"formDataChanged()\" ng-minlength=\"8\" different-to=\"formData.username\" required>\n                <input ng-if=\"showPassword\" name=\"text\" type=\"text\" placeholder=\"{{\'LOGIN.PASSWORD_HELP\' | translate}}\" ng-model=\"formData.password\" autocomplete=\"off\" ng-change=\"formDataChanged()\" ng-minlength=\"8\" different-to=\"formData.username\" required>\n              </div>\n              <div class=\"form-errors\" ng-show=\"passwordForm.$submitted && passwordForm.password.$error\" ng-messages=\"passwordForm.password.$error\">\n                <div class=\"form-error\" ng-message=\"minlength\">\n                  <span translate=\"ERROR.FIELD_TOO_SHORT_WITH_LENGTH\" translate-values=\"{minLength: 8}\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"required\">\n                  <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"differentTo\">\n                  <span translate=\"ERROR.EQUALS_TO_SALT\"></span>\n                </div>\n              </div>\n\n              <!-- confirm password -->\n              <div class=\"item item-input\" ng-class=\"{ \'item-input-error\': passwordForm.$submitted && passwordForm.confirmPassword.$invalid}\">\n                <span class=\"input-label\" translate>ACCOUNT.NEW.PASSWORD_CONFIRM</span>\n                <input ng-if=\"!showPassword\" name=\"confirmPassword\" type=\"password\" placeholder=\"{{\'ACCOUNT.NEW.PASSWORD_CONFIRM_HELP\' | translate}}\" ng-model=\"formData.confirmPassword\" autocomplete=\"off\" compare-to=\"formData.password\">\n                <input ng-if=\"showPassword\" name=\"confirmPassword\" type=\"text\" placeholder=\"{{\'ACCOUNT.NEW.PASSWORD_CONFIRM_HELP\' | translate}}\" ng-model=\"formData.confirmPassword\" autocomplete=\"off\" compare-to=\"formData.password\">\n              </div>\n              <div class=\"form-errors\" ng-show=\"passwordForm.$submitted && passwordForm.confirmPassword.$error\" ng-messages=\"passwordForm.confirmPassword.$error\">\n                <div class=\"form-error\" ng-message=\"compareTo\">\n                  <span translate=\"ERROR.PASSWORD_NOT_CONFIRMED\"></span>\n                </div>\n              </div>\n\n              <!-- Show values -->\n              <div class=\"item item-toggle dark\">\n                <span translate>COMMON.SHOW_VALUES</span>\n                <label class=\"toggle toggle-royal\">\n                  <input type=\"checkbox\" ng-model=\"showPassword\">\n                  <div class=\"track\">\n                    <div class=\"handle\"></div>\n                  </div>\n                </label>\n              </div>\n            </div>\n\n            <div class=\"padding hidden-xs text-right\">\n              <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n              </button>\n              <button class=\"button button-calm icon-right ion-chevron-right ink\" type=\"submit\" ng-click=\"getRevocationDocument()\" translate>\n                  COMMON.BTN_NEXT\n              </button>\n            </div>\n\n            <div class=\"padding hidden-xs\">\n            </div>\n          </form>\n        </ion-content>\n      </ion-slide-page>\n\n      <!--<cs-extension-point name=\"last-slide\"></cs-extension-point>-->\n\n      <!-- STEP 5: last slide  -->\n      <ion-slide-page>\n        <ion-content class=\"has-header\" scroll=\"false\">\n\n          <!-- Computing -->\n          <div class=\"center padding\" ng-if=\"formData.computing\">\n            <ion-spinner icon=\"android\"></ion-spinner>\n          </div>\n\n          <!-- Account available -->\n          <div ng-if=\"accountAvailable && !formData.computing\">\n            <div class=\"padding text-center\" translate>ACCOUNT.NEW.LAST_SLIDE_CONGRATULATION</div>\n\n            <div class=\"list\">\n\n              <ion-item class=\"item text-center item-text-wrap\">\n                <h3 class=\"gray\" translate>LOGIN.ASSOCIATED_PUBKEY</h3>\n                <!-- do NOT add copy-on-click attribute here - see issue #470-->\n                <h3 class=\"dark bold\">\n                  {{formData.pubkey}}\n                </h3>\n              </ion-item>\n            </div>\n\n            <div class=\"padding hidden-xs text-right\">\n              <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n              </button>\n              <button class=\"button button-positive ink\" ng-click=\"doNewAccount()\" translate>\n                COMMON.BTN_SEND\n                <i class=\"icon ion-android-send\"></i>\n              </button>\n            </div>\n          </div>\n\n          <!-- Existing non-empty account -->\n          <div ng-if=\"!accountAvailable && !formData.computing\">\n\n            <ion-item class=\"item-icon-left item-text-wrap text-center\">\n                <i class=\"icon ion-minus-circled assertive\"></i>\n                <span id=\"modal-license\" trust-as-html=\"\'ERROR.EXISTING_ACCOUNT\'|translate\"></span>\n            </ion-item>\n\n            <div class=\"list\">\n\n              <ion-item class=\"item item-text-wrap item-border\">\n                <div class=\"padding text-center\">\n                  <span class=\"gray text-no-wrap\">\n                    {{formData.pubkey}}\n                  </span>\n                </div>\n              </ion-item>\n\n              <div class=\"padding text-center\">\n                <span translate>ERROR.EXISTING_ACCOUNT_REQUEST</span>\n              </div>\n\n            </div>\n            <div class=\"padding hidden-xs text-left\">\n              <button class=\"button button-assertive icon-left ion-chevron-left ink\" ng-click=\"identifierRecovery()\" translate>\n                COMMON.BTN_MODIFY\n              </button>\n            </div>\n          </div>\n\n        </ion-content>\n      </ion-slide-page>\n    </ion-slides>\n  \n</ion-modal-view>\n");
$templateCache.put("templates/join/modal_join_wallet.html","<ion-modal-view class=\"modal-full-height\">\n\n  <ion-header-bar class=\"bar-positive\">\n\n    <button class=\"button button-clear visible-xs\" ng-if=\"!slides.slider.activeIndex\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL\n    </button>\n    <button class=\"button button-icon button-clear icon ion-ios-arrow-back buttons header-item\" ng-click=\"slidePrev()\" ng-if=\"slideBehavior.hasPreviousButton\">\n    </button>\n    <button class=\"button button-icon button-clear icon ion-ios-help-outline visible-xs\" ng-if=\"slideBehavior.helpAnchor\" ng-click=\"showHelpModal(slideBehavior.helpAnchor)\">\n    </button>\n\n    <h1 class=\"title\" translate>ACCOUNT.NEW.WALLET_ACCOUNT_TITLE</h1>\n\n    <!-- next -->\n    <button class=\"button button-clear icon-right visible-xs\" ng-if=\"slideBehavior.hasNextButton\" ng-click=\"doNext()\">\n      <span translate>COMMON.BTN_NEXT</span>\n      <i class=\"icon ion-ios-arrow-right\"></i>\n    </button>\n    <!-- send -->\n    <button class=\"button button-clear icon-right visible-xs\" ng-if=\"slideBehavior.hasSendButton\" ng-click=\"doNewAccount()\">\n      <i class=\"icon ion-android-send\"></i>\n    </button>\n  </ion-header-bar>\n\n\n    <ion-slides options=\"slides.options\" slider=\"slides.slider\">\n\n      <!-- STEP 1: salt -->\n      <ion-slide-page>\n        <ion-content class=\"has-header\" scroll=\"false\">\n          <form name=\"saltForm\" novalidate=\"\" ng-submit=\"doNext(\'saltForm\')\">\n\n            <div class=\"list\" ng-init=\"setForm(saltForm, \'saltForm\')\">\n\n              <div class=\"item item-text-wrap text-center padding hidden-xs\">\n                <a class=\"pull-right icon-help\" ng-click=\"showHelpModal(\'join-salt\')\"></a>\n                <span translate>ACCOUNT.NEW.SALT_WARNING</span>\n              </div>\n\n              <!-- salt -->\n              <div class=\"item item-input\" ng-class=\"{ \'item-input-error\': saltForm.$submitted && saltForm.username.$invalid}\">\n                <span class=\"input-label\" translate>LOGIN.SALT</span>\n                <input ng-if=\"!showUsername\" name=\"username\" type=\"password\" placeholder=\"{{\'LOGIN.SALT_HELP\' | translate}}\" ng-change=\"formDataChanged()\" ng-model=\"formData.username\" ng-minlength=\"8\" required>\n                <input ng-if=\"showUsername\" name=\"username\" type=\"text\" placeholder=\"{{\'LOGIN.SALT_HELP\' | translate}}\" ng-change=\"formDataChanged()\" ng-model=\"formData.username\" ng-minlength=\"8\" required>\n              </div>\n              <div class=\"form-errors\" ng-show=\"saltForm.$submitted && saltForm.username.$error\" ng-messages=\"saltForm.username.$error\">\n                <div class=\"form-error\" ng-message=\"minlength\">\n                  <span translate=\"ERROR.FIELD_TOO_SHORT_WITH_LENGTH\" translate-values=\"{minLength: 8}\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"required\">\n                  <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n                </div>\n              </div>\n\n              <!-- confirm salt -->\n              <div class=\"item item-input\" ng-class=\"{ \'item-input-error\': saltForm.$submitted && saltForm.confirmSalt.$invalid}\">\n                <span class=\"input-label pull-right\" translate>ACCOUNT.NEW.SALT_CONFIRM</span>\n                <input ng-if=\"!showUsername\" name=\"confirmUsername\" type=\"password\" placeholder=\"{{\'ACCOUNT.NEW.SALT_CONFIRM_HELP\' | translate}}\" ng-model=\"formData.confirmUsername\" compare-to=\"formData.username\">\n                <input ng-if=\"showUsername\" name=\"confirmUsername\" type=\"text\" placeholder=\"{{\'ACCOUNT.NEW.SALT_CONFIRM_HELP\' | translate}}\" ng-model=\"formData.confirmUsername\" compare-to=\"formData.username\">\n              </div>\n              <div class=\"form-errors\" ng-show=\"saltForm.$submitted && saltForm.confirmUsername.$error\" ng-messages=\"saltForm.confirmUsername.$error\">\n                <div class=\"form-error\" ng-message=\"compareTo\">\n                  <span translate=\"ERROR.SALT_NOT_CONFIRMED\"></span>\n                </div>\n              </div>\n\n              <!-- Show values -->\n              <div class=\"item item-toggle dark\">\n                <span translate>COMMON.SHOW_VALUES</span>\n                <label class=\"toggle toggle-royal\">\n                  <input type=\"checkbox\" ng-model=\"showUsername\">\n                  <div class=\"track\">\n                    <div class=\"handle\"></div>\n                  </div>\n                </label>\n              </div>\n\n              <div class=\"padding hidden-xs text-right\">\n                <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n                </button>\n                <button class=\"button button-calm icon-right ion-chevron-right ink\" type=\"submit\" translate>\n                  COMMON.BTN_NEXT\n                  <i class=\"icon ion-arrow-right-a\"></i>\n                </button>\n              </div>\n            </div>\n          </form>\n        </ion-content>\n      </ion-slide-page>\n\n      <!-- STEP 2: password-->\n      <ion-slide-page>\n        <ion-content class=\"has-header\" scroll=\"false\">\n          <form name=\"passwordForm\" novalidate=\"\" ng-submit=\"doNext(\'passwordForm\')\">\n\n            <div class=\"item item-text-wrap text-center padding hidden-xs\">\n              <a class=\"pull-right icon-help\" ng-click=\"showHelpModal(\'join-password\')\"></a>\n              <span translate>ACCOUNT.NEW.PASSWORD_WARNING</span>\n            </div>\n\n            <div class=\"list\" ng-init=\"setForm(passwordForm, \'passwordForm\')\">\n\n              <!-- password -->\n              <div class=\"item item-input\" ng-class=\"{ \'item-input-error\': passwordForm.$submitted && passwordForm.password.$invalid}\">\n                <span class=\"input-label\" translate>LOGIN.PASSWORD</span>\n                <input ng-if=\"!showPassword\" name=\"password\" type=\"password\" placeholder=\"{{\'LOGIN.PASSWORD_HELP\' | translate}}\" ng-model=\"formData.password\" ng-change=\"formDataChanged()\" ng-minlength=\"8\" required>\n                <input ng-if=\"showPassword\" name=\"text\" type=\"text\" placeholder=\"{{\'LOGIN.PASSWORD_HELP\' | translate}}\" ng-model=\"formData.password\" ng-change=\"formDataChanged()\" ng-minlength=\"8\" required>\n              </div>\n              <div class=\"form-errors\" ng-show=\"passwordForm.$submitted && passwordForm.password.$error\" ng-messages=\"passwordForm.password.$error\">\n                <div class=\"form-error\" ng-message=\"minlength\">\n                  <span translate=\"ERROR.FIELD_TOO_SHORT_WITH_LENGTH\" translate-values=\"{minLength: 8}\"></span>\n                </div>\n                <div class=\"form-error\" ng-message=\"required\">\n                  <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n                </div>\n              </div>\n\n              <!-- confirm password -->\n              <div class=\"item item-input\" ng-class=\"{ \'item-input-error\': passwordForm.$submitted && passwordForm.confirmPassword.$invalid}\">\n                <span class=\"input-label\" translate>ACCOUNT.NEW.PASSWORD_CONFIRM</span>\n                <input ng-if=\"!showPassword\" name=\"confirmPassword\" type=\"password\" placeholder=\"{{\'ACCOUNT.NEW.PASSWORD_CONFIRM_HELP\' | translate}}\" ng-model=\"formData.confirmPassword\" compare-to=\"formData.password\">\n                <input ng-if=\"showPassword\" name=\"confirmPassword\" type=\"text\" placeholder=\"{{\'ACCOUNT.NEW.PASSWORD_CONFIRM_HELP\' | translate}}\" ng-model=\"formData.confirmPassword\" compare-to=\"formData.password\">\n              </div>\n              <div class=\"form-errors\" ng-show=\"passwordForm.$submitted && passwordForm.confirmPassword.$error\" ng-messages=\"passwordForm.confirmPassword.$error\">\n                <div class=\"form-error\" ng-message=\"compareTo\">\n                  <span translate=\"ERROR.PASSWORD_NOT_CONFIRMED\"></span>\n                </div>\n              </div>\n\n              <!-- Show values -->\n              <div class=\"item item-toggle dark\">\n                <span translate>COMMON.SHOW_VALUES</span>\n                <label class=\"toggle toggle-royal\">\n                  <input type=\"checkbox\" ng-model=\"showPassword\">\n                  <div class=\"track\">\n                    <div class=\"handle\"></div>\n                  </div>\n                </label>\n              </div>\n            </div>\n\n            <div class=\"padding hidden-xs text-right\">\n              <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n              </button>\n              <button class=\"button button-calm icon-right ion-chevron-right ink\" type=\"submit\" translate>\n                  COMMON.BTN_NEXT\n              </button>\n            </div>\n\n            <div class=\"padding hidden-xs\">\n            </div>\n          </form>\n        </ion-content>\n      </ion-slide-page>\n\n\n      <!--<cs-extension-point name=\"last-slide\"></cs-extension-point>-->\n\n      <!-- STEP 3: last slide  -->\n      <ion-slide-page>\n        <ion-content class=\"has-header\" scroll=\"false\">\n\n          <!-- Computing -->\n\n          <div class=\"padding center\" ng-if=\"formData.computing\">\n            <ion-spinner icon=\"android\"></ion-spinner>\n          </div>\n          <!-- ng-if=\"formData.computing\" -->\n\n          <!-- Account available -->\n          <div ng-if=\"accountAvailable && !formData.computing\">\n            <div class=\"padding text-center\" translate>ACCOUNT.NEW.LAST_SLIDE_CONGRATULATION</div>\n\n            <div class=\"list\">\n\n              <ion-item class=\"item item-text-wrap item-border\">\n                <div class=\"dark pull-right padding-right\" ng-if=\"formData.computing\">\n                  <ion-spinner icon=\"android\"></ion-spinner>\n                </div>\n                <span class=\"input-label\" translate>COMMON.PUBKEY</span>\n                <span class=\"gray text-no-wrap\" ng-if=\"formData.computing\" translate>\n                  ACCOUNT.NEW.COMPUTING_PUBKEY\n                </span>\n                <span class=\"gray text-no-wrap\" ng-if=\"formData.pubkey\">\n                  {{formData.pubkey}}\n                </span>\n              </ion-item>\n            </div>\n\n            <div class=\"padding hidden-xs text-right\">\n              <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n              </button>\n              <button class=\"button button-positive ink\" ng-click=\"doNewAccount()\" translate>\n                COMMON.BTN_CREATE\n              </button>\n            </div>\n          </div>\n\n          <!-- Existing non-empty account -->\n          <div ng-if=\"!accountAvailable && !formData.computing\">\n\n            <ion-item class=\"item-icon-left item-text-wrap text-center\">\n                <i class=\"icon ion-android-close active\"></i>\n                <span id=\"modal-license\" translate>ERROR.EXISTING_ACCOUNT</span>\n            </ion-item>\n\n\n            <div class=\"list\">\n\n              <ion-item class=\"item item-text-wrap item-border\">\n                <div class=\"dark pull-right padding-right\" ng-if=\"formData.computing\">\n                  <ion-spinner icon=\"android\"></ion-spinner>\n                </div>\n                <span class=\"gray text-no-wrap\" ng-if=\"formData.computing\" translate>\n                  ACCOUNT.NEW.COMPUTING_PUBKEY\n                </span>\n                <div class=\"padding text-center\">\n                  <span class=\"gray text-no-wrap\" ng-if=\"formData.pubkey\">\n                    {{formData.pubkey}}\n                  </span>\n                </div>\n              </ion-item>\n\n              <div class=\"padding text-center\">\n                <span translate>ERROR.EXISTING_ACCOUNT_REQUEST</span>\n              </div>\n\n            </div>\n            <div class=\"padding hidden-xs text-left\">\n              <button class=\"button button-assertive icon-left ion-chevron-left ink\" ng-click=\"identifierRecovery()\" translate>\n                COMMON.BTN_MODIFY\n              </button>\n            </div>\n          </div>\n\n        </ion-content>\n      </ion-slide-page>\n  \n</ion-slides></ion-modal-view>\n");
$templateCache.put("templates/login/form_file_import.html","<div class=\"item\">\n  <p class=\"item-text-wrap\" translate>LOGIN.FILE_FORM_HELP</p>\n</div>\n\n\n<div class=\"item item-icon-left item-text-wrap\">\n  <i class=\"icon ion-ios-information-outline positive\"></i>\n  <span class=\"positive\" translate>LOGIN.FILE.HELP</span>\n</div>\n\n<div class=\"dropzone\" dropzone=\"onKeyFileDrop(file)\">\n  <div ng-if=\"!formData.file\" onclick=\"angular.element(document.querySelector(\'#loginImportFile\'))[0].click();\">\n    <h2 class=\"gray\" translate>COMMON.CHOOSE_FILE</h2>\n    <input type=\"file\" id=\"loginImportFile\" accept=\".yml\" style=\"visibility:hidden; position:absolute\" onchange=\"angular.element(this).scope().fileChanged(event)\">\n  </div>\n\n  <div class=\"item item-icon-left item-icon-right stable-bg\" ng-if=\"formData.file\">\n    <i class=\"icon ion-document-text dark\"></i>\n    <div class=\"item-content row\">\n      <div class=\"col\">\n        <h2>\n          {{formData.file.name}}\n        </h2>\n        <h4 class=\"dark\" ng-if=\"formData.file.lastModified\">\n          <span class=\"gray\" translate>LOGIN.FILE.DATE</span> {{formData.file.lastModified/1000|formatDate}}\n        </h4>\n        <h5 class=\"dark\">\n          <span class=\"gray\" translate>LOGIN.FILE.SIZE</span> {{formData.file.size|formatInteger}} Ko\n        </h5>\n      </div>\n\n      <div class=\"col\">\n        <h3>\n          <span class=\"gray\" translate>COMMON.PUBKEY</span>\n          <ion-spinner class=\"ion-spinner-small\" icon=\"android\" ng-if=\"validatingFile\"></ion-spinner>\n        </h3>\n        <h3 ng-if=\"!validatingFile\">\n\n          <span class=\"dark animate-show-hide ng-hide\" ng-show=\"formData.file.pubkey\">{{formData.file.pubkey}}</span>\n          <span class=\"assertive animate-show-hide ng-hide\" ng-show=\"!formData.file.valid\"><br>\n            <i class=\"ion-close-circled assertive\"></i>\n            {{formData.file.pubkey ? \'ERROR.AUTH_INVALID_PUBKEY\' : \'ERROR.AUTH_INVALID_FILE\' |translate}}\n          </span>\n        </h3>\n      </div>\n    </div>\n\n    <!--  -->\n    <a class=\"ion-close-round gray pull-right\" style=\"font-size: 10px; position: absolute; top: 6px; right: 6px\" ng-click=\"removeKeyFile()\"></a>\n  </div>\n</div>\n\n\n<!-- keep auth after login (checkbox)  -->\n<ion-checkbox ng-model=\"formData.keepAuth\" class=\"item ink item-text-wrap\">\n  <div class=\"item-content dark\" translate>LOGIN.MEMORIZE_AUTH_FILE</div>\n</ion-checkbox>\n\n\n");
$templateCache.put("templates/login/form_login.html","\n  <form name=\"loginForm\" novalidate=\"\" ng-submit=\"doLogin()\" autocomplete=\"off\">\n\n   <div class=\"list padding no-padding-xs\" ng-init=\"setForm(loginForm)\" ng-switch on=\"formData.method\">\n\n     <div class=\"item hidden-xs no-padding\" ng-if=\"showMethods\">\n       <div class=\"pull-right\">\n          <a class=\"button button-text button-small-padding icon-right ink\" ng-click=\"showMethodsPopover($event)\">\n            <i class=\"icon ion-wrench\"></i>\n            {{\'LOGIN.BTN_METHODS\'| translate}}\n          </a>&nbsp;\n          <a class=\"button button-icon positive button-small-padding icon ion-ios-help-outline\" style=\"right: 8px\" ng-click=\"showHelpModal(\'login-method\')\">\n          </a>\n       </div>\n     </div>\n\n      <!-- Form content, depending of the login method -->\n      <div ng-switch-when=\"SCRYPT_DEFAULT\">\n        <ng-include src=\"\'templates/login/form_scrypt.html\'\"></ng-include>\n      </div>\n      <div ng-switch-when=\"SCRYPT_ADVANCED\">\n        <ng-include src=\"\'templates/login/form_scrypt_advanced.html\'\"></ng-include>\n      </div>\n      <div ng-switch-when=\"PUBKEY\">\n        <ng-include src=\"\'templates/login/form_pubkey.html\'\"></ng-include>\n      </div>\n      <div ng-switch-when=\"FILE\">\n        <ng-include src=\"\'templates/login/form_file_import.html\'\"></ng-include>\n      </div>\n      <div ng-switch-default>\n        <ng-include src=\"\'templates/login/form_scrypt.html\'\"></ng-include>\n      </div>\n    </div>\n\n    <div class=\"padding hidden-xs text-right\">\n      <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n      </button>\n      <button class=\"button button-positive ink\" ng-class=\"{\'button-assertive\': isAuth, \'button-positive\': !isAuth}\" type=\"submit\">\n        {{isAuth ? \'AUTH.BTN_AUTH\' : \'COMMON.BTN_LOGIN\' | translate}}\n      </button>\n    </div>\n\n    <div class=\"text-center no-padding visible-xs\">\n      <br>\n      <a ng-click=\"showMethodsPopover($event)\">\n        <i class=\"ion-loop\"></i>\n        <span translate>LOGIN.BTN_METHODS_DOTS</span>\n      </a>\n      <br><br>\n    </div>\n\n    <!-- Register ? -->\n    <ng-if ng-if=\"!isAuth\">\n      <div class=\"text-center no-padding\">\n        {{\'LOGIN.NO_ACCOUNT_QUESTION\'|translate}}\n        <br class=\"visible-xs\">\n        <a ng-click=\"showJoinModal()\" translate>\n          LOGIN.CREATE_ACCOUNT\n        </a>\n      </div>\n\n      <br class=\"visible-xs\">\n\n      <div class=\"text-center no-padding\">\n        <a ng-click=\"showAccountSecurityModal()\" translate>\n          LOGIN.FORGOTTEN_ID\n        </a>\n      </div>\n    </ng-if>\n  </form>\n");
$templateCache.put("templates/login/form_pubkey.html","<div class=\"item\">\n  <p class=\"item-text-wrap\" translate>LOGIN.PUBKEY_FORM_HELP</p>\n</div>\n\n<!-- pubkey  -->\n<div class=\"item item-input item-button-right\" ng-class=\"{ \'item-input-error\': form.$submitted && form.pubkey.$invalid}\">\n  <span class=\"input-label hidden-xs\" translate>COMMON.PUBKEY</span>\n  <input name=\"pubkey\" type=\"text\" placeholder=\"{{\'LOGIN.PUBKEY_HELP\' | translate}}\" ng-model=\"formData.pubkey\" ng-model-options=\"{ debounce: 650 }\" ng-pattern=\"pubkeyPattern\" required>\n  <a class=\"button button-stable icon ion-android-search ink\" ng-click=\"showWotLookupModal()\">\n  </a>\n</div>\n<div class=\"form-errors\" ng-show=\"form.$submitted && form.pubkey.$error\" ng-messages=\"form.pubkey.$error\">\n  <div class=\"form-error\" ng-message=\"required\">\n    <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n  </div>\n  <div class=\"form-error\" ng-message=\"pattern\">\n    <span translate=\"ERROR.INVALID_PUBKEY\"></span>\n  </div>\n</div>\n\n");
$templateCache.put("templates/login/form_scrypt.html","\n  <div class=\"item item-text-wrap\">\n    <p ng-bind-html=\"isAuth ? \'AUTH.SCRYPT_FORM_HELP\': \'LOGIN.SCRYPT_FORM_HELP\'|translate\"></p>\n  </div>\n\n  <!-- avoid web browser to fill password automatically -->\n  <input type=\"password\" name=\"fake-password\" autocomplete=\"off\" style=\"visibility:hidden; position:absolute\">\n\n  <!-- salt (=username, to enable browser login cache) -->\n  <label class=\"item item-input\" ng-class=\"{ \'item-input-error\': form.$submitted && form.username.$invalid}\">\n    <span class=\"input-label hidden-xs\" translate>LOGIN.SALT</span>\n    <input name=\"username\" type=\"password\" placeholder=\"{{\'LOGIN.SALT_HELP\' | translate}}\" autocomplete=\"off\" ng-model=\"formData.username\" ng-model-options=\"{ debounce: 650 }\" class=\"highlight-light\" required>\n  </label>\n  <div class=\"form-errors\" ng-show=\"form.$submitted && form.username.$error\" ng-messages=\"form.username.$error\">\n    <div class=\"form-error\" ng-message=\"required\">\n      <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n    </div>\n  </div>\n\n  <!-- password-->\n  <label class=\"item item-input\" ng-class=\"{ \'item-input-error\': form.$submitted && form.password.$invalid}\">\n    <span class=\"input-label hidden-xs\" translate>LOGIN.PASSWORD</span>\n    <input name=\"password\" type=\"password\" placeholder=\"{{\'LOGIN.PASSWORD_HELP\' | translate}}\" autocomplete=\"off\" ng-model=\"formData.password\" ng-model-options=\"{ debounce: 650 }\" select-on-click required>\n  </label>\n  <div class=\"form-errors\" ng-show=\"form.$submitted && form.password.$error\" ng-messages=\"form.password.$error\">\n    <div class=\"form-error\" ng-message=\"required\">\n      <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n    </div>\n  </div>\n\n  <div class=\"item item-icon-right item-text-wrap\" ng-class=\"{ \'item-input-error\': pubkeyError, \'item-input\': showPubkey}\">\n    <span class=\"input-label hidden-xs animate-show-hide ng-hide\" ng-show=\"showPubkey\" translate>COMMON.PUBKEY</span>\n    <div class=\"item-content text-wrap\">\n      <a class=\"positive ink animate-show-hide ng-hide\" ng-show=\"showComputePubkeyButton && !pubkey\" ng-click=\"computePubkey()\">\n        <i class=\"ion-eye\"></i>\n        {{\'COMMON.BTN_SHOW_PUBKEY\' | translate}}\n      </a>\n      <span class=\"gray animate-show-hide\" ng-show=\"!computing && pubkey\">{{pubkey}}</span>\n      <ion-spinner class=\"ion-spinner-small\" icon=\"android\" ng-if=\"computing\"></ion-spinner>\n    </div>\n    <a class=\"button button-icon positive button-small-padding icon ion-ios-help-outline animate-show-hide\" ng-click=\"showHelpModal(\'login-pubkey\')\" ng-if=\"!expectedPubkey\" ng-show=\"showPubkey\">\n    </a>\n    <span class=\"button button-icon balanced button-small-padding icon ion-checkmark animate-show-hide\" ng-if=\"expectedPubkey\" ng-show=\"showPubkey && !showComputePubkeyButton && !computing && !pubkeyError\">\n    </span>\n  </div>\n  <div class=\"form-errors\" ng-if=\"expectedPubkey\">\n    <div class=\"form-error\" ng-show=\"pubkeyError\">\n      <span trust-as-html=\"::\'ERROR.AUTH_INVALID_PUBKEY\'|translate:{pubkey: expectedPubkey}\"></span>\n    </div>\n  </div>\n\n");
$templateCache.put("templates/login/form_scrypt_advanced.html","\n<!-- Scrypt method: params -->\n<div class=\"row responsive-md responsive-sm padding-left\">\n  <div class=\"col col-33 no-padding\">\n    <label class=\"item item-input item-select\">\n      <select ng-model=\"formData.scrypt\" style=\"max-width: 100%\" ng-change=\"changeScrypt(formData.scrypt)\" ng-options=\"l as (l.label | translate) for l in scryptParamsValues track by l.id\">\n      </select>\n    </label>\n  </div>\n  <div class=\"col no-padding\">\n    <label class=\"item item-input\">\n      <span class=\"input-label\" translate>LOGIN.SCRYPT.N</span>\n      <input class=\"no-padding-right\" type=\"number\" placeholder=\"N\" ng-model=\"formData.scrypt.params.N\" ng-model-options=\"{ debounce: 650 }\" ng-change=\"onScryptFormChanged()\" required>\n    </label>\n  </div>\n  <div class=\"col no-padding\">\n    <label class=\"item item-input\">\n      <span class=\"input-label\" translate>LOGIN.SCRYPT.r</span>\n      <input class=\"no-padding-right\" type=\"number\" placeholder=\"r\" ng-model=\"formData.scrypt.params.r\" ng-model-options=\"{ debounce: 650 }\" ng-change=\"onScryptFormChanged()\" required>\n    </label>\n  </div>\n  <div class=\"col no-padding\">\n    <label class=\"item item-input\">\n      <span class=\"input-label\" translate>LOGIN.SCRYPT.p</span>\n      <input class=\"no-padding-right\" type=\"number\" placeholder=\"p\" ng-model=\"formData.scrypt.params.p\" ng-model-options=\"{ debounce: 650 }\" ng-change=\"onScryptFormChanged()\" required>\n    </label>\n  </div>\n</div>\n\n\n<!-- WARN: not implemented yet -->\n<p class=\"energized-100-bg padding\">\n  <i class=\"icon ion-android-warning\"></i>\n  <span translate>INFO.FEATURES_NOT_IMPLEMENTED</span>\n</p>\n\n<ng-include src=\"\'templates/login/form_scrypt.html\'\"></ng-include>\n\n");
$templateCache.put("templates/login/item_remember_me.html","<!-- remember me (checkbox) -->\n<ion-checkbox ng-model=\"formData.rememberMe\" ng-if=\"!isAuth\" class=\"item ink item-text-wrap\">\n  <div class=\"item-content dark\" translate>SETTINGS.REMEMBER_ME</div>\n</ion-checkbox>\n");
$templateCache.put("templates/login/modal_login.html","<ion-modal-view class=\"modal-full-height modal-login\">\n  <ion-header-bar class=\"\" ng-class=\"{\'bar-positive\': !isAuth, \'bar-assertive\': isAuth}\">\n    <button class=\"button button-clear visible-xs\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL\n    </button>\n    <h1 class=\"title\" ng-bind-html=\"isAuth ? \'AUTH.TITLE\' : \'LOGIN.TITLE\' | translate\">\n    </h1>\n    <div class=\"buttons buttons-right\">\n      <div class=\"secondary-buttons\">\n        <button class=\"button button-positive button-icon button-clear icon ion-android-done visible-xs\" ng-click=\"doLogin()\">\n        </button>\n      </div>\n    </div>\n\n  </ion-header-bar>\n\n  <ion-content>\n    <ng-include src=\"\'templates/login/form_login.html\'\"></ng-include>\n  </ion-content>\n</ion-modal-view>\n");
$templateCache.put("templates/login/popover_methods.html","<ion-popover-view class=\"fit has-header popover-login-methods\" ng-class=\"{\'auth\': isAuth}\">\n  <ion-header-bar>\n    <h1 class=\"title\" translate>LOGIN.METHOD_POPOVER_TITLE</h1>\n  </ion-header-bar>\n  <ion-content scroll=\"false\">\n\n\n    <div class=\"list item-text-wrap\">\n\n      <a class=\"item item-icon-left ink\" ng-click=\"changeMethod(\'SCRYPT_DEFAULT\')\">\n        <i class=\"icon ion-shuffle\" style=\"font-size: 22px\"></i>\n        {{\'LOGIN.METHOD.SCRYPT_DEFAULT\' | translate}}\n      </a>\n\n      <a class=\"item item-icon-left ink\" ng-click=\"changeMethod(\'SCRYPT_ADVANCED\')\">\n        <i class=\"icon ion-shuffle\" style=\"font-size: 22px\"></i>\n        <i class=\"icon-secondary ion-plus\" style=\"font-size: 13px; left: 40px; margin-top: -4px\"></i>\n        {{\'LOGIN.METHOD.SCRYPT_ADVANCED\' | translate}}\n      </a>\n\n      <a class=\"item item-icon-left ink hidden-xs\" ng-click=\"changeMethod(\'FILE\')\">\n        <i class=\"icon ion-document-text\"></i>\n        {{\'LOGIN.METHOD.FILE\' | translate}}\n      </a>\n\n      <ng-if ng-if=\"!isAuth\">\n\n        <div class=\"item-divider\"></div>\n\n        <a class=\"item item-icon-left ink\" ng-click=\"changeMethod(\'PUBKEY\')\">\n          <i class=\"icon ion-key\"></i>\n          {{\'LOGIN.METHOD.PUBKEY\' | translate}}\n        </a>\n\n      </ng-if>\n\n<!--\n      {id: \'PUBKEY\', label: \'LOGIN.METHOD.PUBKEY\'}\n      ];\n      var authMethods = [\n      {id: \'SCRYPT_DEFAULT\', label: \'LOGIN.METHOD.SCRYPT_DEFAULT\'},\n      {\n      id: \'SCRYPT_ADVANCED\', label: \'LOGIN.METHOD.SCRYPT_ADVANCED\',\n      values: _.keys(CryptoUtils.constants.SCRYPT_PARAMS).reduce(function(res, key) {\n      return res.concat({id: key, label: \'LOGIN.SCRYPT.\' + key, params: CryptoUtils.constants.SCRYPT_PARAMS[key]});\n      }, [{id: \'user\', label: \'LOGIN.SCRYPT.USER\', params: {}}])\n      },\n      {id: \'FILE\', label: \'LOGIN.METHOD.FILE\'}\n       -->\n\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/network/item_content_peer.html","\n    <i class=\"icon ion-android-desktop\" ng-class=\":rebind:{\'balanced\': peer.online && peer.hasMainConsensusBlock, \'energized\': peer.online && peer.hasConsensusBlock, \'gray\': peer.online && !peer.hasConsensusBlock && !peer.hasMainConsensusBlock, \'stable\': !peer.online}\" ng-if=\":rebind:!peer.avatar\"></i>\n    <b class=\"icon-secondary ion-person\" ng-if=\":rebind:!peer.avatar\" ng-class=\":rebind:{\'balanced\': peer.online && peer.hasMainConsensusBlock, \'energized\': peer.online && peer.hasConsensusBlock, \'gray\': peer.online && !peer.hasConsensusBlock && !peer.hasMainConsensusBlock, \'stable\': !peer.online}\" style=\"left: 26px; top: -3px\"></b>\n    <i class=\"avatar\" ng-if=\":rebind:peer.avatar\" style=\"background-image: url(\'{{:rebind:peer.avatar.src}}\')\"></i>\n    <b class=\"icon-secondary assertive ion-close-circled\" ng-if=\":rebind:!peer.online\" style=\"left: 37px; top: -10px\"></b>\n\n    <div class=\"row no-padding\">\n      <div class=\"col no-padding\">\n        <h3 class=\"dark\" ng-if=\":rebind:!peer.bma.private\">{{:rebind:peer.dns || peer.server}}</h3>\n        <h4 class=\"gray\" ng-if=\":rebind:peer.bma.private\"><i class=\"ion-flash\"></i> {{\'NETWORK.VIEW.PRIVATE_ACCESS\'|translate}}</h4>\n        <h4>\n          <span class=\"gray\" ng-if=\":rebind:!peer.uid\">\n            <i class=\"ion-key\"></i> {{:rebind:peer.pubkey|formatPubkey}}\n          </span>\n          <span class=\"positive\" ng-if=\":rebind:peer.uid\">\n            <i class=\"ion-person\"></i> {{:rebind:peer.name || peer.uid}}\n          </span>\n          <span class=\"gray\">{{:rebind:peer.dns && (\' | \' + peer.server) + (peer.bma.path||\'\') }}</span>\n        </h4>\n      </div>\n      <div class=\"col col-15 no-padding text-center hidden-xs hidden-sm\" ng-if=\"::expertMode\">\n        <div style=\"min-width: 50px; padding-top: 5px\" title=\"SSL\">\n          <span ng-if=\":rebind:peer.isSsl()\">\n            <i class=\"ion-locked\"></i><small class=\"hidden-md\"> SSL</small>\n          </span>\n          <span ng-if=\":rebind:peer.isWs2p()\" ng-click=\"showWs2pPopover($event, peer)\" title=\"WS2P\">\n            <i class=\"ion-arrow-swap\"></i><small class=\"hidden-md\"> WS2P</small>\n          </span>\n        </div>\n        <div ng-if=\":rebind:!peer.isWs2p()&&peer.hasEndpoint(\'ES_USER_API\')\" ng-click=\"showEndpointsPopover($event, peer, \'ES_USER_API\')\" title=\"Cesium+\">\n          <i class=\"ion-es-user-api\"></i>\n          <b class=\"ion-plus dark\" style=\"position: relative; left: -14px; top:-17px; font-size : 16px\"></b>\n        </div>\n        <div ng-if=\":rebind:!peer.isWs2p()&&peer.isTor()\" ng-click=\"showEndpointsPopover($event, peer, \'BMATOR\')\">\n          <i class=\"ion-bma-tor-api\"></i>\n        </div>\n        <div ng-if=\":rebind:peer.isWs2p()&&peer.isTor()\" ng-click=\"showWs2pPopover($event, peer)\">\n          <i class=\"ion-bma-tor-api\"></i>\n        </div>\n      </div>\n      <div class=\"col col-20 no-padding text-center\" ng-if=\"::expertMode && search.type != \'offline\'\">\n        <h3 class=\"hidden-sm hidden-xs gray\">\n          <span ng-if=\":rebind:peer.uid\"><i class=\"ion-lock-combination\"></i>{{:rebind:peer.difficulty||\'?\'}}</span>\n          <span ng-if=\":rebind:!peer.uid\" translate>PEER.MIRROR</span>\n        </h3>\n        <h4 class=\"hidden-sm hidden-xs gray\">{{:rebind: peer.version ? (\'v\'+peer.version) : \'\'}}</h4>\n      </div>\n      <div class=\"col col-20 no-padding text-center\">\n        <span id=\"{{$index === 0 ? helptipPrefix + \'-peer-0-block\' : \'\'}}\" class=\"badge\" ng-class=\":rebind:{\'badge-balanced\': peer.hasMainConsensusBlock, \'badge-energized\': peer.hasConsensusBlock, \'ng-hide\': !peer.currentNumber }\">\n          {{::!expertMode ? (\'COMMON.BLOCK\'|translate) : \'\' }}\n          {{:rebind:peer.currentNumber|formatInteger}}</span>\n        <span class=\"badge badge-secondary\" ng-if=\":rebind:peer.consensusBlockDelta && expertMode\">\n          <i class=\"ion-clock\"></i>&nbsp;\n          {{:rebind:peer.consensusBlockDelta|formatDurationTime}}</span>\n\n      </div>\n    </div>\n");
$templateCache.put("templates/network/items_peers.html","<div ng-class=\"::motion.ionListClass\" class=\"no-padding\">\n\n  <div class=\"item item-text-wrap no-border done in gray no-padding-top no-padding-bottom inline text-italic\" ng-if=\"::isHttps && expertMode\">\n    <small><i class=\"icon ion-alert-circled\"></i> {{\'NETWORK.INFO.ONLY_SSL_PEERS\'|translate}}</small>\n  </div>\n\n  <div class=\"item row row-header hidden-xs hidden-sm done in\" ng-if=\"::expertMode\">\n    <a class=\"col col-header no-padding dark\" ng-click=\"toggleSort(\'uid\')\">\n      <cs-sort-icon asc=\"search.asc\" sort=\"search.sort\" toggle=\"\'uid\'\"></cs-sort-icon>\n      {{\'COMMON.UID\' | translate}} / {{\'COMMON.PUBKEY\' | translate}}\n    </a>\n    <a class=\"no-padding dark hidden-md col col-15 col-header\" ng-click=\"toggleSort(\'api\')\">\n      <cs-sort-icon asc=\"search.asc\" sort=\"search.sort\" toggle=\"\'api\'\"></cs-sort-icon>\n      {{\'PEER.API\' | translate}}\n    </a>\n    <a class=\"no-padding dark col col-20 col-header\" ng-click=\"toggleSort(\'difficulty\')\">\n      <cs-sort-icon asc=\"search.asc\" sort=\"search.sort\" toggle=\"\'difficulty\'\"></cs-sort-icon>\n      {{\'PEER.DIFFICULTY\' | translate}}\n    </a>\n    <a class=\"col col-20 col-header no-padding dark\" ng-click=\"toggleSort(\'current_block\')\">\n      <cs-sort-icon asc=\"search.asc\" sort=\"search.sort\" toggle=\"\'current_block\'\"></cs-sort-icon>\n      {{\'PEER.CURRENT_BLOCK\' | translate}}\n    </a>\n  </div>\n\n  <div ng-repeat=\"peer in :rebind:search.results track by peer.id\" class=\"item item-peer item-icon-left ink\" ng-class=\"::ionItemClass\" id=\"{{helptipPrefix}}-peer-{{$index}}\" ng-click=\"selectPeer(peer)\" ng-include=\"\'templates/network/item_content_peer.html\'\">\n  </div>\n\n</div>\n");
$templateCache.put("templates/network/lookup_popover_actions.html","<ion-popover-view class=\"fit has-header\">\n  <ion-header-bar>\n    <h1 class=\"title\" translate>PEER.POPOVER_FILTER_TITLE</h1>\n  </ion-header-bar>\n  <ion-content scroll=\"false\">\n    <div class=\"list item-text-wrap\">\n\n      <a class=\"item item-icon-left item-icon-right ink\" ng-click=\"toggleSearchType(\'member\')\">\n        <i class=\"icon ion-person\"></i>\n        {{\'PEER.MEMBERS\' | translate}}\n        <i class=\"icon ion-ios-checkmark-empty\" ng-show=\"search.type==\'member\'\"></i>\n      </a>\n\n      <a class=\"item item-icon-left item-icon-right ink\" ng-click=\"toggleSearchType(\'mirror\')\">\n        <i class=\"icon ion-radio-waves\"></i>\n        {{\'PEER.MIRRORS\' | translate}}\n        <i class=\"icon ion-ios-checkmark-empty\" ng-show=\"search.type==\'mirror\'\"></i>\n      </a>\n\n      <a class=\"item item-icon-left item-icon-right ink\" ng-click=\"toggleSearchType(\'offline\')\">\n        <i class=\"icon ion-eye-disabled\"></i>\n        {{\'PEER.OFFLINE\' | translate}}\n        <i class=\"icon ion-ios-checkmark-empty\" ng-show=\"search.type==\'offline\'\"></i>\n      </a>\n\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/network/modal_network.html","<ion-modal-view id=\"nodes\" class=\"modal-full-height\" cache-view=\"false\">\n  <ion-header-bar class=\"bar-positive\">\n    <button class=\"button button-clear\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL</button>\n    <h1 class=\"title\" translate>PEER.PEER_LIST</h1>\n    <div class=\"buttons buttons-right header-item\">\n      <span class=\"secondary\">\n        <button class=\"button button-clear icon ion-loop button-clear\" ng-click=\"refresh()\">\n\n        </button>\n        <button class=\"button button-icon button-clear icon ion-android-more-vertical visible-xs visible-sm\" ng-click=\"showActionsPopover($event)\">\n        </button>\n      </span>\n    </div>\n  </ion-header-bar>\n\n  <ion-content>\n    <div class=\"list\">\n      <div class=\"padding padding-xs\" style=\"display: block; height: 60px\">\n\n        <div class=\"pull-left\">\n          <h4 ng-if=\"enableFilter && search.type==\'member\'\">\n            {{\'PEER.MEMBERS\' | translate}} <span ng-if=\"!search.loading\">({{search.results.length}})</span>\n          </h4>\n          <h4 ng-if=\"enableFilter && search.type==\'mirror\'\">\n            {{\'PEER.MIRRORS\' | translate}} <span ng-if=\"!search.loading\">({{search.results.length}})</span>\n          </h4>\n          <h4 ng-if=\"!enableFilter || !search.type\">\n            {{\'PEER.ALL_PEERS\' | translate}} <span ng-if=\"!search.loading\">({{search.results.length}})</span>\n          </h4>\n        </div>\n\n        <div class=\"pull-right\">\n          <ion-spinner class=\"icon\" icon=\"android\" ng-if=\"search.loading\"></ion-spinner>&nbsp;\n\n          <div class=\"pull-right\">\n            <a class=\"button button-text button-small hidden-xs hidden-sm ink\" ng-if=\"enableFilter\" ng-class=\"{\'button-text-positive\': search.type==\'member\'}\" ng-click=\"toggleSearchType(\'member\')\">\n              <i class=\"icon ion-person\"></i>\n              {{\'PEER.MEMBERS\'|translate}}\n            </a>\n            &nbsp;\n            <a class=\"button button-text button-small hidden-xs hidden-sm ink\" ng-if=\"enableFilter\" ng-class=\"{\'button-text-positive\': search.type==\'mirror\'}\" ng-click=\"toggleSearchType(\'mirror\')\">\n              <i class=\"icon ion-ios-infinite\"></i>\n              {{\'PEER.MIRRORS\'|translate}}\n            </a>\n          </div>\n        </div>\n      </div>\n\n      <ng-include src=\"\'templates/network/items_peers.html\'\"></ng-include>\n\n	  </div>\n  </ion-content>\n</ion-modal-view>\n");
$templateCache.put("templates/network/popover_endpoints.html","<ion-popover-view class=\"popover-endpoints popover-light\" style=\"height: {{(titleKey?30:0)+((!items || items.length &lt;= 1) ? 55 : 3+items.length*52)}}px\">\n  <ion-header-bar class=\"bar bar-header stable-bg\" ng-if=\"titleKey\">\n    <div class=\"title\">\n      {{titleKey | translate:titleValues }}\n    </div>\n  </ion-header-bar>\n  <ion-content scroll=\"false\">\n    <div class=\"list\" ng-class=\"{\'has-header\': titleKey}\">\n      <div class=\"item item-text-wrap\" ng-repeat=\"item in items\">\n        <div class=\"item-label\" ng-if=\"item.label\">{{item.label | translate}}</div>\n        <div id=\"endpoint_{{$index}}\" class=\"badge item-note dark\">{{item.value}}\n      </div>\n    </div>\n  </div></ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/network/popover_network.html","<ion-popover-view class=\"fit hidden-xs hidden-sm popover-notification popover-network\" ng-controller=\"NetworkLookupPopoverCtrl\">\n  <ion-header-bar class=\"stable-bg block\">\n    <div class=\"title\">\n      {{\'MENU.NETWORK\'|translate}}\n      <ion-spinner class=\"ion-spinner-small\" icon=\"android\" ng-if=\"search.loading\"></ion-spinner>\n    </div>\n\n    <div class=\"pull-right\">\n      <a ng-class=\"{\'positive\': search.type==\'member\', \'dark\': search.type!==\'member\'}\" ng-click=\"toggleSearchType(\'member\')\" translate>PEER.MEMBERS</a>\n    </div>\n  </ion-header-bar>\n  <ion-content scroll=\"true\">\n    <div class=\"list no-padding\">\n      <ng-include src=\"\'templates/network/items_peers.html\'\"></ng-include>\n    </div>\n  </ion-content>\n\n  <ion-footer-bar class=\"stable-bg block\">\n    <!-- settings -->\n    <div class=\"pull-left\">\n      <a class=\"positive\" ui-sref=\"app.settings\" ng-click=\"closePopover()\" translate>COMMON.NOTIFICATIONS.SETTINGS</a>\n    </div>\n\n    <!-- show all -->\n    <div class=\"pull-right\">\n      <a class=\"positive\" ui-sref=\"app.network\" ng-click=\"closePopover()\" translate>COMMON.NOTIFICATIONS.SHOW_ALL</a>\n    </div>\n  </ion-footer-bar>\n</ion-popover-view>\n");
$templateCache.put("templates/network/popover_peer_info.html","<ion-popover-view class=\"fit hidden-xs hidden-sm popover-notification popover-peer-info\" ng-controller=\"PeerInfoPopoverCtrl\">\n  <ion-header-bar class=\"stable-bg block\">\n    <div class=\"title\">\n      {{\'PEER.VIEW.TITLE\'|translate}}\n    </div>\n  </ion-header-bar>\n  <ion-content scroll=\"true\">\n    <div class=\"center padding\" ng-if=\"loading\">\n      <ion-spinner icon=\"android\"></ion-spinner>\n    </div>\n\n    <div class=\"list no-padding\" ng-if=\"!loading\">\n\n      <div class=\"item\" ng-if=\":rebind:formData.software\">\n        <i class=\"ion-outlet\"></i>\n        {{\'NETWORK.VIEW.SOFTWARE\'|translate}}\n        <div class=\"badge\" ng-class=\":rebind:{\'badge-energized\': formData.isPreRelease, \'badge-assertive\': formData.hasNewRelease }\">\n          {{formData.software}} v{{:rebind:formData.version}}\n        </div>\n        <div class=\"gray badge badge-secondary\" ng-if=\"formData.isPreRelease\">\n          <i class=\"ion-alert-circled\"></i>\n          <span ng-bind-html=\"\'NETWORK.VIEW.WARN_PRE_RELEASE\'|translate: formData.latestRelease\"></span>\n        </div>\n        <div class=\"gray badge badge-secondary\" ng-if=\"formData.hasNewRelease\">\n          <i class=\"ion-alert-circled\"></i>\n          <span ng-bind-html=\"\'NETWORK.VIEW.WARN_NEW_RELEASE\'|translate: formData.latestRelease\"></span>\n        </div>\n      </div>\n\n      <div class=\"item\">\n        <i class=\"ion-locked\"></i>\n        {{\'NETWORK.VIEW.ENDPOINTS.BMAS\'|translate}}\n        <div class=\"badge badge-balanced\" ng-if=\":rebind:formData.useSsl\" translate>COMMON.BTN_YES</div>\n        <div class=\"badge badge-assertive\" ng-if=\":rebind:!formData.useSsl\" translate>COMMON.BTN_NO</div>\n      </div>\n\n      <div class=\"item\">\n        <i class=\"ion-cube\"></i>\n        {{\'BLOCKCHAIN.VIEW.TITLE_CURRENT\'|translate}}\n        <div class=\"badge badge-balanced\">\n          {{:rebind:formData.number | formatInteger}}\n        </div>\n      </div>\n\n      <div class=\"item\">\n          <i class=\"ion-clock\"></i>\n          {{\'CURRENCY.VIEW.MEDIAN_TIME\'|translate}}\n        <div class=\"badge dark\">\n          {{:rebind:formData.medianTime | formatDate}}\n        </div>\n      </div>\n\n      <div class=\"item\">\n        <i class=\"ion-lock-combination\"></i>\n        {{\'CURRENCY.VIEW.POW_MIN\'|translate}}\n        <div class=\"badge dark\">\n          {{:rebind:formData.powMin | formatInteger}}\n        </div>\n      </div>\n\n      <!-- Allow extension here -->\n      <cs-extension-point name=\"default\"></cs-extension-point>\n\n    </div>\n  </ion-content>\n\n  <ion-footer-bar class=\"stable-bg block\">\n    <!-- settings -->\n    <div class=\"pull-left\">\n      <a class=\"positive\" ui-sref=\"app.settings\" ng-click=\"closePopover()\" translate>MENU.SETTINGS</a>\n    </div>\n\n    <!-- show all -->\n    <div class=\"pull-right\">\n      <a class=\"positive\" ui-sref=\"app.view_peer\" ng-click=\"closePopover()\" translate>PEER.BTN_SHOW_PEER</a>\n    </div>\n  </ion-footer-bar>\n</ion-popover-view>\n");
$templateCache.put("templates/network/view_network.html","<ion-view>\n  <ion-nav-title>\n    <span translate>MENU.NETWORK</span>\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"secondary\">\n    <button class=\"button button-icon button-clear icon ion-loop visible-xs visible-sm\" ng-click=\"refresh()\">\n    </button>\n  </ion-nav-buttons>\n\n\n  <ion-content scroll=\"true\" ng-init=\"enableFilter=true; ionItemClass=\'item-border-large\';\">\n\n    <div class=\"row responsive-sm responsive-md responsive-lg\">\n      <div class=\"col list col-border-right\">\n        <div class=\"padding padding-xs\" style=\"display: block; height: 60px\">\n          <div class=\"pull-left\">\n            <h4>\n              <span ng-if=\"enableFilter && search.type==\'member\'\" translate>PEER.MEMBERS</span>\n              <span ng-if=\"enableFilter && search.type==\'mirror\'\" translate>PEER.MIRRORS</span>\n              <span ng-if=\"enableFilter && search.type==\'offline\'\" translate>PEER.OFFLINE</span>\n              <span ng-if=\"!enableFilter || !search.type\" translate>PEER.ALL_PEERS</span>\n              <span ng-if=\"search.results.length\">({{search.results.length}})</span>\n              <ion-spinner ng-if=\"search.loading\" class=\"icon ion-spinner-small\" icon=\"android\"></ion-spinner>\n            </h4>\n          </div>\n\n          <div class=\"pull-right\">\n\n            <div class=\"pull-right\" ng-if=\"enableFilter\">\n              <a class=\"button button-text button-small hidden-xs hidden-sm ink\" ng-class=\"{\'button-text-positive\': search.type==\'member\'}\" ng-click=\"toggleSearchType(\'member\')\">\n                <i class=\"icon ion-person-stalker\"></i>\n                {{\'PEER.MEMBERS\'|translate}}\n              </a>\n              &nbsp;\n              <a class=\"button button-text button-small hidden-xs hidden-sm ink\" ng-class=\"{\'button-text-positive\': search.type==\'mirror\'}\" ng-click=\"toggleSearchType(\'mirror\')\">\n                <i class=\"icon ion-radio-waves\"></i>\n                {{\'PEER.MIRRORS\'|translate}}\n              </a>\n\n              <a class=\"button button-text button-small hidden-xs hidden-sm ink\" ng-class=\"{\'button-text-positive\': search.type==\'offline\', \'button-text-stable\': search.type!=\'offline\'}\" ng-click=\"toggleSearchType(\'offline\')\">\n                <i class=\"icon ion-close-circled light-gray\"></i>\n                <span>{{\'PEER.OFFLINE\'|translate}}</span>\n              </a>\n\n              <!-- Allow extension here -->\n              <cs-extension-point name=\"filter-buttons\"></cs-extension-point>\n            </div>\n          </div>\n        </div>\n\n        <div id=\"helptip-network-blockchain\" style=\"display: block\"></div>\n        <div id=\"helptip-network-peers\" style=\"display: block\"></div>\n\n        <ng-include src=\"\'templates/network/items_peers.html\'\"></ng-include>\n      </div>\n\n      <div class=\"col col-33\" ng-controller=\"BlockLookupCtrl\">\n\n        <div class=\"padding padding-xs\" style=\"display: block; height: 100px\">\n          <h4 translate>BLOCKCHAIN.LOOKUP.LAST_BLOCKS</h4>\n\n          <div class=\"pull-right hidden-xs hidden-sm\">\n            <a class=\"button button-text button-small ink\" ng-class=\"{\'button-text-positive\': compactMode, \'button-text-stable\': !compactMode}\" ng-click=\"toggleCompactMode()\">\n              <i class=\"icon ion-navicon\"></i>\n              <b class=\"icon-secondary ion-arrow-down-b\" style=\"top: -8px; left: 5px; font-size: 8px\"></b>\n              <b class=\"icon-secondary ion-arrow-up-b\" style=\"top: 4px; left: 5px; font-size: 8px\"></b>\n              <span>{{\'BLOCKCHAIN.LOOKUP.BTN_COMPACT\'|translate}}</span>\n            </a>\n\n            <!-- Allow extension here -->\n            <cs-extension-point name=\"buttons\"></cs-extension-point>\n\n          </div>\n        </div>\n\n        <ng-include src=\"\'templates/blockchain/list_blocks.html\'\"></ng-include>\n      </div>\n    </div>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/network/view_peer.html","<ion-view>\n  <ion-nav-title>\n    <span translate>PEER.VIEW.TITLE</span>\n  </ion-nav-title>\n\n  <ion-content class=\"has-header\" scroll=\"true\">\n\n    <div class=\"row no-padding\">\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;\n      </div>\n\n      <div class=\"col list\">\n\n        <ion-item>\n          <h1>\n            <span translate>PEER.VIEW.TITLE</span>\n            <span class=\"gray\">\n              {{node.host}}\n            </span>\n          </h1>\n          <h2 class=\"gray\">\n            <i class=\"gray icon ion-android-globe\"></i>\n            {{node.bma.dns || node.server}}\n            <span class=\"gray\" ng-if=\"!loading && node.useSsl\">\n              <i class=\"gray ion-locked\"></i> <small>SSL</small>\n            </span>\n            <span class=\"gray\" ng-if=\"!loading && node.useTor\">\n              <i class=\"gray ion-bma-tor-api\"></i>\n            </span>\n\n            <span class=\"assertive\" ng-if=\"!loading && !node.uid\">({{\'PEER.MIRROR\'|translate}})</span>\n          </h2>\n\n          <!-- node owner -->\n          <h3>\n            <span class=\"dark\">\n              <i class=\"icon ion-android-desktop\"></i>\n              {{\'PEER.VIEW.OWNER\'|translate}}\n            </span>\n            <a class=\"positive\" ng-if=\"node.uid\" ui-sref=\"app.wot_identity({pubkey: node.pubkey, uid: node.uid})\">\n              <i class=\"ion-person\"></i> {{node.name || node.uid}}\n              <span class=\"gray\" ng-if=\"node.name\">\n                ({{node.uid}})\n              </span>\n            </a>\n            <span ng-if=\"!loading && !node.uid\">\n              <a class=\"gray\" ui-sref=\"app.wot_identity({pubkey: node.pubkey})\">\n                <i class=\"ion-key\"></i>\n                {{node.pubkey|formatPubkey}}\n                <span class=\"gray\" ng-if=\"node.name\">\n                  ({{node.name}})\n                </span>\n              </a>\n            </span>\n          </h3>\n\n          <h3>\n            <a ng-click=\"openRawPeering($event)\">\n              <i class=\"icon ion-share\"></i> {{\'PEER.VIEW.SHOW_RAW_PEERING\'|translate}}\n            </a>\n\n            <span class=\"gray\" ng-if=\"!isReachable\"> | </span>\n            <a ng-if=\"!isReachable\" ng-click=\"openRawCurrentBlock($event)\">\n              <i class=\"icon ion-share\"></i> <span translate>PEER.VIEW.SHOW_RAW_CURRENT_BLOCK</span>\n            </a>\n          </h3>\n        </ion-item>\n\n\n        <div class=\"item item-divider\" translate>\n          PEER.VIEW.GENERAL_DIVIDER\n        </div>\n\n        <ion-item class=\"item-icon-left item-text-wrap ink\" copy-on-click=\"{{node.pubkey}}\">\n          <i class=\"icon ion-key\"></i>\n          <span translate>COMMON.PUBKEY</span>\n          <h4 class=\"dark text-left\">{{node.pubkey}}</h4>\n        </ion-item>\n\n        <a class=\"item item-icon-left item-icon-right item-text-wrap ink\" ng-if=\"isReachable\" ui-sref=\"app.view_server_block_hash({server: node.server, ssl: node.useSsl, tor: node.useTor, number: current.number, hash: current.hash})\">\n          <i class=\"icon ion-cube\"></i>\n          <span translate>BLOCKCHAIN.VIEW.TITLE_CURRENT</span>\n          <div class=\"badge badge-calm\" ng-if=\"!loading\">\n            {{current.number|formatInteger}}\n          </div>\n          <i class=\"gray icon ion-ios-arrow-right\"></i>\n        </a>\n\n        <a class=\"item item-icon-left item-icon-right item-text-wrap ink\" ng-if=\"isReachable\" ui-sref=\"app.server_blockchain({server: node.server, ssl: node.useSsl, tor: node.useTor})\">\n          <i class=\"icon ion-cube\" style=\"font-size: 25px\"></i>\n          <i class=\"icon-secondary ion-clock\" style=\"font-size: 18px; left: 33px; top: -12px\"></i>\n          <span translate>PEER.VIEW.LAST_BLOCKS</span>\n          <i class=\"gray icon ion-ios-arrow-right\"></i>\n        </a>\n\n        <!-- Allow extension here -->\n        <cs-extension-point name=\"general\"></cs-extension-point>\n\n        <div class=\"item item-divider\" ng-hide=\"loading || !isReachable\" translate>\n          PEER.VIEW.KNOWN_PEERS\n        </div>\n\n        <ion-item class=\"item item-text-wrap no-border done in gray no-padding-top no-padding-bottom inline text-italic\" ng-show=\"!loading && !isReachable\">\n          <small><i class=\"icon ion-alert-circled\"></i> {{\'NETWORK.INFO.ONLY_SSL_PEERS\'|translate}}</small>\n        </ion-item>\n\n        <div class=\"item center\" ng-if=\"loading\">\n            <ion-spinner class=\"icon\" icon=\"android\"></ion-spinner>\n        </div>\n\n        <div class=\"list no-padding {{::motion.ionListClass}}\" ng-if=\"isReachable\">\n\n          <div ng-repeat=\"peer in :rebind:peers track by peer.id\" class=\"item item-peer item-icon-left ink\" ng-class=\"::ionItemClass\" ng-click=\"selectPeer(peer)\" ng-include=\"\'templates/network/item_content_peer.html\'\">\n          </div>\n\n        </div>\n      </div>\n\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;\n      </div>\n    </div>\n\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/settings/popover_actions.html","<ion-popover-view class=\"fit has-header\">\n  <ion-header-bar>\n    <h1 class=\"title\" translate>COMMON.POPOVER_ACTIONS_TITLE</h1>\n  </ion-header-bar>\n  <ion-content scroll=\"false\">\n    <div class=\"list item-text-wrap\">\n\n      <a class=\"item item-icon-left ink visible-xs visible-sm\" ng-click=\"reset()\">\n        <i class=\"icon ion-refresh\"></i>\n        {{\'SETTINGS.BTN_RESET\' | translate}}\n      </a>\n\n      <!-- help tour -->\n      <a class=\"item item-icon-left ink\" ng-click=\"startSettingsTour()\">\n        <i class=\"icon ion-easel\"></i>\n        {{\'COMMON.BTN_HELP_TOUR_SCREEN\' | translate}}\n      </a>\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/settings/popup_node.html","<form name=\"popupForm\" ng-submit=\"\">\n\n  <div class=\"list no-padding\" ng-init=\"setPopupForm(popupForm)\">\n    <div class=\"item item-input item-floating-label\" ng-class=\"{\'item-input-error\': popupForm.$submitted && popupForm.newNode.$invalid}\">\n      <span class=\"input-label\" ng-bind-html=\"\'SETTINGS.POPUP_PEER.HOST\'|translate\"></span>\n      <input name=\"newNode\" type=\"text\" placeholder=\"{{\'SETTINGS.POPUP_PEER.HOST_HELP\' | translate}}\" ng-model=\"popupData.newNode\" ng-minlength=\"3\" required>\n    </div>\n    <div class=\"form-errors\" ng-if=\"popupForm.$submitted && popupForm.newNode.$error\" ng-messages=\"popupForm.newNode.$error\">\n      <div class=\"form-error\" ng-message=\"required\">\n        <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n      </div>\n      <div class=\"form-error\" ng-message=\"minlength\">\n        <span translate=\"ERROR.FIELD_TOO_SHORT\"></span>\n      </div>\n    </div>\n\n    <div class=\"item item-toggle\">\n      <span class=\"input-label\">\n        {{\'SETTINGS.POPUP_PEER.USE_SSL\' | translate}}\n      </span>\n      <h4>\n        <small class=\"gray\" ng-bind-html=\"\'SETTINGS.POPUP_PEER.USE_SSL_HELP\' | translate\">\n        </small>\n      </h4>\n      <label class=\"toggle toggle-royal no-padding-right\">\n        <input type=\"checkbox\" ng-model=\"popupData.useSsl\">\n        <div class=\"track\">\n          <div class=\"handle\"></div>\n        </div>\n      </label>\n    </div>\n\n\n    <a class=\"button button-positive button-clear positive button-outline button-full button-small-padding icon-left ink no-padding\" ng-click=\"showNodeList()\">\n      <i class=\"icon ion-search\"></i>\n      {{\'SETTINGS.POPUP_PEER.BTN_SHOW_LIST\' | translate}}\n    </a>\n  </div>\n\n  <button type=\"submit\" class=\"hide\"></button>\n</form>\n\n\n\n");
$templateCache.put("templates/settings/settings.html","<ion-view left-buttons=\"leftButtons\" cache-view=\"false\" class=\"settings\">\n  <ion-nav-title translate>SETTINGS.TITLE</ion-nav-title>\n\n  <ion-nav-buttons side=\"secondary\">\n    <button class=\"button button-icon button-clear icon ion-android-more-vertical visible-xs visible-sm\" ng-click=\"showActionsPopover($event)\">\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content>\n\n    <!-- Buttons bar-->\n    <div class=\"padding text-center hidden-xs hidden-sm\">\n      <button class=\"button button-raised button-stable ink\" ng-click=\"reset()\">\n        <i class=\"icon ion-refresh\"></i>\n        {{\'SETTINGS.BTN_RESET\' | translate}}\n      </button>\n\n      <button class=\"button button-stable button-small-padding icon ion-android-more-vertical\" ng-click=\"showActionsPopover($event)\" title=\"{{\'COMMON.BTN_OPTIONS\' | translate}}\">\n      </button>\n    </div>\n\n    <div class=\"list item-border-large\">\n\n      <div class=\"item item-toggle dark\">\n        <div class=\"input-label\">\n          {{\'COMMON.BTN_RELATIVE_UNIT\' | translate}}\n        </div>\n        <label class=\"toggle toggle-royal\" id=\"helptip-settings-btn-unit-relative\">\n            <input type=\"checkbox\" ng-model=\"formData.useRelative\">\n            <div class=\"track\">\n                <div class=\"handle\"></div>\n            </div>\n        </label>\n      </div>\n\n      <label class=\"item item-input item-select\">\n        <div class=\"input-label\">\n          {{\'COMMON.LANGUAGE\' | translate}}\n        </div>\n        <select ng-model=\"formData.locale\" ng-change=\"changeLanguage(formData.locale.id)\" ng-options=\"l as l.label for l in locales track by l.id\">\n        </select>\n      </label>\n\n      <div class=\"item item-text-wrap item-toggle dark\">\n        <div class=\"input-label\">\n         {{\'SETTINGS.USE_LOCAL_STORAGE\' | translate}}\n        </div>\n        <h4 class=\"gray\" ng-bind-html=\"\'SETTINGS.USE_LOCAL_STORAGE_HELP\' | translate\">\n        </h4>\n        <label class=\"toggle toggle-royal\">\n          <input type=\"checkbox\" ng-model=\"formData.useLocalStorage\">\n          <div class=\"track\">\n            <div class=\"handle\"></div>\n          </div>\n        </label>\n      </div>\n\n      <div class=\"item item-toggle dark item-text-wrap\">\n        <div class=\"input-label\" ng-bind-html=\"\'SETTINGS.ENABLE_HELPTIP\' | translate\">\n        </div>\n        <label class=\"toggle toggle-royal\">\n          <input type=\"checkbox\" ng-model=\"formData.helptip.enable\">\n          <div class=\"track\">\n            <div class=\"handle\"></div>\n          </div>\n        </label>\n      </div>\n\n     <!-- <div class=\"item item-toggle dark item-text-wrap\">\n        <div class=\"input-label\" ng-bind-html=\"\'SETTINGS.ENABLE_UI_EFFECTS\' | translate\">\n        </div>\n        <label class=\"toggle toggle-royal\">\n          <input type=\"checkbox\" ng-model=\"formData.enableUuiEffects\" >\n          <div class=\"track\">\n            <div class=\"handle\"></div>\n          </div>\n        </label>\n      </div>-->\n      <!-- Allow extension here -->\n      <cs-extension-point name=\"common\"></cs-extension-point>\n\n      <span class=\"item item-divider\">\n        {{\'SETTINGS.AUTHENTICATION_SETTINGS\' | translate}}\n      </span>\n\n      <div class=\"item item-toggle\">\n        <div class=\"input-label\" ng-class=\"{\'gray\': !formData.useLocalStorage}\">\n          {{\'SETTINGS.REMEMBER_ME\' | translate}}\n        </div>\n        <h4 class=\"gray text-wrap\" ng-bind-html=\"\'SETTINGS.REMEMBER_ME_HELP\' | translate\"></h4>\n\n        <label class=\"toggle toggle-royal\">\n          <input type=\"checkbox\" ng-model=\"formData.rememberMe\" ng-disabled=\"!formData.useLocalStorage\">\n          <div class=\"track\">\n            <div class=\"handle\"></div>\n          </div>\n        </label>\n      </div>\n\n      <div class=\"item item-input item-select\">\n        <div class=\"input-label\">\n          <span class=\"input-label\" translate>SETTINGS.KEEP_AUTH</span>\n          <h4 class=\"gray text-wrap\" ng-bind-html=\"\'SETTINGS.KEEP_AUTH_HELP\' | translate\"></h4>\n        </div>\n\n        <label>\n          <select ng-model=\"formData.keepAuthIdle\" ng-options=\"i as (keepAuthIdleLabels[i].labelKey | translate:keepAuthIdleLabels[i].labelParams ) for i in keepAuthIdles track by i\">\n          </select>\n        </label>\n      </div>\n\n      <span class=\"item item-divider\" translate>SETTINGS.HISTORY_SETTINGS</span>\n\n      <div class=\"item item-toggle dark\">\n        <div class=\"col col-75 input-label\" translate>SETTINGS.DISPLAY_UD_HISTORY</div>\n        <label class=\"toggle toggle-royal\">\n          <input type=\"checkbox\" ng-model=\"formData.showUDHistory\">\n          <div class=\"track\">\n            <div class=\"handle\"></div>\n          </div>\n        </label>\n      </div>\n\n      <!-- Allow extension here -->\n      <cs-extension-point name=\"history\"></cs-extension-point>\n\n      <span class=\"item item-divider\" translate>SETTINGS.NETWORK_SETTINGS</span>\n\n      <div class=\"item ink item-text-wrap\" ng-click=\"changeNode()\">\n        <div class=\"input-label\">\n        {{\'SETTINGS.PEER\' | translate}}\n        </div>\n\n        <!-- node temporary changed -->\n        <ng-if ng-if=\"formData.node.temporary\">\n          <h4 class=\"gray text-wrap assertive\">\n            <i class=\"icon ion-alert-circled\"></i>\n            <span ng-bind-html=\"\'SETTINGS.PEER_CHANGED_TEMPORARY\' | translate \"></span>\n          </h4>\n          <div class=\"item-note assertive text-italic\">{{bma.server}}</div>\n        </ng-if>\n\n        <div class=\"item-note dark\" ng-if=\"!formData.node.temporary\">{{bma.server}}</div>\n      </div>\n\n      <div class=\"item item-text-wrap item-toggle dark hidden-xs hidden-sm\">\n        <div class=\"input-label\" ng-bind-html=\"\'SETTINGS.EXPERT_MODE\' | translate\"></div>\n        <h4 class=\"gray\" ng-bind-html=\"\'SETTINGS.EXPERT_MODE_HELP\' | translate\"></h4>\n        <label class=\"toggle toggle-royal\">\n          <input type=\"checkbox\" ng-model=\"formData.expertMode\">\n          <div class=\"track\">\n            <div class=\"handle\"></div>\n          </div>\n        </label>\n      </div>\n\n      <!-- Allow extension here -->\n      <cs-extension-point name=\"network\"></cs-extension-point>\n\n      <span class=\"item item-divider\" ng-if=\"$root.config.plugins\" translate>SETTINGS.PLUGINS_SETTINGS</span>\n\n      <!-- Allow extension here -->\n      <cs-extension-point name=\"plugins\"></cs-extension-point>\n    </div>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/wallet/item_tx.html","<i class=\"icon item-image\" ng-if=\"::!tx.avatar\" ng-class=\"::{\'ion-person dark\': tx.uid, \'ion-card dark\': !tx.uid}\"></i>\n<i class=\"avatar\" ng-if=\"::tx.avatar\" style=\"background-image: url({{::tx.avatar.src}})\"></i>\n\n<div class=\"row no-padding\">\n  <div class=\"col no-padding\">\n    <b class=\"ion-clock\" ng-if=\"::pending\"> </b>\n    <a class=\"\" ui-sref=\"app.wot_identity({pubkey:tx.pubkey, uid:tx.uid})\" ng-if=\"tx.uid\">\n      {{::tx.name||tx.uid}}\n    </a>\n    <a class=\"gray\" ui-sref=\"app.wot_identity({pubkey:tx.pubkey, uid:tx.uid})\" ng-if=\"!tx.uid\">\n      <i class=\"ion-key gray\"></i>\n      {{::tx.pubkey | formatPubkey}}\n    </a>\n    <p class=\"dark visible-xs width-cup text-italic\" data-toggle=\"tooltip\" ng-if=\"::tx.comment\" title=\"{{::tx.comment}}\">\n      <i class=\"ion-ios-chatbubble-outline\"></i>\n      {{::tx.comment}}<br>\n    </p>\n    <h4>\n      <a ng-if=\"::!pending\" class=\"gray underline\" ui-sref=\"app.view_block({number: tx.block_number})\">\n        {{::tx.time | formatFromNow}} | {{::tx.time | formatDate}}\n      </a>\n      <span ng-if=\"::pending\" class=\"gray\">\n        {{::tx.time | formatFromNow}} | {{::tx.time | formatDate}}\n      </span>\n    </h4>\n  </div>\n  <div class=\"col col-50 no-padding\" ng-if=\"::tx.comment\">\n    <p class=\"vertical-center gray text-italic hidden-xs\" data-toggle=\"tooltip\" title=\"{{::tx.comment}}\">{{::tx.comment}}</p>\n  </div>\n  <div class=\"col col-10 no-padding\">\n\n    <!-- not locked TX -->\n    <span ng-if=\"::!tx.lockedOutputs\" class=\"badge item-note\" ng-class=\"{\'badge-calm\': tx.amount > 0}\">\n      <!--<span class=\"hidden-xs\" ng-if=\":rebind:tx.amount>0\">+</span>-->\n      <span ng-bind-html=\":rebind:tx.amount| formatAmount:{currency:$root.currency.name}\"></span>\n    </span>\n\n    <!-- Locked TX -->\n    <a ng-if=\":rebind:tx.lockedOutputs\" class=\"badge item-note\" ng-class=\"{\'badge-calm\': tx.amount > 0}\" ng-click=\"showLockedOutputsPopover(tx, $event)\">\n      <i class=\"icon ion-locked\"></i>\n      <!--<span class=\"hidden-xs\" ng-if=\":rebind:tx.amount>0\">+</span>-->\n      <span ng-bind-html=\":rebind:tx.amount| formatAmount:{currency:$root.currency.name}\"></span>\n    </a>\n\n    <div class=\"badge badge-secondary\" ng-if=\"$root.settings.expertMode\">\n      (<span ng-bind-html=\":rebind:tx.amount| formatAmount:{useRelative: !$root.settings.useRelative, currency:$root.currency.name}\"></span>)\n    </div>\n  </div>\n</div>\n");
$templateCache.put("templates/wallet/item_ud.html","<i class=\"icon item-image ion-arrow-up-c energized\"></i>\n\n<div class=\"row no-padding\">\n  <div class=\"col no-padding\">\n    <span class=\"energized\" translate>COMMON.UNIVERSAL_DIVIDEND</span>\n    <h4>\n      <a class=\"gray underline\" ui-sref=\"app.view_block({number: tx.block_number})\">\n        {{::tx.time | formatFromNow}} | {{::tx.time | formatDate}}\n      </a>\n    </h4>\n  </div>\n  <div class=\"col col-10 no-padding\">\n\n    <span class=\"badge item-note badge-energized\">\n      <!--<span class=\"hidden-xs\" ng-if=\":rebind:tx.amount>0\">+</span>-->\n      <span ng-bind-html=\":rebind:tx.amount| formatAmount:{currency:$root.currency.name}\"></span>\n    </span>\n\n    <div class=\"badge badge-secondary\" ng-if=\"$root.settings.expertMode\">\n      (<span ng-bind-html=\":rebind:tx.amount| formatAmount:{useRelative: !$root.settings.useRelative, currency:$root.currency.name}\"></span>)\n    </div>\n  </div>\n</div>\n");
$templateCache.put("templates/wallet/modal_security.html","<ion-modal-view class=\"modal-full-height\">\n\n  <ion-header-bar class=\"bar-positive\">\n\n    <button class=\"button button-clear visible-xs\" ng-if=\"!slides.slider.activeIndex\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL\n    </button>\n    <button class=\"button button-icon button-clear icon ion-ios-arrow-back buttons header-item\" ng-click=\"slidePrev()\" ng-if=\"slides.slider.activeIndex\">\n    </button>\n\n    <h1 class=\"title hidden-xs\" translate>ACCOUNT.SECURITY.TITLE</h1>\n\n    <button class=\"button button-clear icon-right visible-xs\" ng-if=\"!isLastSlide && slides.slider.activeIndex > 0\" ng-click=\"doNext()\">\n      <span translate>COMMON.BTN_NEXT</span>\n      <i class=\"icon ion-ios-arrow-right\"></i>\n    </button>\n    <button class=\"button button-positive button-icon button-clear icon ion-android-done visible-xs\" ng-click=\"doNext()\" ng-if=\"isLastSlide && option === \'saveID\'\">\n    </button>\n  </ion-header-bar>\n\n\n    <ion-slides options=\"slides.options\" slider=\"slides.slider\">\n\n      <!-- STEP 1 -->\n      <ion-slide-page>\n        <ion-content class=\"has-header padding\">\n\n          <div class=\"list\">\n\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink\" ng-click=\"selectOption(\'recoverID\')\" ng-if=\"!isLogin\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image dark icon ion-person\"></i>\n                <b class=\"ion-ios-undo icon-secondary dark\" style=\"top: -8px; left: 39px; font-size: 12px\"></b>\n                <h2 translate>ACCOUNT.SECURITY.RECOVER_ID</h2>\n                <h4 class=\"gray\" translate>ACCOUNT.SECURITY.RECOVER_ID_HELP</h4>\n                <i class=\"icon dark ion-ios-arrow-right\"></i>\n              </div>\n            </div>\n\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink\" ng-click=\"selectOption(\'revocation\')\" ng-if=\"!isLogin\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image dark icon ion-person\"></i>\n                <b class=\"ion-close icon-secondary dark\" style=\"top: -8px; left: 39px; font-size: 12px\"></b>\n                <h2 translate>ACCOUNT.SECURITY.REVOCATION_WITH_FILE</h2>\n                <h4 class=\"gray\" translate>ACCOUNT.SECURITY.REVOCATION_WITH_FILE_DESCRIPTION</h4>\n                <i class=\"icon dark ion-ios-arrow-right\"></i>\n              </div>\n            </div>\n\n\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink\" ng-click=\"selectOption(\'saveID\')\" ng-if=\"isLogin\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image dark icon ion-person\"></i>\n                <b class=\"ion-ios-redo icon-secondary dark\" style=\"top: -8px; left: 39px; font-size: 12px\"></b>\n                <b class=\"ion-locked icon-secondary dark\" style=\"top: 0px; left: 40px; font-size: 8px\"></b>\n                <h2 translate>ACCOUNT.SECURITY.SAVE_ID</h2>\n                <h4 class=\"gray\" translate>ACCOUNT.SECURITY.SAVE_ID_HELP</h4>\n                <i class=\"icon dark ion-ios-arrow-right\"></i>\n              </div>\n            </div>\n\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink hidden-xs hidden-sm\" ng-click=\"downloadRevokeFile()\" ng-if=\"isLogin && hasSelf\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image dark icon ion-person\"></i>\n                <b class=\"ion-ios-redo icon-secondary dark\" style=\"top: -8px; left: 39px; font-size: 12px\"></b>\n                <b class=\"ion-close icon-secondary dark\" style=\"top: 0px; left: 40px; font-size: 8px\"></b>\n                <h2 translate>ACCOUNT.SECURITY.DOWNLOAD_REVOKE</h2>\n                <h4 class=\"gray\" translate>ACCOUNT.SECURITY.DOWNLOAD_REVOKE_HELP</h4>\n                <i class=\"icon dark ion-android-archive\"></i>\n              </div>\n            </div>\n\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink\" ng-click=\"self()\" ng-if=\"needSelf\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image icon ion-person dark\"></i>\n                <b class=\"ion-flag icon-secondary dark\" style=\"top: -8px; left: 39px; font-size: 12px\"></b>\n                <h2 translate>ACCOUNT.SECURITY.SEND_IDENTITY</h2>\n                <h4 class=\"gray\" ng-bind-html=\"::\'ACCOUNT.SECURITY.SEND_IDENTITY_HELP\' | translate\"></h4>\n              </div>\n            </div>\n\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink\" ng-click=\"membershipIn()\" ng-if=\"needMembership\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image icon ion-person dark\"></i>\n                <b class=\"ion-plus icon-secondary dark\" style=\"top: -8px; left: 39px; font-size: 12px\"></b>\n                <h2 translate>ACCOUNT.SECURITY.MEMBERSHIP_IN</h2>\n                <h4 class=\"gray\" ng-bind-html=\"::\'ACCOUNT.SECURITY.MEMBERSHIP_IN_HELP\' | translate\"></h4>\n              </div>\n            </div>\n\n            <div class=\"item item-complex card stable-bg item-icon-left item-icon-right ink\" ng-click=\"revokeWalletIdentity()\" ng-if=\"isLogin && hasSelf\">\n              <div class=\"item-content item-text-wrap\">\n                <i class=\"item-image icon ion-person assertive-900\"></i>\n                <b class=\"ion-close icon-secondary assertive-900\" style=\"top: -8px; left: 39px; font-size: 12px\"></b>\n                <h2 translate>ACCOUNT.SECURITY.REVOCATION_WALLET</h2>\n                <h4 class=\"gray\" translate>ACCOUNT.SECURITY.REVOCATION_WALLET_HELP</h4>\n              </div>\n            </div>\n          </div>\n\n          <div class=\"padding hidden-xs text-right\">\n            <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n            </button>\n          </div>\n        </ion-content>\n      </ion-slide-page>\n\n      <ion-slide-page ng-if=\"option == \'revocation\'\">\n        <ng-include src=\"\'templates/wallet/slides/slides_revocation_file.html\'\"></ng-include>\n      </ion-slide-page>\n\n      <ion-slide-page ng-if=\"isLogin && option == \'saveID\'\">\n        <ng-include src=\"\'templates/wallet/slides/slides_saveID_1.html\'\"></ng-include>\n      </ion-slide-page>\n      <ion-slide-page ng-if=\"isLogin && option == \'saveID\'\">\n        <ng-include src=\"\'templates/wallet/slides/slides_saveID_2.html\'\"></ng-include>\n      </ion-slide-page>\n\n      <ion-slide-page ng-if=\"option == \'recoverID\'\">\n        <ng-include src=\"\'templates/wallet/slides/slides_recoverID_1.html\'\"></ng-include>\n      </ion-slide-page>\n      <ion-slide-page ng-if=\"option == \'recoverID\'\">\n        <ng-include src=\"\'templates/wallet/slides/slides_recoverID_2.html\'\"></ng-include>\n      </ion-slide-page>\n      <ion-slide-page ng-if=\"option == \'recoverID\'\">\n        <ng-include src=\"\'templates/wallet/slides/slides_recoverID_3.html\'\"></ng-include>\n      </ion-slide-page>\n\n    </ion-slides>\n</ion-modal-view>\n");
$templateCache.put("templates/wallet/modal_transfer.html","<ion-modal-view id=\"transfer\" class=\"modal-full-height modal-transfer\">\n  <ion-header-bar class=\"bar-positive\">\n    <button class=\"button button-clear visible-xs\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL</button>\n    <h1 class=\"title\" translate>TRANSFER.MODAL.TITLE</h1>\n\n    <button class=\"button button-icon button-clear icon ion-android-send visible-xs\" ng-click=\"doTransfer()\">\n    </button>\n  </ion-header-bar>\n\n  <ion-content scroll=\"true\">\n      <ng-include src=\"\'templates/wallet/transfer_form.html\'\"></ng-include>\n  </ion-content>\n\n  <!-- Digit keyboard - fix #30 -->\n  <ion-digit-keyboard settings=\"digitKeyboardSettings\" ng-if=\"digitKeyboardVisible\"></ion-digit-keyboard>\n</ion-modal-view>\n");
$templateCache.put("templates/wallet/new_transfer.html","<ion-view left-buttons=\"leftButtons\" id=\"transfer\">\n  <ion-nav-title>\n    <span class=\"visible-xs visible-sm\" translate>TRANSFER.TITLE</span>\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"secondary\">\n      <button class=\"button button-icon button-clear icon ion-android-send visible-xs\" ng-click=\"doTransfer()\">\n      </button>\n  </ion-nav-buttons>\n\n  <ion-content scroll=\"true\">\n    <div class=\"row no-padding-xs\">\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;</div>\n      <div class=\"col no-padding-xs\">\n        <h2 class=\"hidden-xs hidden-sm\">\n          {{\'TRANSFER.SUB_TITLE\'|translate}}\n        </h2>\n        <h4 class=\"hidden-xs hidden-sm\">&nbsp;</h4>\n        <ng-include src=\"\'templates/wallet/transfer_form.html\'\"></ng-include>\n      </div>\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;</div>\n    </div>\n  </ion-content>\n\n  <!-- Digit keyboard - fix #30 -->\n  <ion-digit-keyboard settings=\"digitKeyboardSettings\" ng-if=\"digitKeyboardVisible\"></ion-digit-keyboard>\n</ion-view>\n");
$templateCache.put("templates/wallet/popover_actions.html","<ion-popover-view class=\"fit has-header popover-wallet-actions\">\n  <ion-header-bar>\n    <h1 class=\"title\" translate>COMMON.POPOVER_ACTIONS_TITLE</h1>\n  </ion-header-bar>\n  <ion-content scroll=\"false\">\n    <div class=\"list item-text-wrap\">\n\n      <a class=\"item item-icon-left ink visible-xs visible-sm\" ng-click=\"showSharePopover($event)\">\n        <i class=\"icon ion-android-share-alt\"></i>\n        {{\'COMMON.BTN_SHARE\' | translate}}\n      </a>\n\n\n      <!-- alternatives identities -->\n      <a class=\"item item-icon-left ink\" ng-if=\"walletData.requirements.alternatives\" ng-click=\"showSelectIdentitiesModal()\">\n        <i class=\"icon ion-person\"></i>\n        <b class=\"icon-secondary ion-loop\" style=\"margin-top: 4px; left: 15px\"></b>\n        {{\'ACCOUNT.BTN_SELECT_ALTERNATIVES_IDENTITIES\' | translate}}\n      </a>\n\n      <!-- renew membership -->\n      <!-- (show only if a SELF has been sent - fix #673) -->\n      <a class=\"item item-icon-left ink visible-xs visible-sm\" ng-if=\"!walletData.requirements.needSelf && walletData.requirements.needRenew\" ng-click=\"renewMembership()\">\n        <i class=\"icon ion-loop\"></i>\n        {{\'ACCOUNT.BTN_MEMBERSHIP_RENEW_DOTS\' | translate}}\n      </a>\n      <a class=\"item item-icon-left ink hidden-xs hidden-sm\" ng-if=\"!walletData.requirements.needSelf\" ng-class=\"{\'gray\':!walletData.requirements.needRenew}\" ng-click=\"renewMembership()\">\n        <i class=\"icon ion-loop\"></i>\n        {{\'ACCOUNT.BTN_MEMBERSHIP_RENEW_DOTS\' | translate}}\n      </a>\n\n      <a class=\"item item-icon-left assertive ink\" ng-if=\"walletData.requirements.canMembershipOut\" ng-click=\"membershipOut()\">\n        <i class=\"icon ion-log-out\"></i>\n        {{\'ACCOUNT.BTN_MEMBERSHIP_OUT_DOTS\' | translate}}\n      </a>\n\n      <a class=\"item item-icon-left ink\" ng-click=\"showSecurityModal()\">\n        <i class=\"icon ion-locked\"></i>\n        <span ng-bind-html=\"\'ACCOUNT.BTN_SECURITY_DOTS\' | translate\"></span>\n\n      </a>\n\n      <div class=\"item-divider hidden-sm hidden-xs\"></div>\n\n      <!-- help tour -->\n      <a class=\"item item-icon-left ink hidden-sm hidden-xs\" ng-click=\"startWalletTour()\">\n        <i class=\"icon ion-easel\"></i>\n        {{\'COMMON.BTN_HELP_TOUR_SCREEN\' | translate}}\n      </a>\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/wallet/popover_unit.html","<ion-popover-view class=\"popover-unit\">\n  <ion-content scroll=\"false\">\n    <div class=\"list\">\n      <a class=\"item item-icon-left\" ng-class=\"{ \'selected\': !formData.useRelative}\" ng-click=\"closePopover(false)\">\n        <i class=\"icon\" ng-class=\"{ \'ion-ios-checkmark-empty\': !formData.useRelative}\"></i>\n        <i ng-bind-html=\"$root.currency.name | currencySymbol:false\"></i>\n      </a>\n      <a class=\"item item-icon-left\" ng-class=\"{ \'selected\': formData.useRelative}\" ng-click=\"closePopover(true)\">\n        <i class=\"icon\" ng-class=\"{ \'ion-ios-checkmark-empty\': formData.useRelative}\"></i>\n        <i ng-bind-html=\"$root.currency.name | currencySymbol:true\"></i>\n      </a>\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/wallet/popup_register.html","<form name=\"registerForm\" ng-submit=\"\">\n  <div class=\"list\" ng-init=\"setRegisterForm(registerForm)\">\n    <label class=\"item item-input\" ng-class=\"{\'item-input-error\': registerForm.$submitted && registerForm.pseudo.$invalid}\">\n      <input name=\"pseudo\" type=\"text\" placeholder=\"{{\'ACCOUNT.NEW.PSEUDO_HELP\' | translate}}\" ng-model=\"walletData.newUid\" ng-minlength=\"3\" required>\n    </label>\n    <div class=\"form-errors\" ng-if=\"registerForm.$submitted && registerForm.pseudo.$error\" ng-messages=\"registerForm.pseudo.$error\">\n      <div class=\"form-error\" ng-message=\"required\">\n        <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n      </div>\n      <div class=\"form-error\" ng-message=\"minlength\">\n        <span translate=\"ERROR.FIELD_TOO_SHORT\"></span>\n      </div>\n    </div>\n  </div>\n</form>\n");
$templateCache.put("templates/wallet/transfer_form.html","  <form name=\"transferForm\" novalidate=\"\" ng-submit=\"doTransfer()\">\n\n    <div class=\"list no-padding-xs\" ng-init=\"setForm(transferForm)\">\n\n      <a class=\"item item-icon-right gray ink\" ng-class=\"{\'item-input-error\': form.$submitted && !formData.destPub}\" ng-click=\"showWotLookupModal()\">\n          <span class=\"gray\" translate>TRANSFER.TO</span>\n          <span class=\"badge badge-royal animate-fade-in animate-show-hide ng-hide\" ng-show=\"destUid\">\n            <i class=\"ion-person\"></i> {{destUid}}\n          </span>&nbsp;\n          <span class=\"badge badge-royal\" ng-show=\"!destUid && formData.destPub\">\n            <i class=\"ion-key\"></i> {{formData.destPub | formatPubkey}}\n          </span>\n        <i class=\"gray icon ion-ios-arrow-right\"></i>\n      </a>\n      <div class=\"form-errors\" ng-if=\"form.$submitted && !formData.destPub\">\n        <div class=\"form-error\">\n          <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n        </div>\n      </div>\n\n      <span class=\"item item-text-wrap\">\n          <span class=\"gray\" translate>TRANSFER.FROM</span>\n          <span class=\"badge animate-fade-in animate-show-hide ng-hide\" ng-show=\"!loading\" ng-class=\"{\'badge-assertive\': (convertedBalance <= 0 || (formData.amount && convertedBalance < formData.amount)), \'badge-balanced\': (convertedBalance > 0 && (!formData.amount || convertedBalance >= formData.amount)) }\">\n              <ion-spinner icon=\"android\" ng-if=\"!walletData.pubkey\"></ion-spinner>\n              <span ng-if=\"walletData.pubkey && !walletData.isMember\">\n                <i class=\"ion-key\"></i> {{walletData.pubkey| formatPubkey}}&nbsp;&nbsp;\n              </span>\n              <span ng-if=\"walletData.isMember\">\n                <i class=\"ion-person\"></i> {{walletData.name||walletData.uid}}&nbsp;&nbsp;\n              </span>\n              <span ng-bind-html=\"walletData.balance|formatAmount:{useRelative: formData.useRelative, currency:currency}\"></span>\n          </span>\n\n      </span>\n\n      <!-- Amount -->\n      <ion-item class=\"item-input item-floating-label item-button-right\" ng-class=\"{\'item-input-error\': form.$submitted && form.amount.$invalid}\">\n        <div class=\"input-label\">\n          <span translate>TRANSFER.AMOUNT</span>\n          (<span ng-bind-html=\"$root.currency.name | currencySymbol:formData.useRelative\"></span>)\n        </div>\n        <input type=\"text\" class=\"hidden-xs hidden-sm\" name=\"amount\" placeholder=\"{{::\'TRANSFER.AMOUNT_HELP\' | translate}} ({{$root.currency.name | currencySymbolNoHtml:formData.useRelative}})\" ng-model=\"formData.amount\" required number-float>\n\n        <!-- mobile: come OVER the input -->\n        <input type=\"text\" class=\"visible-xs visible-sm\" name=\"amount\" placeholder=\"{{::\'TRANSFER.AMOUNT_HELP\' | translate}} ({{$root.currency.name | currencySymbolNoHtml:formData.useRelative}})\" ng-model=\"formData.amount\" required number-float>\n        <!-- This div will catch click event, to open digit keyboard -->\n        <div class=\"block visible-xs visible-sm\" style=\"position:absolute; opacity:0; z-index:100; top: 0; left: 0; height: 100%; width: 100%\" ng-click=\"showDigitKeyboard()\"></div>\n\n        <a class=\"button button-stable icon ion-arrow-swap gray ink hidden-xs hidden-sm\" ng-click=\"showUnitPopover($event)\">\n        </a>\n        <a class=\"button button-icon gray icon ion-android-more-vertical ink visible-xs visible-sm\" style=\"z-index:110; right: 0px\" ng-click=\"showUnitPopover($event)\">\n        </a>\n      </ion-item>\n      <div class=\"form-errors\" ng-show=\"form.$submitted && form.amount.$error\" ng-messages=\"form.amount.$error\">\n        <div class=\"form-error\" ng-message=\"required\">\n          <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n        </div>\n        <div class=\"form-error\" ng-message=\"numberFloat\">\n          <span translate=\"ERROR.FIELD_NOT_NUMBER\"></span>\n        </div>\n        <div class=\"form-error\" ng-message=\"numberInt\">\n          <span translate=\"ERROR.FIELD_NOT_INT\"></span>\n        </div>\n        <div class=\"form-error\" ng-message=\"min\">\n          <span translate=\"ERROR.FIELD_MIN\" translate-values=\"{min: minAmount}\"></span>\n        </div>\n      </div>\n\n      <!-- Enable comment ? -->\n      <div class=\"pull-right visible-xs visible-sm\" ng-if=\"!formData.useComment\">\n        <a class=\"button button-text button-small ink\" ng-click=\"addComment()\">\n          <i class=\"icon ion-plus\"></i>\n          <span translate>TRANSFER.BTN_ADD_COMMENT</span>\n        </a>\n      </div>\n\n      <!-- Comment -->\n      <label class=\"item item-input item-floating-label hidden-xs hidden-sm\" ng-class=\"{\'item-input-error\': form.$submitted && form.comment.$invalid}\">\n        <span class=\"input-label\">{{\'TRANSFER.COMMENT\' | translate}}</span>\n        <textarea placeholder=\"{{\'TRANSFER.COMMENT_HELP\' | translate}}\" name=\"comment\" ng-model=\"formData.comment\" ng-maxlength=\"255\" ng-pattern=\"commentPattern\">\n        </textarea>\n      </label>\n      <label class=\"item item-input item-floating-label visible-xs visible-sm\" ng-if=\"formData.useComment\" ng-class=\"{\'item-input-error\': form.$submitted && form.comment.$invalid}\">\n        <span class=\"input-label\">{{\'TRANSFER.COMMENT\' | translate}}</span>\n        <textarea placeholder=\"{{\'TRANSFER.COMMENT_HELP\' | translate}}\" id=\"{{commentInputId}}\" name=\"comment\" ng-model=\"formData.comment\" ng-maxlength=\"255\" ng-pattern=\"commentPattern\" ng-focus=\"hideDigitKeyboard()\">\n        </textarea>\n      </label>\n      <div class=\"form-errors\" ng-show=\"form.$submitted && form.comment.$error\" ng-messages=\"form.comment.$error\">\n        <div class=\"form-error\" ng-message=\"maxlength\">\n          <span translate=\"ERROR.FIELD_TOO_LONG\"></span>\n        </div>\n        <div class=\"form-error\" ng-message=\"pattern\">\n          <span translate=\"ERROR.FIELD_ACCENT\"></span>\n        </div>\n      </div>\n\n      <!-- Warn comment is public -->\n      <div class=\"item item-icon-left item-text-wrap hidden-xs hidden-sm\">\n        <i class=\"icon ion-ios-information-outline positive\"></i>\n        <h4 class=\"positive\" translate>TRANSFER.WARN_COMMENT_IS_PUBLIC</h4>\n      </div>\n      <div class=\"item item-icon-left item-text-wrap visible-xs visible-sm\" ng-if=\"formData.useComment\">\n        <i class=\"icon ion-ios-information-outline positive\"></i>\n        <h4 class=\"positive\" translate>TRANSFER.WARN_COMMENT_IS_PUBLIC</h4>\n      </div>\n    </div>\n\n\n\n    <div class=\"padding hidden-xs text-right\">\n      <button class=\"button button-clear button-dark ink\" ng-click=\"cancel()\" type=\"button\" translate>COMMON.BTN_CANCEL\n      </button>\n      <button class=\"button button-positive ink\" type=\"submit\">\n        {{\'TRANSFER.BTN_SEND\' | translate}}\n      </button>\n    </div>\n  </form>\n\n\n");
$templateCache.put("templates/wallet/tx_locked_outputs_popover.html","<ion-popover-view class=\"fit popover-locked-outputs\">\n  <ion-header-bar>\n    <h1 class=\"title\" translate>ACCOUNT.LOCKED_OUTPUTS_POPOVER.TITLE</h1>\n  </ion-header-bar>\n  <ion-content scroll=\"true\">\n\n    <div ng-if=\"popoverData.lockedOuputs.length == 1\" class=\"item item-text-wrap no-border\">\n      <h4 class=\"positive\" translate>ACCOUNT.LOCKED_OUTPUTS_POPOVER.DESCRIPTION</h4>\n    </div>\n    <div ng-if=\"popoverData.lockedOuputs.length > 1\" class=\"item item-text-wrap no-border\">\n      <h4 class=\"positive\" translate>ACCOUNT.LOCKED_OUTPUTS_POPOVER.DESCRIPTION_MANY</h4>\n    </div>\n\n    <!-- outputs -->\n    <div ng-repeat=\"output in popoverData.lockedOuputs track by $index\" class=\"item\">\n\n      <!-- output amount (only visible if more than one outputs -->\n      <h2 class=\"gray\" ng-if=\"popoverData.lockedOuputs.length > 1\" translate>ACCOUNT.LOCKED_OUTPUTS_POPOVER.LOCKED_AMOUNT</h2>\n      <div ng-if=\"popoverData.lockedOuputs.length > 1\" class=\"badge item-note\" ng-class=\"{\'badge-balanced\': output.amount > 0}\">\n        <i class=\"icon ion-locked\"></i>\n        <span ng-bind-html=\"::output.amount| formatAmount\"></span>\n        <span ng-bind-html=\"::unit\"></span>\n      </div>\n\n      <!-- unlock conditions -->\n      <div ng-repeat=\"condition in output.unlockConditions track by $index\" class=\"row\" ng-class=\"::{\'padding-top\': !$index && popoverData.lockedOuputs.length > 1}\" ng-style=\"::condition.style\">\n        <span class=\"gray\" ng-if=\"::condition.operator\">{{::\'BLOCKCHAIN.VIEW.TX_OUTPUT_OPERATOR.\'+condition.operator|translate}}&nbsp;</span>\n        <div ng-if=\"::condition.type==\'SIG\'\">\n            <i class=\"icon ion-key dark\"></i>\n          <span class=\"dark\" ng-bind-html=\"::\'BLOCKCHAIN.VIEW.TX_OUTPUT_FUNCTION.SIG\' | translate\"></span>\n          <a ng-click=\"goState(\'app.wot_identity\', {pubkey:condition.value})\" style=\"text-decoration: none\" class=\"positive\">\n            {{condition.value|formatPubkey}}\n          </a>\n        </div>\n        <div ng-if=\"::condition.type==\'XHX\'\">\n          <i class=\"icon ion-lock-combination dark\"></i>\n          <span class=\"dark\" ng-bind-html=\"::\'BLOCKCHAIN.VIEW.TX_OUTPUT_FUNCTION.XHX\' | translate\"></span>\n          <a copy-on-click=\"{{::condition.value}}\" class=\"positive\">\n            {{::condition.value|formatPubkey}}...\n          </a>\n        </div>\n        <div ng-if=\"condition.type==\'CSV\'\">\n          <i class=\"icon ion-clock dark\"></i>\n          <span class=\"dark\" ng-bind-html=\"::\'BLOCKCHAIN.VIEW.TX_OUTPUT_FUNCTION.CSV\' | translate\"></span>\n          {{::condition.value|formatDuration}}\n        </div>\n        <div ng-if=\"condition.type==\'CLTV\'\">\n          <i class=\"icon ion-clock dark\"></i>\n          <span class=\"dark\" ng-bind-html=\"::\'BLOCKCHAIN.VIEW.TX_OUTPUT_FUNCTION.CLTV\' | translate\"></span>\n          {{::condition.value|formatDate}}\n        </div>\n      </div>\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/wallet/view_wallet.html","<ion-view left-buttons=\"leftButtons\" class=\"view-wallet\" id=\"wallet\">\n  <ion-nav-title>\n    <!-- no title-->\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"secondary\">\n\n    <cs-extension-point name=\"nav-buttons\"></cs-extension-point>\n\n    <button class=\"button button-icon button-clear icon ion-android-more-vertical visible-xs visible-sm\" id=\"helptip-wallet-options-xs\" ng-click=\"showActionsPopover($event)\">\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content scroll=\"true\" class=\"refresher-top-bg refresher-light\" bind-notifier=\"{ rebind:settings.useRelative, locale:$root.settings.locale.id}\">\n\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"doUpdate(true)\">\n    </ion-refresher>\n\n    <div class=\"positive-900-bg hero\" id=\"wallet-header\" ng-class=\"{\'hero-qrcode-active\': toggleQRCode}\">\n      <div class=\"content\" ng-if=\"!loading\">\n        <i class=\"avatar\" ng-if=\":rebind:!formData.avatar\" ng-class=\":rebind:{\'avatar-wallet\': !formData.isMember, \'avatar-member\': formData.isMember}\"></i>\n        <i class=\"avatar\" ng-if=\":rebind:formData.avatar\" style=\"background-image: url({{:rebind:formData.avatar.src}})\"></i>\n        <ng-if ng-if=\":rebind:formData.name\">\n          <h3 class=\"light\">{{:rebind:formData.name}}</h3>\n        </ng-if>\n        <ng-if ng-if=\":rebind:!formData.name\">\n          <h3 class=\"light\" ng-if=\":rebind:formData.uid\">{{:rebind:formData.uid}}</h3>\n          <h3 class=\"light\" ng-if=\":rebind:!formData.uid\"><i class=\"ion-key\"></i> {{:rebind:formData.pubkey | formatPubkey}}</h3>\n        </ng-if>\n        <h4 class=\"assertive\"><ng-if ng-if=\":rebind:(formData.name || formData.uid) && !formData.isMember\" translate>WOT.NOT_MEMBER_PARENTHESIS</ng-if></h4>\n      </div>\n      <h4 class=\"content light\" ng-if=\"loading\">\n        <ion-spinner icon=\"android\"></ion-spinner>\n      </h4>\n    </div>\n\n    <div ng-attr-id=\"{{ qrcodeId }}\" class=\"qrcode spin\" ng-class=\"{\'active\': toggleQRCode}\" ng-click=\"toggleQRCode = !toggleQRCode\"></div>\n\n    <!-- Buttons bar-->\n    <a id=\"wallet-share-anchor\"></a>\n    <div class=\"hidden-xs hidden-sm padding text-center\" ng-if=\"!loading\">\n\n      <button class=\"button button-stable button-small-padding icon ion-android-share-alt ink\" ng-click=\"showSharePopover($event)\" title=\"{{\'COMMON.BTN_SHARE\' | translate}}\">\n      </button>\n\n      <button class=\"button button-stable button-small-padding icon ion-loop ink\" ng-click=\"doUpdate()\" title=\"{{\'COMMON.BTN_REFRESH\' | translate}}\">\n      </button>\n\n      <cs-extension-point name=\"buttons\"></cs-extension-point>\n\n      &nbsp;&nbsp;\n\n      <button id=\"helptip-wallet-options\" class=\"button button-stable icon-right ink\" ng-click=\"showActionsPopover($event)\">\n        &nbsp; <i class=\"icon ion-android-more-vertical\"></i>&nbsp;\n        {{:locale:\'COMMON.BTN_OPTIONS\' | translate}}\n      </button>\n\n      <div ng-if=\"formData.requirements.needRenew\">\n        <br>\n        <button class=\"button button-raised button-stable ink\" ng-click=\"renewMembership()\">\n          <span class=\"assertive\">{{:locale:\'ACCOUNT.BTN_MEMBERSHIP_RENEW\' | translate}}</span>\n        </button>\n      </div>\n    </div>\n\n    <div class=\"visible-xs visible-sm padding text-center\" ng-if=\"!loading\">\n      <button class=\"button button-assertive button-small-padding ink\" ng-click=\"logout({askConfirm: true})\">\n        <i class=\"icon ion-log-out\"></i>\n        {{\'COMMON.BTN_LOGOUT\' | translate}}\n      </button>\n    </div>\n\n    <div class=\"row no-padding\">\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;</div>\n\n      <div class=\"col\">\n\n        <div class=\"list\" ng-class=\"::motion.ionListClass\" ng-hide=\"loading\">\n\n          <span class=\"item item-divider\" translate>WOT.GENERAL_DIVIDER</span>\n\n          <!-- Public key -->\n          <div id=\"helptip-wallet-pubkey\" class=\"item item-icon-left item-text-wrap ink\" on-hold=\"copy(formData.pubkey)\" copy-on-click=\"{{:rebind:formData.pubkey}}\">\n            <i class=\"icon ion-key\"></i>\n            <span>{{:locale:\'COMMON.PUBKEY\'|translate}}</span>\n            <h4 id=\"pubkey\" class=\"dark\">{{:rebind:formData.pubkey}}</h4>\n          </div>\n\n          <!-- Uid + Registration date -->\n          <ion-item class=\"item-icon-left\" ng-if=\":rebind:formData.sigDate||formData.uid\">\n            <i class=\"icon ion-calendar\"></i>\n            <span translate>COMMON.UID</span>\n            <h5 class=\"dark\" ng-if=\":rebind:formData.sigDate\">\n              <span translate>WOT.REGISTERED_SINCE</span>\n              {{:rebind:formData.sigDate | formatDate}}\n            </h5>\n            <span class=\"badge badge-stable\">{{:rebind:formData.uid}}</span>\n          </ion-item>\n\n          <!-- Certifications -->\n          <a id=\"helptip-wallet-certifications\" class=\"item item-icon-left item-icon-right item-text-wrap ink\" ng-if=\"formData.isMember||formData.requirements.pendingMembership||!formData.requirements.needSelf\" ng-click=\"showCertifications()\">\n            <i class=\"icon ion-ribbon-b\"></i>\n            <b ng-if=\"formData.requirements.isSentry\" class=\"ion-star icon-secondary\" style=\"color: yellow; font-size: 16px; left: 25px; top: -7px\"></b>\n            {{:locale:\'ACCOUNT.CERTIFICATION_COUNT\'|translate}}\n            <cs-badge-certification requirements=\"formData.requirements\" parameters=\"::currency.parameters\">\n            </cs-badge-certification>\n            <i class=\"gray icon ion-ios-arrow-right\"></i>\n          </a>\n\n          <!-- Signature stock -->\n          <a id=\"helptip-wallet-given-certifications\" class=\"item item-icon-left item-text-wrap item-icon-right ink visible-xs visible-sm\" ng-if=\"formData.isMember\" ng-click=\"showGivenCertifications()\">\n            <i class=\"icon ion-ribbon-a\"></i>\n            <span translate>WOT.GIVEN_CERTIFICATIONS.SENT</span>\n            <i class=\"gray icon ion-ios-arrow-right\"></i>\n          </a>\n\n          <!-- Account transaction -->\n          <a class=\"item item-icon-left item-icon-right ink\" ng-if=\"!loading\" ui-sref=\"app.view_wallet_tx\">\n            <i class=\"icon ion-card\"></i>\n            <span translate>WOT.ACCOUNT_OPERATIONS</span>\n            <i class=\"gray icon ion-ios-arrow-right\"></i>\n          </a>\n\n          <!-- Events -->\n          <span class=\"item item-divider\" ng-if=\"formData.events.length\">\n            {{:locale:\'ACCOUNT.EVENTS\' | translate}}\n          </span>\n\n          <div class=\"item item-text-wrap item-icon-left item-wallet-event\" ng-repeat=\"event in formData.events\">\n            <i class=\"icon\" ng-class=\"{\'ion-information-circled royal\': event.type==\'info\',\'ion-alert-circled\': event.type==\'warn\'||event.type==\'error\',\'assertive\': event.type==\'error\',\'ion-clock\': event.type==\'pending\'}\"></i>\n            <span trust-as-html=\"event.message | translate:event.messageParams\"></span>\n          </div>\n\n\n          <cs-extension-point name=\"general\"></cs-extension-point>\n\n          <cs-extension-point name=\"after-general\"></cs-extension-point>\n\n\n       </div>\n      </div>\n\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;\n      </div>\n    </div>\n  </ion-content>\n\n</ion-view>\n");
$templateCache.put("templates/wallet/view_wallet_tx.html","<ion-view left-buttons=\"leftButtons\" class=\"view-wallet-tx\">\n  <ion-nav-title>\n    <!-- no title-->\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"secondary\">\n    <cs-extension-point name=\"nav-buttons\"></cs-extension-point>\n  </ion-nav-buttons>\n\n  <ion-content scroll=\"true\" class=\"refresher-top-bg refresher-light\" bind-notifier=\"{ rebind:settings.useRelative, locale:settings.locale.id}\">\n\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"doUpdate(true)\">\n    </ion-refresher>\n\n    <div class=\"positive-900-bg hero\">\n      <div class=\"content\" ng-if=\"!loading\">\n        <h1 class=\"light\">\n          <span ng-bind-html=\":balance:rebind:formData.balance | formatAmount:{currency: $root.currency.name}\"></span>\n        </h1>\n        <h4 ng-if=\"!loading && $root.settings.expertMode\" style=\"font-style: italic\">\n          (<span ng-bind-html=\":balance:rebind:formData.balance | formatAmount:{useRelative:!$root.settings.useRelative, currency: $root.currency.name}\"></span>)\n        </h4>\n        <div class=\"helptip-anchor-center\">\n          <a id=\"helptip-wallet-balance\">&nbsp;</a>\n        </div>\n      </div>\n\n      <h2 class=\"content light\" ng-if=\"loading\">\n        <ion-spinner icon=\"android\"></ion-spinner>\n      </h2>\n    </div>\n\n    <!-- Buttons bar-->\n    <div class=\"hidden-xs hidden-sm padding text-center\" ng-if=\"!loading\">\n\n      <button class=\"button button-stable button-small-padding icon ion-loop ink\" ng-click=\"doUpdate()\" title=\"{{\'COMMON.BTN_REFRESH\' | translate}}\">\n      </button>\n\n      <button class=\"button button-stable button-small-padding icon ion-android-download ink\" ng-click=\"downloadHistoryFile()\" title=\"{{\'COMMON.BTN_DOWNLOAD_ACCOUNT_STATEMENT\' | translate}}\">\n      </button>\n\n      <cs-extension-point name=\"buttons\"></cs-extension-point>\n\n      &nbsp;&nbsp;\n\n      <button class=\"button button-calm ink\" ng-click=\"showTransferModal()\">\n        {{:locale:\'COMMON.BTN_SEND_MONEY\' | translate}}\n      </button>\n  </div>\n\n    <div class=\"row no-padding\">\n\n      <div class=\"col col-15 hidden-xs hidden-sm\">&nbsp;</div>\n\n      <div class=\"col\">\n\n\n\n        <div class=\"list\" ng-class=\"::motion.ionListClass\">\n\n          <!-- Errors transactions-->\n          <a class=\"item item-icon-left item-icon-right ink\" ng-if=\"formData.tx.errors && formData.tx.errors.length\" ui-sref=\"app.view_wallet_tx_errors\">\n            <i class=\"icon ion-alert-circled\"></i>\n            {{:locale:\'ACCOUNT.ERROR_TX\'|translate}}\n            <div class=\"badge badge-assertive\">\n              {{formData.tx.errors.length}}\n            </div>\n            <i class=\"gray icon ion-ios-arrow-right\"></i>\n          </a>\n\n          <!-- Pending transactions -->\n          <span class=\"item item-pending item-divider\" ng-if=\"formData.tx.pendings && formData.tx.pendings.length\">\n            {{:locale:\'ACCOUNT.PENDING_TX\'|translate}}\n          </span>\n\n          <div class=\"item item-pending item-tx item-icon-left\" ng-repeat=\"tx in formData.tx.pendings\" ng-init=\"pending=true;\" ng-include=\"\'templates/wallet/item_tx.html\'\">\n          </div>\n\n          <!-- Last Transactions -->\n          <span class=\"item item-divider\" ng-if=\"!loading\">\n            {{:locale:\'ACCOUNT.LAST_TX\'|translate}}\n            <a id=\"helptip-wallet-tx\" style=\"position: relative; bottom: 0; right: 0px\">&nbsp;</a>\n          </span>\n\n\n          <span class=\"item padding\" ng-if=\"!loading && !formData.tx.history.length\">\n            <span class=\"gray\">{{:locale:\'ACCOUNT.NO_TX\'|translate}}</span>\n          </span>\n\n\n          <div ng-repeat=\"tx in formData.tx.history\" class=\"item item-tx item-icon-left\" ng-include=\"::!tx.isUD ? \'templates/wallet/item_tx.html\' : \'templates/wallet/item_ud.html\'\">\n          </div>\n          <div class=\"item item-text-wrap text-center\" ng-if=\"formData.tx.fromTime > 0\">\n            <p>\n              <a ng-click=\"showMoreTx()\">{{:locale:\'ACCOUNT.SHOW_MORE_TX\'|translate}}</a>\n              <span class=\"gray\" translate=\"ACCOUNT.TX_FROM_DATE\" translate-values=\"{fromTime: formData.tx.fromTime}\"></span>\n              <span class=\"gray\">|</span>\n              <a ng-click=\"showMoreTx(-1)\" translate>ACCOUNT.SHOW_ALL_TX</a>\n            </p>\n          </div>\n        </div>\n      </div>\n\n      <div class=\"col col-15 hidden-xs hidden-sm\">&nbsp;</div>\n\n    </div>\n\n\n    \n  </ion-content>\n\n  <button id=\"fab-transfer\" ng-if=\"formData\" class=\"button button-fab button-fab-bottom-right button-energized-900 hidden-md hidden-lg drop\" ng-click=\"showTransferModal()\">\n    <i class=\"icon ion-android-send\"></i>\n  </button>\n</ion-view>\n");
$templateCache.put("templates/wallet/view_wallet_tx_error.html","<ion-view left-buttons=\"leftButtons\" id=\"wallet\">\n  <ion-nav-title>\n    <!-- no title-->\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"secondary\">\n\n    <button class=\"button button-icon button-clear icon ion-loop visible-xs visible-sm\" ng-click=\"doUpdate()\">\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content scroll=\"true\">\n\n    <!-- Buttons bar -->\n    <div class=\"hidden-xs hidden-sm padding text-center\">\n\n      <button class=\"button button-stable button-small-padding icon ion-loop ink\" ng-click=\"doUpdate()\" title=\"{{\'COMMON.BTN_REFRESH\' | translate}}\">\n      </button>\n    </div>\n\n    <div class=\"row no-padding\">\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;\n      </div>\n\n      <div class=\"col list\" ng-class=\"::motion.ionListClass\">\n\n        <!-- Pending received TX -->\n        <span class=\"item item-divider\">\n          <span translate>ACCOUNT.PENDING_TX_RECEIVED</span>\n          <div class=\"badge item-note\">\n            <span ng-if=\"!$root.settings.useRelative\">({{$root.currency.name | abbreviate}})</span>\n            <span ng-if=\"$root.settings.useRelative\">({{\'COMMON.UD\' | translate}}<sub>{{$root.currency.name | abbreviate}}</sub>)</span>\n          </div>\n        </span>\n\n        <span class=\"item\" ng-if=\"!formData.tx.errors.length\">\n          <h3 translate>ACCOUNT.NO_TX</h3>\n        </span>\n\n        <div class=\"item item-pending item-tx item-icon-left\" ng-repeat=\"tx in formData.tx.errors | filter: filterReceivedTx\" ng-init=\"pending=true;\" ng-include=\"\'templates/wallet/item_tx.html\'\">\n        </div>\n\n        <!-- Error sent TX -->\n        <span class=\"item item-divider\">\n          <span translate>ACCOUNT.ERROR_TX_SENT</span>\n          <div class=\"badge item-note\">\n            <span ng-if=\"!$root.settings.useRelative\">({{$root.currency.name | abbreviate}})</span>\n            <span ng-if=\"$root.settings.useRelative\">({{\'COMMON.UD\' | translate}}<sub>{{$root.currency.name | abbreviate}}</sub>)</span>\n          </div>\n        </span>\n\n        <span class=\"item\" ng-if=\"!formData.tx.errors.length\">\n          <h3 translate>ACCOUNT.NO_TX</h3>\n        </span>\n\n        <div class=\"item item-pending item-tx item-icon-left\" ng-repeat=\"tx in formData.tx.errors | filter: filterSentTx\" ng-init=\"error=true;\" ng-include=\"\'templates/wallet/item_tx.html\'\">\n        </div>\n\n      </div>\n\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;\n      </div>\n    </div>\n  </ion-content>\n\n  <!--button id=\"fab-redo-transfer\"\n          ng-if=\"walletData\"\n          class=\"button button-fab button-fab-bottom-right button-energized-900 hidden-md hidden-lg drop\"\n          ng-click=\"redoTransfer()\">\n    <i class=\"icon ion-refresh\"></i>\n  </button-->\n</ion-view>\n");
$templateCache.put("templates/wot/item_certification.html","\n          <i class=\"item-image\" ng-if=\"!cert.avatar\" ng-class=\"{\'ion-card\': !cert.isMember, \'ion-person\': cert.isMember}\"></i>\n          <i class=\"item-image avatar\" ng-if=\"cert.avatar\" style=\"background-image: url({{::cert.avatar.src}})\"></i>\n\n            <span ng-if=\"cert.isMember\">\n              <h3>\n                <i class=\"icon ion-clock\" ng-if=\"cert.pending\"> </i>\n                <span class=\"positive\">\n                  {{::cert.name||cert.uid}}\n                </span>\n              </h3>\n              <h4 class=\"gray\">\n                <i class=\"ion-key\"></i>\n                {{::cert.pubkey | formatPubkey}}\n                <span class=\"gray\"> | {{::cert.time|formatDate}}</span>\n                <span class=\"gray\" ng-if=\"$root.settings.expertMode\"> | {{::cert.pending ? \'WOT.SIGNED_ON_BLOCK\' : \'WOT.WRITTEN_ON_BLOCK\' | translate:cert}}</span>\n              </h4>\n            </span>\n            <span ng-if=\"!cert.isMember\">\n              <h3>\n                <i class=\"icon ion-clock\" ng-if=\"cert.pending\"> </i>\n                <span ng-if=\"cert.uid\" class=\"dark\">\n                  {{::cert.name||cert.uid}}\n                </span>\n                <span ng-if=\"!cert.uid\" class=\"gray\">\n                  <i class=\"ion-key\"> </i>\n                  {{::cert.pubkey | formatPubkey}}\n                </span>\n              </h3>\n              <h5 class=\"assertive\">\n                {{::\'WOT.NOT_MEMBER_PARENTHESIS\'|translate}}\n              </h5>\n              <h4 class=\"gray\">\n                <span ng-if=\"cert.uid\">\n                  <i class=\"ion-key\"></i>\n                  {{::cert.pubkey | formatPubkey}}\n                </span>\n                <span class=\"gray\"> | {{::cert.time|formatDate}}</span>\n                <span class=\"gray\" ng-if=\"$root.settings.expertMode\"> | {{::cert.pending ? \'WOT.SIGNED_ON_BLOCK\' : \'WOT.WRITTEN_ON_BLOCK\' | translate:cert}}</span>\n              </h4>\n            </span>\n            <div class=\"badge badge-stable\" ng-class=\"{\'badge-energized\': cert.willExpire}\" ng-if=\"cert.expiresIn\">\n              {{::cert.expiresIn | formatDurationTo}}\n            </div>\n            <div class=\"badge badge-assertive\" ng-if=\"!cert.expiresIn\">\n              {{::\'WOT.EXPIRED\' | translate}}\n            </div>\n");
$templateCache.put("templates/wot/item_content_identity.html","<i ng-if=\"::!item.avatar\" class=\"item-image icon ion-person\"></i>\n<i ng-if=\"::item.avatar\" class=\"item-image avatar\" style=\"background-image: url({{::item.avatar.src}})\"></i>\n\n<h2>\n  <ng-if ng-if=\"::item.name||item.uid\" ng-bind-html=\"::item.name||item.uid\"></ng-if>\n  <ng-if ng-if=\"::!item.name && !item.uid\">{{::item.pubkey|formatPubkey}}</ng-if>\n</h2>\n\n<h4 class=\"gray\" ng-class=\"{\'pull-right\': !smallscreen}\" ng-if=\"::item.sigDate\">\n  <i class=\"ion-clock\"></i>\n  {{::\'WOT.LOOKUP.REGISTERED\' | translate:item}}\n</h4>\n<h4 class=\"gray\" ng-class=\"{\'pull-right\': !smallscreen}\" ng-if=\"item.memberDate\">\n  <i class=\"ion-clock\"></i>\n  {{::\'WOT.LOOKUP.MEMBER_FROM\' | translate:item}}\n</h4>\n<h4 class=\"gray\">\n  <span class=\"positive\" ng-if=\"::item.name && item.uid\">\n    <i class=\"ion-person\"></i>\n    {{::item.uid}}&nbsp;\n  </span>\n  <b class=\"ion-key\"></b>\n  {{::item.pubkey | formatPubkey}}\n  <span ng-if=\"::!item.uid\" class=\"assertive\" translate>WOT.NOT_MEMBER_PARENTHESIS</span>\n  <span ng-if=\"::item.revoked\" class=\"assertive\" translate>WOT.IDENTITY_REVOKED_PARENTHESIS</span>\n</h4>\n<h4 ng-if=\"::item.events||item.tags\">\n  <span ng-repeat=\"event in ::item.events\" class=\"assertive\">\n    <i class=\"ion-alert-circled\" ng-if=\"::!item.valid\"></i>\n    <span ng-bind-html=\"::event.message|translate:event.messageParams\"></span>\n  </span>\n  <span ng-if=\"::item.tags\" class=\"dark\">\n   <ng-repeat ng-repeat=\"tag in ::item.tags\">\n     #<ng-bind-html ng-bind-html=\"::tag\"></ng-bind-html>\n   </ng-repeat>\n  </span>\n</h4>\n");
$templateCache.put("templates/wot/items_given_certifications.html","\n<div class=\"list given-certifications\" ng-class=\"::motions.givenCertifications.ionListClass\">\n  <span class=\"item item-divider hidden-xs\">\n    <span translate>WOT.GIVEN_CERTIFICATIONS.SUMMARY</span>\n  </span>\n\n  <!-- Signature stock -->\n  <div id=\"helptip-certs-stock\" class=\"item item-icon-left item-text-wrap ink\">\n    <i class=\"icon ion-ribbon-a\"></i>\n    <span translate>WOT.GIVEN_CERTIFICATIONS.SENT</span>\n\n    <cs-badge-given-certification identity=\"formData\" parameters=\"$root.currency.parameters\">\n    </cs-badge-given-certification>\n  </div>\n\n  <!-- Error certifications count -->\n  <div class=\"item item-icon-left item-text-wrap ink\" ng-if=\"formData.given_cert_error.length\">\n    <i class=\"icon ion-alert-circled\"></i>\n    <span translate>WOT.GIVEN_CERTIFICATIONS.ERROR</span>\n\n    <span class=\"badge badge-assertive\">\n      {{formData.given_cert_error.length}}\n    </span>\n  </div>\n\n  <!-- pending given certifications -->\n  <span class=\"item item-divider\" ng-if=\"formData.given_cert_pending.length\">\n    <span translate>WOT.GIVEN_CERTIFICATIONS.PENDING_LIST</span>\n    <div class=\"badge item-note\" style=\"text-align: right !important\" translate>WOT.NOT_WRITTEN_EXPIRE_IN</div>\n  </span>\n\n  <a class=\"item item-avatar ink\" ng-repeat=\"cert in formData.given_cert_pending\" ui-sref=\"app.wot_identity({pubkey:cert.pubkey, uid:cert.uid})\" ng-include=\"\'templates/wot/item_certification.html\'\">\n  </a>\n\n  <!-- validated given certifications -->\n  <span class=\"item item-divider\">\n    <span translate>WOT.GIVEN_CERTIFICATIONS.LIST</span>\n    <div class=\"badge item-note\" translate>WOT.EXPIRE_IN</div>\n  </span>\n\n  <span class=\"item gray\" ng-if=\"!formData.given_cert.length\" translate>\n    WOT.NO_GIVEN_CERTIFICATION\n  </span>\n\n  <a class=\"item item-avatar ink\" ng-repeat=\"cert in formData.given_cert\" ui-sref=\"app.wot_identity({pubkey:cert.pubkey, uid:cert.uid})\" ng-include=\"\'templates/wot/item_certification.html\'\">\n  </a>\n</div>\n");
$templateCache.put("templates/wot/items_received_certifications.html","        <div class=\"list certifications\" ng-class=\"::motions.receivedCertifications.ionListClass\">\n          <span class=\"item item-divider hidden-xs\">\n            <span translate>WOT.CERTIFICATIONS.SUMMARY</span>\n          </span>\n\n          <!-- Certifications count -->\n          <div id=\"helptip-received-certs\" class=\"item item-icon-left item-text-wrap ink\">\n            <i class=\"icon ion-ribbon-b\"></i>\n            <b ng-if=\"formData.requirements.isSentry\" class=\"ion-star icon-secondary\" style=\"color: yellow; font-size: 16px; left: 25px; top: -7px\"></b>\n\n            <span translate>WOT.CERTIFICATIONS.RECEIVED</span>\n            <h4 class=\"gray\" ng-if=\"formData.requirements.isSentry\" translate>WOT.CERTIFICATIONS.SENTRY_MEMBER</h4>\n\n            <cs-badge-certification cs-id=\"helptip-wot-view-certifications-count\" requirements=\"formData.requirements\" parameters=\"$root.currency.parameters\">\n            </cs-badge-certification>\n          </div>\n\n          <!-- Error certifications count -->\n          <div class=\"item item-icon-left item-text-wrap ink\" ng-if=\"formData.received_cert_error.length\">\n            <i class=\"icon ion-alert-circled\"></i>\n            <span translate>WOT.CERTIFICATIONS.ERROR</span>\n\n            <span class=\"badge badge-assertive\">\n              {{formData.received_cert_error.length}}\n            </span>\n          </div>\n\n          <!-- pending certifications -->\n          <span class=\"item item-divider\" ng-if=\"formData.received_cert_pending.length\">\n            <span translate>WOT.CERTIFICATIONS.PENDING_LIST</span>\n            <div class=\"badge item-note\" style=\"text-align: right !important\" translate>WOT.NOT_WRITTEN_EXPIRE_IN</div>\n          </span>\n\n          <a class=\"item item-avatar ink\" ng-repeat=\"cert in formData.received_cert_pending\" ui-sref=\"app.wot_identity({pubkey:cert.pubkey, uid:cert.uid})\" ng-include=\"\'templates/wot/item_certification.html\'\">\n          </a>\n\n          <!-- valid certifications -->\n          <span class=\"item item-divider\">\n            <span translate>WOT.CERTIFICATIONS.LIST</span>\n            <div class=\"badge item-note\" translate>WOT.EXPIRE_IN</div>\n          </span>\n\n          <span class=\"item gray\" ng-if=\"!formData.received_cert.length\" translate>WOT.NO_CERTIFICATION</span>\n\n          <a class=\"item item-avatar ink\" ng-repeat=\"cert in formData.received_cert\" ui-sref=\"app.wot_identity({pubkey:cert.pubkey, uid:cert.uid})\" ng-include=\"\'templates/wot/item_certification.html\'\">\n          </a>\n        </div>\n");
$templateCache.put("templates/wot/lookup.html","<ion-view left-buttons=\"leftButtons\">\n\n  <ion-tabs class=\"tabs-positive tabs-icon-top\">\n\n    <!--ion-tab title=\"{{\'WOT.CONTACTS.TITLE\'|translate}}\" icon=\"ion-person\"\n             ui-sref=\"app.wot_lookup.tab_contacts\">\n      <i class=\"ion-person\"></i>\n      <ion-nav-view name=\"tab\"></ion-nav-view>\n    </ion-tab-->\n\n\n    <ion-tab title=\"{{\'WOT.LOOKUP.TITLE\'|translate}}\" icon=\"ion-person-stalker\" ui-sref=\"app.wot_lookup.tab_search\">\n      <ion-nav-view name=\"tab\"></ion-nav-view>\n    </ion-tab>\n\n\n    <!-- Allow extension here -->\n    <cs-extension-point name=\"tabs\"></cs-extension-point>\n\n  </ion-tabs>\n</ion-view>\n");
$templateCache.put("templates/wot/lookup_form.html","<div class=\"lookupForm\">\n\n  <div class=\"item no-padding\">\n\n    <div class=\"double-padding-x padding-top-xs item-text-wrap\" ng-if=\"::allowMultiple\" style=\"height: 36px\">\n\n      <div class=\"gray padding-top\" ng-if=\"!selection.length && parameters.help\">{{::parameters.help|translate}}</div>\n\n      <div ng-repeat=\"identity in selection track by identity.id\" class=\"button button-small button-text button-stable button-icon-event ink\" ng-class=\"{\'button-text-positive\': identity.selected}\">\n        <span ng-bind-html=\"identity.name||identity.uid||(identity.pubkey|formatPubkey)\"></span>\n        <i class=\"icon ion-close\" ng-click=\"removeSelection(identity, $event)\">&nbsp;&nbsp;</i>\n      </div>\n\n    </div>\n\n    <div class=\"item-input\">\n      <i class=\"icon ion-search placeholder-icon\"></i>\n\n      <input type=\"text\" class=\"visible-xs visible-sm\" placeholder=\"{{\'WOT.SEARCH_HELP\'|translate}}\" ng-model=\"search.text\" ng-model-options=\"{ debounce: 650 }\" ng-change=\"doSearch()\" on-return=\"doSearchText()\" select-on-click>\n      <input type=\"text\" class=\"hidden-xs hidden-sm\" id=\"{{wotSearchTextId}}\" placeholder=\"{{\'WOT.SEARCH_HELP\'|translate}}\" ng-model=\"search.text\" on-return=\"doSearchText()\">\n      <div class=\"helptip-anchor-center\">\n        <a id=\"helptip-wot-search-text\"></a>\n      </div>\n    </div>\n  </div>\n\n  <div class=\"padding-top padding-xs\" style=\"display: block; height: 60px\" ng-class=\"::{\'hidden-xs\': !showResultLabel}\">\n    <div class=\"pull-left\" ng-if=\"!search.loading && showResultLabel\">\n      <ng-if ng-if=\"search.type==\'newcomers\'\">\n        <h4 translate>WOT.LOOKUP.NEWCOMERS</h4>\n        <small class=\"gray no-padding\" ng-if=\"search.total\">{{\'WOT.LOOKUP.NEWCOMERS_COUNT\'|translate:{count: search.total} }}</small>\n      </ng-if>\n      <ng-if ng-if=\"search.type==\'pending\'\">\n        <h4 translate>WOT.LOOKUP.PENDING</h4>\n        <small class=\"gray no-padding\" ng-if=\"search.total\">{{\'WOT.LOOKUP.PENDING_COUNT\'|translate:{count: search.total} }}</small>\n      </ng-if>\n      <h4 ng-if=\"search.type==\'text\'\">\n        <span translate>COMMON.RESULTS_LIST</span>\n        <small class=\"gray\" ng-if=\"search.total\">({{search.total}})</small>\n      </h4>\n    </div>\n\n\n    <div class=\"pull-right hidden-xs hidden-sm\">\n      <a ng-if=\"enableFilter\" class=\"button button-text button-small ink\" ng-class=\"{\'button-text-positive\': search.type==\'newcomers\'}\" ng-click=\"doGetNewcomers()\">\n        <i class=\"icon ion-person-stalker\"></i>\n        {{\'WOT.LOOKUP.BTN_NEWCOMERS\' | translate}}\n      </a>\n      <a ng-if=\"enableFilter\" class=\"button button-text button-small ink\" ng-class=\"{\'button-text-positive\': search.type==\'pending\'}\" ng-click=\"doGetPending()\" class=\"badge-balanced\">\n        <i class=\"icon ion-clock\"></i>\n        {{\'WOT.LOOKUP.BTN_PENDING\' | translate}}\n      </a>\n\n      <!-- Allow extension here -->\n      <cs-extension-point name=\"filter-buttons\"></cs-extension-point>\n      &nbsp;\n      <button class=\"button button-small button-stable ink\" ng-click=\"doSearch()\">\n        {{\'COMMON.BTN_SEARCH\' | translate}}\n      </button>\n\n      <button class=\"button button-small button-positive {{parameters.okType}} ink\" ng-if=\"::allowMultiple\" ng-disabled=\"!selection.length\" ng-click=\"next()\">\n        {{parameters.okText||\'COMMON.BTN_NEXT\' | translate}}\n      </button>\n    </div>\n  </div>\n\n  <div class=\"text-center\" ng-if=\"search.loading\">\n    <p class=\"gray\" ng-if=\"::$root.currency.initPhase\" translate>WOT.SEARCH_INIT_PHASE_WARNING</p>\n    <ion-spinner icon=\"android\"></ion-spinner>\n  </div>\n\n  <ng-if ng-if=\"!search.loading\">\n    <div class=\"assertive padding\" ng-if=\"!search.results.length\">\n      <span ng-if=\"search.type==\'text\'\" translate>COMMON.SEARCH_NO_RESULT</span>\n      <span ng-if=\"search.type==\'pending\'\" translate>WOT.LOOKUP.NO_PENDING</span>\n      <span ng-if=\"search.type==\'newcomers\'\" translate>WOT.LOOKUP.NO_NEWCOMERS</span>\n    </div>\n\n    <!-- simple selection + device -->\n    \n\n    <!-- simple selection + no device -->\n    <!--removeIf(device)-->\n    <div ng-if=\"::!allowMultiple\" class=\"list {{::motion.ionListClass}}\">\n\n      <div ng-repeat=\"item in search.results track by item.id\" id=\"helptip-wot-search-result-{{$index}}\" ng-class=\"::{\'item-avatar item-icon-right ink\': !item.divider, \'item-divider \': item.divider}\" class=\"item item-border-large {{::item.ionItemClass}}\" ng-click=\"::select(item)\">\n\n        <!-- divider -->\n        <span ng-if=\"::item.divider\">{{::(\'WOT.SEARCH.DIVIDER_\' + item.index)|upper|translate}}</span>\n\n        <!-- item -->\n        <ng-include ng-if=\"::!item.divider\" src=\"item.templateUrl || \'templates/wot/item_content_identity.html\'\"></ng-include>\n\n        <i ng-if=\"::!item.divider\" class=\"icon ion-ios-arrow-right\"></i>\n      </div>\n    </div>\n    <!--endRemoveIf(device)-->\n\n    <!-- multi selection -->\n    <div ng-if=\"::allowMultiple\" class=\"list {{::motion.ionListClass}}\">\n\n      <ion-checkbox ng-repeat=\"item in search.results track by item.id\" ng-model=\"item.checked\" class=\"item item-border-large item-avatar ink\" ng-click=\"toggleCheck($index, $event)\">\n        <ng-include src=\"\'templates/wot/item_content_identity.html\'\"></ng-include>\n      </ion-checkbox>\n    </div>\n\n    <ion-infinite-scroll ng-if=\"search.hasMore\" spinner=\"android\" on-infinite=\"showMore()\" distance=\"20%\">\n    </ion-infinite-scroll>\n\n  </ng-if>\n</div>\n");
$templateCache.put("templates/wot/lookup_lg.html","<ion-view>\n  <ion-nav-title>\n    {{\'MENU.WOT\' | translate}}\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"secondary\">\n    \n    <button class=\"button button-icon button-clear visible-xs visible-sm\" ng-click=\"showActionsPopover($event)\">\n      <i class=\"icon ion-android-funnel\"></i>\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content class=\"padding no-padding-xs\" scroll=\"true\">\n\n    <!-- Allow extension here -->\n    <cs-extension-point name=\"top\"></cs-extension-point>\n\n    <ng-include src=\"\'templates/wot/lookup_form.html\'\"></ng-include>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/wot/lookup_popover_actions.html","<ion-popover-view class=\"fit has-header visible-sm visible-xs\">\n  <ion-header-bar>\n    <h1 class=\"title\" translate>COMMON.POPOVER_FILTER_TITLE</h1>\n  </ion-header-bar>\n  <ion-content scroll=\"false\">\n    <div class=\"list item-text-wrap\">\n\n      <a class=\"item item-icon-left ink\" ng-click=\"doGetNewcomers()\">\n        <i class=\"icon ion-person\"></i>\n        {{\'WOT.LOOKUP.BTN_NEWCOMERS\' | translate}}\n      </a>\n\n      <a class=\"item item-icon-left ink\" ng-click=\"doGetPending()\">\n        <i class=\"icon ion-clock\"></i>\n        {{\'WOT.LOOKUP.BTN_PENDING\' | translate}}\n      </a>\n\n    </div>\n  </ion-content>\n</ion-popover-view>\n");
$templateCache.put("templates/wot/modal_lookup.html","<ion-modal-view id=\"wotLookup\" class=\"modal-full-height\">\n\n  <ion-header-bar class=\"bar-positive\">\n    <button class=\"button button-clear\" ng-click=\"closeModal()\" translate=\"\">COMMON.BTN_CANCEL</button>\n\n    <h1 class=\"title hidden-xs\">\n      {{::parameters.title?parameters.title:\'WOT.MODAL.TITLE\'|translate}}\n    </h1>\n\n    <button class=\"button button-clear icon-right visible-xs ink\" ng-if=\"allowMultiple && selection.length\" ng-click=\"closeModal(selection)\">\n      {{::parameters.okText||\'COMMON.BTN_NEXT\' | translate}}\n      <i ng-if=\"::!parameters.okText||parameters.okIcon\" class=\"icon {{::parameters.okIcon||\'ion-ios-arrow-right\'}}\"></i>\n    </button>\n  </ion-header-bar>\n\n  <ion-content class=\"padding no-padding-xs\" scroll=\"true\">\n\n    <div class=\"visible-xs visible-sm text-right stable-bg stable\">\n      \n      <button class=\"button button-icon button-small-padding dark ink\" ng-click=\"showActionsPopover($event)\">\n        <i class=\"icon ion-android-funnel\"></i>\n      </button>\n    </div>\n\n    <ng-include src=\"\'templates/wot/lookup_form.html\'\"></ng-include>\n  </ion-content>\n</ion-modal-view>\n");
$templateCache.put("templates/wot/modal_select_pubkey_identity.html","<ion-modal-view id=\"transfer\" class=\"modal-full-height modal-transfer\">\n  <ion-header-bar class=\"bar-positive\">\n    <button class=\"button button-clear\" ng-click=\"closeModal()\" translate>COMMON.BTN_CANCEL</button>\n    <h1 class=\"title\" translate>ACCOUNT.SELECT_IDENTITY_MODAL.TITLE</h1>\n  </ion-header-bar>\n\n  <ion-content scroll=\"true\">\n\n    <div class=\"padding\">\n      <p trust-as-html=\"\'ACCOUNT.SELECT_IDENTITY_MODAL.HELP\'|translate:{pubkey: pubkey}\"></p>\n    </div>\n\n    <ion-list>\n      <ion-item class=\"item-avatar item-icon-right\" ng-repeat=\"item in identities\" ng-click=\"closeModal(item)\">\n\n        <i class=\"item-image icon ion-person\"></i>\n\n        <h2>{{item.uid}}</h2>\n\n        <h4 class=\"gray\">\n          <b class=\"ion-key\"></b>\n          {{::item.pubkey | formatPubkey}}\n          <span ng-if=\"::!item.revoked && !item.pendingRevocation && !item.isMember\" class=\"assertive\" translate>WOT.NOT_MEMBER_PARENTHESIS</span>\n          <span ng-if=\"::item.revoked || item.pendingRevocation\" class=\"assertive bold\" translate>WOT.IDENTITY_REVOKED_PARENTHESIS</span>\n        </h4>\n\n        <ng-if ng-if=\"::!item.revoked && !item.pendingRevocation && (item.certificationCount || item.pendingCertificationCount)\">\n\n          <!-- certification count -->\n          <cs-badge-certification requirements=\"item\" parameters=\"$root.currency.parameters\"></cs-badge-certification>\n\n          <!-- certification label -->\n          <div class=\"gray badge badge-secondary\">\n            <span translate>ACCOUNT.CERTIFICATION_COUNT</span>\n          </div>\n        </ng-if>\n\n        <i class=\"icon ion-ios-arrow-right\"></i>\n      </ion-item>\n    </ion-list>\n\n  </ion-content>\n\n</ion-modal-view>\n");
$templateCache.put("templates/wot/view_certifications.html","<ion-view left-buttons=\"leftButtons\">\n  <ion-nav-title>\n    <span class=\"visible-xs visible-sm\">{{::formData.name||formData.uid}}</span>\n    <span class=\"hidden-xs hidden-sm\" ng-if=\"!loading\" translate=\"WOT.CERTIFICATIONS.TITLE\" translate-values=\"{uid: formData.name || formData.uid}\"></span>\n  </ion-nav-title>\n\n  <ion-nav-buttons side=\"secondary\">\n    <!-- Allow extension here -->\n    <cs-extension-point name=\"nav-buttons\"></cs-extension-point>\n  </ion-nav-buttons>\n\n  <ion-content class=\"certifications certifications-lg\">\n\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"doUpdate()\">\n    </ion-refresher>\n\n    <!-- Buttons bar -->\n    <div class=\"hidden-xs hidden-sm text-center padding\">\n      <button class=\"button button-stable button-small-padding icon ion-loop ink\" ng-click=\"doUpdate()\" title=\"{{\'COMMON.BTN_REFRESH\' | translate}}\">\n      </button>\n\n      <button id=\"helptip-certs-certify\" class=\"button button-raised button-calm icon-left ion-ribbon-b\" ng-if=\"canCertify\" ng-click=\"certify()\" ng-disabled=\"disableCertifyButton\">\n        {{\'WOT.BTN_CERTIFY\' | translate}}\n      </button>\n      <button id=\"helptip-certs-select-certify\" class=\"button button-raised button-calm icon-left\" ng-if=\"canSelectAndCertify\" ng-click=\"selectAndCertify()\">\n        {{\'WOT.BTN_SELECT_AND_CERTIFY\' | translate}}\n      </button>\n\n      <!-- Allow extension here -->\n      <cs-extension-point name=\"buttons\"></cs-extension-point>\n    </div>\n\n    <div class=\"center padding\" ng-if=\"loading\">\n      <ion-spinner icon=\"android\"></ion-spinner>\n    </div>\n\n    <!-- certifications tables -->\n    <div class=\"row responsive-sm responsive-md responsive-lg\">\n      <!-- Received certifications -->\n      <div class=\"col no-padding\" ng-if=\"motions.receivedCertifications.enable\">\n        <ng-include src=\"\'templates/wot/items_received_certifications.html\'\"></ng-include>\n      </div>\n\n      <!-- Avatar -->\n      <div class=\"col col-20 col-avatar hidden-xs hidden-sm hidden-md no-padding\" style=\"margin-top: 100px\" ng-if=\"motions.avatar.enable\">\n        <div class=\"row no-padding\" ng-class=\"::motions.avatar.ionListClass\">\n          <div class=\"col text-center no-padding gray\" style=\"margin-top: 30px\">\n            <i class=\"icon ion-arrow-right-a\" style=\"font-size:30px\"></i>\n          </div>\n          <div class=\"col text-center no-padding\">\n            <a style=\"text-decoration: none\" ui-sref=\"app.wot_identity({pubkey: formData.pubkey, uid: formData.uid})\">\n              <i class=\"avatar avatar-large\" ng-if=\"!formData.avatar\" ng-class=\"{\'avatar-wallet\': !formData.isMember, \'avatar-member\': formData.isMember}\"></i>\n              <i class=\"avatar avatar-large\" ng-if=\"formData.avatar\" style=\"background-image: url({{::formData.avatar.src}})\"></i>\n              <h4 class=\"text-center\" ng-class=\"{\'positive\': formData.isMember, \'gray\': !formData.isMember}\">\n                {{::formData.name||formData.uid}}\n              </h4>\n              <h5 class=\"text-center gray\">\n                <i class=\"icon ion-key\"></i> {{formData.pubkey|formatPubkey}}\n              </h5>\n              <h5 class=\"assertive\">\n                <span ng-if=\"::(formData.name || formData.uid) && !formData.isMember && !revoked\" translate>WOT.NOT_MEMBER_PARENTHESIS</span>\n                <b ng-if=\"::(formData.name || formData.uid) && !formData.isMember && revoked\" translate>WOT.IDENTITY_REVOKED_PARENTHESIS</b>\n                <b ng-if=\"::(formData.name || formData.uid) && formData.isMember && revoked\" translate>WOT.MEMBER_PENDING_REVOCATION_PARENTHESIS</b>\n              </h5>\n            </a>\n          </div>\n          <div class=\"col text-center no-padding gray\" style=\"margin-top: 30px\">\n            <i class=\"icon ion-arrow-right-a\" style=\"font-size:30px\"></i>\n          </div>\n        </div>\n      </div>\n\n      <!-- Given certifications -->\n      <div class=\"col no-padding\" ng-if=\"motions.givenCertifications.enable\">\n        <ng-include src=\"\'templates/wot/items_given_certifications.html\'\"></ng-include>\n      </div>\n    </div>\n  </ion-content>\n\n    <!-- fab button -->\n  <div class=\"visible-xs visible-sm\">\n    <button id=\"fab-certify\" class=\"button button-fab button-fab-bottom-right button-energized-900 spin\" ng-if=\"canCertify && !alreadyCertified\" ng-click=\"certify()\">\n      <i class=\"icon ion-ribbon-b\"></i>\n    </button>\n    <button id=\"fab-select-certify\" class=\"button button-fab button-fab-bottom-right button-energized-900 spin\" ng-if=\"canSelectAndCertify\" ng-click=\"selectAndCertify()\">\n      <i class=\"icon ion-plus\"></i>\n    </button>\n  </div>\n</ion-view>\n");
$templateCache.put("templates/wot/view_identity.html","<ion-view left-buttons=\"leftButtons\" class=\"view-identity\">\n  <ion-nav-title>\n  </ion-nav-title>\n\n  <ion-content scroll=\"true\" class=\"refresher-top-bg\" ng-class=\"{\'member\': !loading && formData.isMember}\">\n\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"doUpdate(true)\">\n    </ion-refresher>\n\n    <div class=\"hero\">\n      <div class=\"content\" ng-if=\"!loading\">\n        <i class=\"avatar\" ng-if=\"::!formData.avatar\" ng-class=\"{\'avatar-wallet\': !formData.isMember, \'avatar-member\': formData.isMember}\"></i>\n        <i class=\"avatar\" ng-if=\"::formData.avatar\" style=\"background-image: url({{::formData.avatar.src}})\"></i>\n        <ng-if ng-if=\"::formData.name\">\n          <h3 class=\"light\">{{::formData.name}}</h3>\n        </ng-if>\n        <ng-if ng-if=\"::!formData.name\">\n          <h3 class=\"light\" ng-if=\"::formData.uid\">{{::formData.uid}}</h3>\n          <h3 class=\"light\" ng-if=\"::!formData.uid\"><i class=\"ion-key\"></i> {{::formData.pubkey | formatPubkey}}</h3>\n        </ng-if>\n        <h4 class=\"assertive\">\n          <ng-if ng-if=\"::(formData.name || formData.uid) && !formData.isMember && revoked\" translate>WOT.IDENTITY_REVOKED_PARENTHESIS</ng-if>\n          <ng-if ng-if=\"::(formData.name || formData.uid) && formData.isMember && revoked\" translate>WOT.MEMBER_PENDING_REVOCATION_PARENTHESIS</ng-if>\n        </h4>\n\n      </div>\n      <h4 class=\"content light\" ng-if=\"loading\">\n        <ion-spinner icon=\"android\"></ion-spinner>\n      </h4>\n\n\n    </div>\n\n    <!-- button bar-->\n    <a id=\"wot-share-anchor-{{::formData.pubkey}}\"></a>\n    <div class=\"hidden-xs hidden-sm padding text-center\">\n      <button class=\"button button-stable button-small-padding icon ion-android-share-alt ink\" ng-click=\"showSharePopover($event)\" title=\"{{\'COMMON.BTN_SHARE\' | translate}}\">\n      </button>\n\n      <!-- Allow extension here -->\n      <cs-extension-point name=\"buttons\"></cs-extension-point>\n\n      <button class=\"button button-stable button-small-padding icon ion-ribbon-b ink\" ng-click=\"certify()\" ng-if=\":rebind:formData.hasSelf\" title=\"{{\'WOT.BTN_CERTIFY\' | translate}}\" ng-disabled=\"disableCertifyButton\">\n      </button>\n\n      <button class=\"button button-calm ink\" ng-click=\"showTransferModal({pubkey:formData.pubkey, uid: formData.name||formData.uid})\">\n        {{\'COMMON.BTN_SEND_MONEY\' | translate}}\n      </button>\n    </div>\n\n    <div class=\"row no-padding\">\n\n      <div class=\"visible-xs visible-sm\">\n        <button id=\"fab-certify-{{:rebind:formData.uid}}\" style=\"top: 170px\" class=\"button button-fab button-fab-top-left button-fab-hero button-calm spin\" ng-if=\":rebind:(canCertify && !alreadyCertified)\" ng-click=\"certify()\">\n          <i class=\"icon ion-ribbon-b\"></i>\n        </button>\n      </div>\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;</div>\n\n      <div class=\"col list\" ng-class=\"::motion.ionListClass\" bind-notifier=\"{ rebind:loading}\">\n\n        <span class=\"item item-divider\" translate>WOT.GENERAL_DIVIDER</span>\n\n        <!-- Pubkey -->\n        <ion-item class=\"item-icon-left item-text-wrap ink\" copy-on-click=\"{{:rebind:formData.pubkey}}\">\n          <i class=\"icon ion-key\"></i>\n          <span translate>COMMON.PUBKEY</span>\n          <h4 id=\"pubkey\" class=\"dark text-left\">{{:rebind:formData.pubkey}}</h4>\n        </ion-item>\n\n        <div class=\"item item-icon-left item-text-wrap\" ng-if=\":rebind:!formData.hasSelf\">\n          <i class=\"icon ion-ios-help-outline positive\"></i>\n          <span translate>WOT.NOT_MEMBER_ACCOUNT</span>\n          <h4 class=\"gray\" translate>WOT.NOT_MEMBER_ACCOUNT_HELP</h4>\n        </div>\n\n        <!-- Uid + Registration date -->\n        <ion-item class=\"item-icon-left\" ng-if=\":rebind:formData.sigDate||formData.uid\">\n          <i class=\"icon ion-calendar\"></i>\n          <span translate>COMMON.UID</span>\n          <h5 class=\"dark\" ng-if=\":rebind:formData.sigDate\">\n            <span translate>WOT.REGISTERED_SINCE</span>\n            {{:rebind:formData.sigDate | formatDate}}\n          </h5>\n          <span class=\"badge badge-stable\">{{:rebind:formData.uid}}</span>\n        </ion-item>\n\n        <!-- Received certifications count -->\n        <a id=\"helptip-wot-view-certifications\" class=\"item item-icon-left item-text-wrap item-icon-right ink\" ng-if=\":rebind:formData.hasSelf\" ng-click=\"showCertifications()\">\n          <i class=\"icon ion-ribbon-b\"></i>\n          <b ng-if=\":rebind:formData.requirements.isSentry\" class=\"ion-star icon-secondary\" style=\"color: yellow; font-size: 16px; left: 25px; top: -7px\"></b>\n          <span translate>ACCOUNT.CERTIFICATION_COUNT</span>\n          <cs-badge-certification cs-id=\"helptip-wot-view-certifications-count\" requirements=\"formData.requirements\" parameters=\"{sigQty: formData.sigQty}\">\n          </cs-badge-certification>\n\n          <i class=\"gray icon ion-ios-arrow-right\"></i>\n        </a>\n\n        <!-- Signature stock -->\n        <a class=\"item item-icon-left item-text-wrap item-icon-right ink visible-xs visible-sm\" ng-if=\":rebind:formData.hasSelf && formData.isMember\" ng-click=\"showGivenCertifications()\">\n          <i class=\"icon ion-ribbon-a\"></i>\n          <span translate>WOT.GIVEN_CERTIFICATIONS.SENT</span>\n          <cs-badge-given-certification identity=\"formData\" parameters=\"{sigStock: formData.sigStock}\">\n          </cs-badge-given-certification>\n          <i class=\"gray icon ion-ios-arrow-right\"></i>\n        </a>\n\n        <!-- Account transaction -->\n        <a class=\"item item-icon-left item-icon-right ink\" ng-if=\"!loading\" ui-sref=\"app.wot_identity_tx_uid({uid:formData.uid,pubkey:formData.pubkey})\">\n          <i class=\"icon ion-card\"></i>\n          <span translate>WOT.ACCOUNT_OPERATIONS</span>\n          <i class=\"gray icon ion-ios-arrow-right\"></i>\n        </a>\n\n        <div class=\"item item-text-wrap item-icon-left item-wallet-event assertive\" ng-repeat=\"event in :rebind:formData.events | filter: {type: \'error\'}\">\n          <i class=\"icon ion-alert-circled\"></i>\n          <span trust-as-html=\"event.message | translate:event.messageParams\"></span>\n        </div>\n\n        <cs-extension-point name=\"general\"></cs-extension-point>\n\n        <cs-extension-point name=\"after-general\"></cs-extension-point>\n\n      </div>\n\n      <div class=\"col col-20 hidden-xs hidden-sm\">&nbsp;</div>\n    </div>\n\n  </ion-content>\n\n  <!-- fab button -->\n  <div class=\"visible-xs visible-sm\" ng-hide=\"loading\">\n    <button id=\"fab-transfer\" class=\"button button-fab button-fab-bottom-right button-assertive drop\" ng-click=\"showTransferModal({pubkey:formData.pubkey, uid: formData.uid})\">\n      <i class=\"icon ion-android-send\"></i>\n    </button>\n  </div>\n</ion-view>\n");
$templateCache.put("templates/wot/view_identity_tx.html","<ion-view left-buttons=\"leftButtons\">\n  <ion-nav-title>\n    <span class=\"visible-xs visible-sm\">{{::formData.name||formData.uid}}</span>\n    <span class=\"hidden-xs hidden-sm\" ng-if=\"!loading\" translate=\"WOT.OPERATIONS.TITLE\" translate-values=\"{uid: formData.name || formData.uid}\"></span>\n  </ion-nav-title>\n\n  <ion-content scroll=\"true\">\n\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"doUpdate(true)\">\n    </ion-refresher>\n\n    <div class=\"hidden-xs hidden-sm padding text-center\">\n\n      <button class=\"button button-stable button-small-padding icon ion-loop ink\" ng-click=\"doUpdate()\" title=\"{{\'COMMON.BTN_REFRESH\' | translate}}\">\n      </button>\n\n      <button class=\"button button-stable button-small-padding icon ion-android-download ink\" ng-click=\"downloadHistoryFile()\" title=\"{{\'COMMON.BTN_DOWNLOAD_ACCOUNT_STATEMENT\' | translate}}\">\n      </button>\n\n      <cs-extension-point name=\"buttons\"></cs-extension-point>\n\n    </div>\n\n    <div class=\"center padding\" ng-if=\"loading\">\n      <ion-spinner icon=\"android\"></ion-spinner>\n    </div>\n\n    <div class=\"list {{motion.ionListClass}}\" ng-if=\"!loading\">\n\n      <div class=\"row\">\n\n        <div class=\"col col-15 hidden-xs hidden-sm\">&nbsp;</div>\n\n        <div class=\"col\">\n\n          <!-- the balance -->\n          <div class=\"item item-tx item-divider\">\n            {{\'ACCOUNT.BALANCE_ACCOUNT\'|translate}}\n            <div class=\"badge item-note badge-balanced\" ng-bind-html=\":balance:formData.balance|formatAmount:{currency: $root.currency.name}\">\n            </div>\n            <div class=\"badge badge-secondary\" ng-if=\"$root.settings.expertMode\">\n              (<span ng-bind-html=\":balance:formData.balance| formatAmount: {useRelative: !$root.settings.useRelative, currency: $root.currency.name} \"></span>)\n            </div>\n          </div>\n\n          <span class=\"item item-divider\" ng-if=\"!loading\">\n            {{:locale:\'ACCOUNT.LAST_TX\'|translate}}\n            <a id=\"helptip-wallet-tx\" style=\"position: relative; bottom: 0; right: 0px\">&nbsp;</a>\n          </span>\n\n          <!-- iterate on each TX -->\n          <div ng-repeat=\"tx in formData.tx.history\" class=\"item item-tx item-icon-left\" ng-include=\"::!tx.isUD ? \'templates/wallet/item_tx.html\' : \'templates/wallet/item_ud.html\'\">\n          </div>\n\n          <div class=\"item item-text-wrap text-center\" ng-if=\"formData.tx.fromTime > 0\">\n            <p>\n              <a ng-click=\"showMoreTx()\">{{:locale:\'ACCOUNT.SHOW_MORE_TX\'|translate}}</a>\n              <span class=\"gray\" translate=\"ACCOUNT.TX_FROM_DATE\" translate-values=\"{fromTime: tx.fromTime}\"></span>\n              <span class=\"gray\">|</span>\n              <a ng-click=\"showMoreTx(-1)\" translate>ACCOUNT.SHOW_ALL_TX</a>\n            </p>\n          </div>\n        </div>\n\n        <div class=\"col col-15 hidden-xs hidden-sm\">&nbsp;</div>\n\n      </div>\n    </div>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/currency/tabs/tab_blocks.html","<ion-view>\n  <ion-nav-buttons side=\"secondary\">\n\n    <button class=\"button button-icon button-clear icon ion-navicon visible-xs visible-sm\" ng-click=\"toggleCompactMode()\">\n      <b class=\"icon-secondary\" ng-class=\"{\'ion-arrow-down-b\': !compactMode, \'ion-arrow-up-b\': compactMode}\" style=\"top: -12px; left: 11px; font-size: 10px\"></b>\n      <b class=\"icon-secondary\" ng-class=\"{\'ion-arrow-up-b\': !compactMode,\'ion-arrow-down-b\': compactMode}\" style=\"top: 12px; left: 11px; font-size: 10px\"></b>\n    </button>\n\n    <!-- Allow extension here -->\n    <cs-extension-point name=\"nav-buttons\"></cs-extension-point>\n  </ion-nav-buttons>\n\n  <ion-content>\n    <div class=\"item item-divider\">\n      <span translate>BLOCKCHAIN.LOOKUP.LAST_BLOCKS</span>\n    </div>\n\n    <cs-extension-point name=\"buttons\"></cs-extension-point>\n\n    <ng-include src=\"\'templates/blockchain/list_blocks.html\'\"></ng-include>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/currency/tabs/tab_network.html","<ion-view>\n  <ion-nav-buttons side=\"secondary\">\n\n    <button class=\"button button-icon button-clear\" ng-click=\"showExtendActionsPopover($event)\">\n      <i class=\"icon ion-android-funnel\"></i>\n    </button>\n\n  </ion-nav-buttons>\n\n  <ion-content>\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"refreshPeers()\">\n    </ion-refresher>\n\n    <div class=\"list\">\n      <ng-include src=\"\'templates/currency/items_network.html\'\"></ng-include>\n    </div>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/currency/tabs/tab_parameters.html","<ion-view>\n\n  <ion-nav-buttons side=\"secondary\">\n    <!-- Allow extension here -->\n    <cs-extension-point name=\"nav-buttons\"></cs-extension-point>\n  </ion-nav-buttons>\n\n  <ion-content>\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"refresh()\">\n    </ion-refresher>\n\n    <div class=\"list\">\n      <ng-include src=\"\'templates/currency/items_parameters.html\'\"></ng-include>\n    </div>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/currency/tabs/tab_wot.html","<ion-view>\n  <ion-content>\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"refresh()\">\n    </ion-refresher>\n    <div class=\"list\">\n      <ng-include src=\"\'templates/currency/items_wot.html\'\"></ng-include>\n    </div>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/wallet/slides/slides_recoverID_1.html","<ion-content class=\"has-header padding\">\n  <h3 translate>ACCOUNT.SECURITY.RECOVER_ID</h3>\n\n  <div dropzone=\"recoverContent(file)\">\n    <div ng-if=\"!hasContent\" onclick=\"angular.element(document.querySelector(\'#saveIdFile\'))[0].click();\">\n      <h2 class=\"gray\" translate>COMMON.CHOOSE_FILE</h2>\n      <input type=\"file\" id=\"saveIdFile\" accept=\".txt\" style=\"visibility:hidden; position:absolute\" on-read-file=\"recoverContent(file)\">\n    </div>\n\n    <div ng-if=\"hasContent\" class=\"item row item-icon-left no-padding\">\n     <i class=\"icon ion-document-text gray\"></i>\n     <div class=\"col\">\n       <span>{{fileData.name}}</span>\n       <br>\n       <small>{{fileData.size}} Ko</small>\n     </div>\n     <div class=\"col-10\">\n       <b ng-class=\"{\'ion-android-done balanced\': isValidFile,\'ion-close-circled assertive\': !isValidFile}\" style=\"font-size: 28px; position: relative; top: 6px\"></b>\n        <button class=\"button-icon ion-close-round gray pull-right\" style=\"font-size:10px\" ng-click=\"restore()\"></button>\n     </div>\n   </div>\n  </div>\n\n  <div class=\"padding hidden-xs text-right\">\n   <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n   </button>\n   <button class=\"button button-calm icon-right ion-chevron-right ink\" ng-click=\"doNext()\" translate>\n     COMMON.BTN_NEXT\n     <i class=\"icon ion-arrow-right-a\"></i>\n   </button>\n  </div>\n</ion-content>\n\n\n");
$templateCache.put("templates/wallet/slides/slides_recoverID_2.html","  <ion-content class=\"has-header padding\">\n    <h3 translate>ACCOUNT.SECURITY.RECOVER_ID</h3>\n\n    <form name=\"recoverForm\" novalidate ng-submit=\"recoverId()\">\n      <div class=\"list\" ng-init=\"setForm(recoverForm, \'recoverForm\')\">\n        <ng-repeat ng-repeat=\"question in recover.questions \">\n          <label class=\"item item-input {{smallscreen ? \'item-stacked-label\' : \'item-floating-label\'}}\" ng-class=\"{\'item-input-error\': recoverForm.$submitted && recoverForm[\'question{{$index}}\'].$invalid}\">\n            <span class=\"input-label\" style=\"{{smallscreen ? \'white-space: normal\' : \'\'}}\">{{question.value }}</span>\n            <input type=\"text\" name=\"question{{$index}}\" placeholder=\"{{smallscreen ? \'\' : question.value }}\" ng-model=\"question.answer\" required>\n          </label>\n          <div class=\"form-errors\" ng-show=\"recoverForm.$submitted && recoverForm[\'question{{$index}}\'].$error\" ng-messages=\"recoverForm[\'question{{$index}}\'].$error\">\n            <div class=\"form-error\" ng-message=\"required\">\n              <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n            </div>\n          </div>\n        </ng-repeat>\n        <div class=\"padding hidden-xs text-right\">\n          <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n          </button>\n          <button class=\"button button-clear button-dark\" ng-click=\"restore()\" type=\"button\" translate>ACCOUNT.SECURITY.BTN_CLEAN\n          </button>\n          <button class=\"button button-calm icon-right ion-chevron-right ink\" type=\"submit\" translate>\n            COMMON.BTN_NEXT\n            <i class=\"icon ion-arrow-right-a\"></i>\n          </button>\n        </div>\n      </div>\n    </form>\n  </ion-content>\n\n");
$templateCache.put("templates/wallet/slides/slides_recoverID_3.html","  <ion-content class=\"has-header padding\">\n    <h3 translate>ACCOUNT.SECURITY.RECOVER_ID</h3>\n\n\n    <div class=\"item item-input\">\n      <span class=\"input-label\">{{\'LOGIN.SALT\' | translate}} :</span>\n      <span>{{recover.salt}}</span>\n    </div>\n\n    <div class=\"item item-input\">\n      <span class=\"input-label\">{{\'LOGIN.PASSWORD\' | translate}} :</span>\n      <span>{{recover.pwd}}</span>\n    </div>\n\n    <div class=\"padding hidden-xs text-right\">\n          <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CLOSE\n          </button>\n    </div>\n  </ion-content>\n\n");
$templateCache.put("templates/wallet/slides/slides_revocation_file.html","<ion-content class=\"has-header padding\">\n  <p translate>ACCOUNT.SECURITY.REVOCATION_WITH_FILE_HELP</p>\n\n  <div dropzone=\"recoverContent(file)\">\n    <div ng-if=\"!hasContent\" onclick=\"angular.element(document.querySelector(\'#revocationFile\'))[0].click();\">\n      <h2 class=\"gray\" translate>COMMON.CHOOSE_FILE</h2>\n      <input type=\"file\" id=\"revocationFile\" accept=\".txt\" style=\"visibility:hidden; position:absolute\" on-read-file=\"recoverContent(file)\">\n    </div>\n\n    <div ng-if=\"hasContent\" class=\"item row item-icon-left no-padding\">\n      <i class=\"icon ion-document-text gray\"></i>\n      <div class=\"col\">\n        <span>{{fileData.name}}</span>\n        <br>\n        <small>{{fileData.size}} Ko</small>\n      </div>\n      <div class=\"col-10\">\n        <b ng-class=\"{\'ion-android-done balanced\': isValidFile,\'ion-close-circled assertive\': !isValidFile}\" style=\"font-size: 28px; position: relative; top: 6px\"></b>\n        <button class=\"button-icon ion-close-round gray pull-right\" style=\"font-size:10px\" ng-click=\"restore()\"></button>\n      </div>\n    </div>\n  </div>\n\n  <div class=\"padding hidden-xs text-right\">\n    <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n    </button>\n    <button class=\"button button-calm icon-right ion-chevron-right ink\" ng-click=\"revokeWithFile()\" translate>\n      COMMON.BTN_NEXT\n      <i class=\"icon ion-arrow-right-a\"></i>\n    </button>\n  </div>\n</ion-content>\n\n\n");
$templateCache.put("templates/wallet/slides/slides_saveID_1.html","  <ion-content class=\"has-header padding\">\n    <h3 translate>ACCOUNT.SECURITY.SAVE_ID</h3>\n    <label class=\"item item-input item-select\">\n      <div class=\"input-label\" translate>\n        ACCOUNT.SECURITY.LEVEL\n      </div>\n      <select ng-model=\"formData.level\">\n        <option value=\"2\" ng-bind-html=\"\'ACCOUNT.SECURITY.LOW_LEVEL\' | translate\"></option>\n        <option value=\"4\" translate>ACCOUNT.SECURITY.MEDIUM_LEVEL</option>\n        <option value=\"6\" translate>ACCOUNT.SECURITY.STRONG_LEVEL</option>\n      </select>\n    </label>\n    <div class=\"padding-top\" translate=\"ACCOUNT.SECURITY.HELP_LEVEL\" translate-values=\"{nb: {{formData.level}}}\"></div>\n    <form name=\"questionsForm\" novalidate ng-submit=\"doNext(\'questionsForm\')\">\n      <div class=\"list\" ng-init=\"setForm(questionsForm, \'questionsForm\')\">\n\n        <ion-checkbox ng-repeat=\"question in formData.questions\" ng-model=\"question.checked\" ng-required=\"isRequired()\">\n          <span style=\"white-space: normal\">{{question.value | translate}}</span>\n        </ion-checkbox>\n        <div class=\"item item-icon-right no-padding-top\">\n          <a class=\"dark\"><i class=\"icon ion-android-add\" ng-click=\"addQuestion()\"></i></a>\n          <div class=\"list list-inset\">\n            <label class=\"item item-input\">\n              <input type=\"text\" placeholder=\"{{\'ACCOUNT.SECURITY.ADD_QUESTION\' | translate}}\" ng-model=\"formData.addQuestion\">\n            </label>\n          </div>\n        </div>\n      </div>\n      <div class=\"padding hidden-xs text-right\">\n        <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n        </button>\n        <button class=\"button button-clear button-dark\" ng-click=\"restore()\" type=\"button\" translate>ACCOUNT.SECURITY.BTN_RESET\n        </button>\n        <button class=\"button button-calm icon-right ion-chevron-right ink\" ng-disabled=\"questionsForm.$invalid\" type=\"submit\" translate>\n          COMMON.BTN_NEXT\n          <i class=\"icon ion-arrow-right-a\"></i>\n        </button>\n      </div>\n    </form>\n\n  </ion-content>\n");
$templateCache.put("templates/wallet/slides/slides_saveID_2.html","  <ion-content class=\"has-header padding\">\n    <h3 translate>ACCOUNT.SECURITY.SAVE_ID</h3>\n\n    <form name=\"answersForm\" novalidate ng-submit=\"doNext(\'answersForm\')\">\n      <div class=\"list\" ng-init=\"setForm(answersForm, \'answersForm\')\">\n        <ng-repeat ng-repeat=\"question in formData.questions |filter:true:checked\">\n          <label class=\"item item-input item-text-wrap {{smallscreen ? \'item-stacked-label\' : \'item-floating-label\'}}\" ng-class=\"{\'item-input-error\': answersForm.$submitted && answersForm[\'question{{$index}}\'].$invalid}\">\n            <span class=\"input-label\" style=\"width: 100%; max-width: inherit\">{{question.value | translate}}</span>\n            <input type=\"text\" name=\"question{{$index}}\" placeholder=\"{{smallscreen ? \'\' : question.value | translate}}\" ng-model=\"question.answer\" required>\n          </label>\n          <div class=\"form-errors\" ng-show=\"answersForm.$submitted && answersForm[\'question{{$index}}\'].$error\" ng-messages=\"answersForm[\'question{{$index}}\'].$error\">\n            <div class=\"form-error\" ng-message=\"required\">\n              <span translate=\"ERROR.FIELD_REQUIRED\"></span>\n            </div>\n          </div>\n        </ng-repeat>\n        <div class=\"padding hidden-xs text-right\">\n          <button class=\"button button-clear button-dark ink\" ng-click=\"closeModal()\" type=\"button\" translate>COMMON.BTN_CANCEL\n          </button>\n          <button class=\"button button-clear button-dark\" ng-click=\"restore()\" type=\"button\" translate>ACCOUNT.SECURITY.BTN_CLEAN\n          </button>\n          <button class=\"button button-positive ink\" type=\"submit\" translate>\n            COMMON.BTN_CONTINUE\n            <i class=\"icon ion-android-archive\"></i>\n          </button>\n        </div>\n      </div>\n\n    </form>\n  </ion-content>\n");
$templateCache.put("templates/wot/tabs/tab_given_certifications.html","<ion-view>\n  <ion-nav-buttons side=\"secondary\">\n    <button class=\"button button-icon button-clear icon ion-loop\" ng-click=\"doUpdate()\">\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content ng-init=\"motions.receivedCertifications=false; motions.avatar=false\">\n    <div class=\"center padding\" ng-if=\"loading\">\n      <ion-spinner icon=\"android\"></ion-spinner>\n    </div>\n\n    <ng-include src=\"\'templates/wot/items_given_certifications.html\'\"></ng-include>\n  </ion-content>\n\n  <!-- fab button -->\n  <div class=\"visible-xs visible-sm\">\n    <button id=\"fab-select-certify\" class=\"button button-fab button-fab-bottom-right button-energized-900 spin\" ng-if=\"canSelectAndCertify || $root.tour\" ng-click=\"selectAndCertify()\">\n      <i class=\"icon ion-plus\"></i>\n    </button>\n  </div>\n</ion-view>\n");
$templateCache.put("templates/wot/tabs/tab_lookup.html","<ion-view>\n  <ion-nav-buttons side=\"secondary\">\n\n    <!-- Allow extension here -->\n    <cs-extension-point name=\"nav-buttons\"></cs-extension-point>\n\n    <button class=\"button button-icon button-clear\" ng-click=\"showActionsPopover($event)\">\n      <i class=\"icon ion-android-funnel\"></i>\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content>\n\n    <ion-refresher pulling-text=\"{{\'COMMON.BTN_REFRESH\' | translate}}\" on-refresh=\"doSearch()\">\n    </ion-refresher>\n\n    <cs-extension-point name=\"buttons\"></cs-extension-point>\n\n    <ng-include src=\"\'templates/wot/lookup_form.html\'\"></ng-include>\n  </ion-content>\n</ion-view>\n");
$templateCache.put("templates/wot/tabs/tab_received_certifications.html","<ion-view>\n  <ion-nav-buttons side=\"secondary\">\n    <button class=\"button button-icon button-clear icon ion-loop\" ng-click=\"doUpdate()\">\n    </button>\n  </ion-nav-buttons>\n\n  <ion-content ng-init=\"motions.givenCertifications=false; motions.avatar=false;\" class=\"has-header\">\n    <div class=\"center padding\" ng-if=\"loading\">\n      <ion-spinner icon=\"android\"></ion-spinner>\n    </div>\n\n    <ng-include src=\"\'templates/wot/items_received_certifications.html\'\"></ng-include>\n  </ion-content>\n\n  <!-- fab button -->\n  <div class=\"visible-xs visible-sm\">\n    <button id=\"fab-certify\" class=\"button button-fab button-fab-bottom-right button-energized-900 spin\" ng-click=\"certify()\" ng-if=\"(formData.hasSelf && canCertify && !alreadyCertified) || $root.tour\">\n      <i class=\"icon ion-ribbon-b\"></i>\n    </button>\n  </div>\n</ion-view>\n");}]);
angular.module("cesium.translations", []).config(["$translateProvider", function($translateProvider) {
$translateProvider.translations("en-GB", {
  "COMMON": {
    "APP_NAME": "Cesium",
    "APP_VERSION": "v{{version}}",
    "APP_BUILD": "build {{build}}",
    "PUBKEY": "Public key",
    "MEMBER": "Member",
    "BLOCK" : "Block",
    "BTN_OK": "OK",
    "BTN_YES": "Yes",
    "BTN_NO": "No",
    "BTN_SEND": "Send",
    "BTN_SEND_MONEY": "Transfer money",
    "BTN_SEND_MONEY_SHORT": "Transfer",
    "BTN_SAVE": "Save",
    "BTN_YES_SAVE": "Yes, Save",
    "BTN_YES_CONTINUE": "Yes, Continue",
    "BTN_SHOW": "Show",
    "BTN_SHOW_PUBKEY": "Show key",
    "BTN_RELATIVE_UNIT": "Use relative unit",
    "BTN_BACK": "Back",
    "BTN_NEXT": "Next",
    "BTN_CANCEL": "Cancel",
    "BTN_CLOSE": "Close",
    "BTN_LATER": "Later",
    "BTN_LOGIN": "Sign In",
    "BTN_LOGOUT": "Logout",
    "BTN_ADD_ACCOUNT": "New Account",
    "BTN_SHARE": "Share",
    "BTN_EDIT": "Edit",
    "BTN_DELETE": "Delete",
    "BTN_ADD": "Add",
    "BTN_SEARCH": "Search",
    "BTN_REFRESH": "Refresh",
    "BTN_RETRY": "Retry",
    "BTN_START": "Start",
    "BTN_CONTINUE": "Continue",
    "BTN_CREATE": "Create",
    "BTN_UNDERSTOOD": "I understood",
    "BTN_OPTIONS": "Options",
    "BTN_HELP_TOUR": "Features tour",
    "BTN_HELP_TOUR_SCREEN": "Discover this screen",
    "BTN_DOWNLOAD": "Download",
    "BTN_DOWNLOAD_ACCOUNT_STATEMENT": "Download account statement",
    "BTN_MODIFY": "Modify",
    "CHOOSE_FILE": "Drag your file<br/>or click to select",
    "DAYS": "days",
    "NO_ACCOUNT_QUESTION": "Not a member yet? Register now!",
    "SEARCH_NO_RESULT": "No result found",
    "LOADING": "Loading...",
    "SEARCHING": "Searching...",
    "FROM": "From",
    "TO": "To",
    "COPY": "Copy",
    "LANGUAGE": "Language",
    "UNIVERSAL_DIVIDEND": "Universal dividend",
    "UD": "UD",
    "DATE_PATTERN": "DD/MM/YYYY HH:mm",
    "DATE_FILE_PATTERN": "YYYY-MM-DD",
    "DATE_SHORT_PATTERN": "DD/MM/YY",
    "DATE_MONTH_YEAR_PATTERN": "MM/YYYY",
    "EMPTY_PARENTHESIS": "(empty)",
    "UID": "Pseudonym",
    "ENABLE": "Enabled",
    "DISABLE": "Disabled",
    "RESULTS_LIST": "Results:",
    "RESULTS_COUNT": "{{count}} results",
    "EXECUTION_TIME": "executed in {{duration|formatDurationMs}}",
    "SHOW_VALUES": "Display values openly?",
    "POPOVER_ACTIONS_TITLE": "Options",
    "POPOVER_FILTER_TITLE": "Filters",
    "SHOW_MORE": "Show more",
    "SHOW_MORE_COUNT": "(current limit at {{limit}})",
    "POPOVER_SHARE": {
      "TITLE": "Share",
      "SHARE_ON_TWITTER": "Share on Twitter",
      "SHARE_ON_FACEBOOK": "Share on Facebook",
      "SHARE_ON_DIASPORA": "Share on Diaspora*",
      "SHARE_ON_GOOGLEPLUS": "Share on Google+"
    }
  },
  "SYSTEM": {
    "PICTURE_CHOOSE_TYPE": "Choose source:",
    "BTN_PICTURE_GALLERY": "Gallery",
    "BTN_PICTURE_CAMERA": "<b>Camera</b>"
  },
  "MENU": {
    "HOME": "Home",
    "WOT": "Registry",
    "CURRENCY": "Currency",
    "ACCOUNT": "My Account",
    "TRANSFER": "Transfer",
    "SCAN": "Scan",
    "SETTINGS": "Settings",
    "NETWORK": "Network",
    "TRANSACTIONS": "My transactions"
  },
  "ABOUT": {
    "TITLE": "About",
    "LICENSE": "<b>Free/libre software</b> (License GNU GPLv3).",
    "LATEST_RELEASE": "There is a <b>newer version</ b> of {{'COMMON.APP_NAME' | translate}} (<b>v{{version}}</b>)",
    "PLEASE_UPDATE": "Please update {{'COMMON.APP_NAME' | translate}} (latest version: <b>v{{version}}</b>)",
    "CODE": "Source code:",
    "DEVELOPERS": "Developers:",
    "FORUM": "Forum:",
    "PLEASE_REPORT_ISSUE": "Please report any issue to us!",
    "REPORT_ISSUE": "Report an issue"
  },
  "HOME": {
    "TITLE": "Cesium",
    "WELCOME": "Welcome to the Cesium Application!",
    "MESSAGE": "Follow your {{currency|abbreviate}} wallets easily",
    "BTN_CURRENCY": "Explore currency",
    "BTN_ABOUT": "about",
    "BTN_HELP": "Help",
    "REPORT_ISSUE": "Report an issue",
    "NOT_YOUR_ACCOUNT_QUESTION" : "You do not own the account <b><i class=\"ion-key\"></i> {{pubkey|formatPubkey}}</b>?",
    "BTN_CHANGE_ACCOUNT": "Disconnect this account",
    "CONNECTION_ERROR": "Peer <b>{{server}}</b> unreachable or invalid address.<br/><br/>Check your Internet connection, or change node <a class=\"positive\" ng-click=\"doQuickFix('settings')\">in the settings</a>."
  },
  "SETTINGS": {
    "TITLE": "Settings",
    "NETWORK_SETTINGS": "Network",
    "PEER": "Duniter peer address",
    "PEER_CHANGED_TEMPORARY": "Address used temporarily",
    "USE_LOCAL_STORAGE": "Enable local storage",
    "USE_LOCAL_STORAGE_HELP": "Allows you to save your settings",
    "ENABLE_HELPTIP": "Enable contextual help tips",
    "ENABLE_UI_EFFECTS": "Enable visual effects",
    "HISTORY_SETTINGS": "Account operations",
    "DISPLAY_UD_HISTORY": "Display produced dividends?",
    "AUTHENTICATION_SETTINGS": "Authentication",
    "KEEP_AUTH": "Expiration of authentication",
    "KEEP_AUTH_HELP": "Define when authentication is cleared from memory",
    "KEEP_AUTH_OPTION": {
      "NEVER": "After each operation",
      "SECONDS": "After {{value}}s of inactivity",
      "MINUTE": "After {{value}}min of inactivity",
      "MINUTES": "After {{value}}min of inactivity",
      "HOUR": "After {{value}}h of inactivity",
      "ALWAYS": "At the end of the session"
    },
    "REMEMBER_ME": "Remember me ?",
    "REMEMBER_ME_HELP": "Allows to remain identified from one session to another, keeping the public key locally.",
    "PLUGINS_SETTINGS": "Extensions",
    "BTN_RESET": "Restore default values",
    "EXPERT_MODE": "Enable expert mode",
    "EXPERT_MODE_HELP": "Allow to see more details",
    "POPUP_PEER": {
      "TITLE" : "Duniter peer",
      "HOST" : "Address",
      "HOST_HELP": "Address: server:port",
      "USE_SSL" : "Secured?",
      "USE_SSL_HELP" : "(SSL Encryption)",
      "BTN_SHOW_LIST" : "Peer's list"
    }
  },
  "BLOCKCHAIN": {
    "HASH": "Hash: {{hash}}",
    "VIEW": {
      "HEADER_TITLE": "Block #{{number}}-{{hash|formatHash}}",
      "TITLE_CURRENT": "Current block",
      "TITLE": "Block #{{number|formatInteger}}",
      "COMPUTED_BY": "Computed by",
      "SHOW_RAW": "Show raw data",
      "TECHNICAL_DIVIDER": "Technical informations",
      "VERSION": "Format version",
      "HASH": "Computed hash",
      "UNIVERSAL_DIVIDEND_HELP": "Money co-produced by each of the {{membersCount}} members",
      "EMPTY": "Aucune donnée dans ce bloc",
      "POW_MIN": "Minimal difficulty",
      "POW_MIN_HELP": "Difficulty imposed in calculating hash",
      "DATA_DIVIDER": "Data",
      "IDENTITIES_COUNT": "New identities",
      "JOINERS_COUNT": "Joiners",
      "ACTIVES_COUNT": "Renewals",
      "ACTIVES_COUNT_HELP": "Members having renewed their membership",
      "LEAVERS_COUNT": "Leavers",
      "LEAVERS_COUNT_HELP": "Members that now refused certification",
      "EXCLUDED_COUNT": "Excluded members",
      "EXCLUDED_COUNT_HELP": "Old members, excluded because missing membreship renewal or certifications",
      "REVOKED_COUNT": "Revoked identities",
      "REVOKED_COUNT_HELP": "These accounts may no longer be member",
      "TX_COUNT": "Transactions",
      "CERT_COUNT": "Certifications",
      "TX_TO_HIMSELF": "Change",
      "TX_OUTPUT_UNLOCK_CONDITIONS": "Unlock conditions",
      "TX_OUTPUT_OPERATOR": {
        "AND": "and",
        "OR": "or"
      },
      "TX_OUTPUT_FUNCTION": {
        "SIG": "<b>Sign</b> of the public key",
        "XHX": "<b>Password</b>, including SHA256 =",
        "CSV": "Blocked during",
        "CLTV": "Bloqué until"
      }
    },
    "LOOKUP": {
      "TITLE": "Blocks",
      "NO_BLOCK": "No bloc",
      "LAST_BLOCKS": "Last blocks:",
      "BTN_COMPACT": "Compact"
    }
  },
  "CURRENCY": {
    "VIEW": {
      "TITLE": "Currency",
      "TAB_CURRENCY": "Currency",
      "TAB_WOT": "Web of trust",
      "TAB_NETWORK": "Network",
      "TAB_BLOCKS": "Blocks",
      "CURRENCY_SHORT_DESCRIPTION": "{{currency|capitalize}} is a <b>libre money</b>, started {{firstBlockTime | formatFromNow}}. It currently counts <b>{{N}} members </b>, who produce and collect a <a ng-click=\"showHelpModal('ud')\">Universal Dividend</a> (DU), each {{dt | formatPeriod}}.",
      "NETWORK_RULES_DIVIDER": "Network rules",
      "CURRENCY_NAME": "Currency name",
      "MEMBERS": "Members count",
      "MEMBERS_VARIATION": "Variation since {{duration|formatDuration}} (since last UD)",
      "MONEY_DIVIDER": "Money",
      "MASS": "Monetary mass",
      "SHARE": "Money share",
      "UD": "Universal Dividend",
      "C_ACTUAL": "Current growth",
      "MEDIAN_TIME": "Current blockchain time",
      "POW_MIN": "Common difficulty",
      "MONEY_RULES_DIVIDER": "Rules of currency",
      "C_RULE": "Theoretical growth target",
      "UD_RULE": "Universal dividend (formula)",
      "DT_REEVAL": "Period between two re-evaluation of the UD",
      "REEVAL_SYMBOL": "reeval",
      "DT_REEVAL_VALUE": "Every <b>{{dtReeval|formatDuration}}</b> ({{dtReeval/86400}} {{'COMMON.DAYS'|translate}})",
      "UD_REEVAL_TIME0": "Date of first reevaluation of the UD",
      "SIG_QTY_RULE": "Required number of certifications to become a member",
      "SIG_STOCK": "Maximum number of certifications sent by a member",
      "SIG_PERIOD": "Minimum delay between 2 certifications sent by one and the same issuer.",
      "SIG_WINDOW": "Maximum delay before a certification will be treated",
      "SIG_VALIDITY": "Lifetime of a certification that has been treated",
      "MS_WINDOW": "Maximum delay before a pending membership will be treated",
      "MS_VALIDITY": "Lifetime of a membership that has been treated",
      "STEP_MAX": "Maximum distance between a newcomer and each referring members.",
      "WOT_RULES_DIVIDER": "Rules for web of trust",
      "SENTRIES": "Required number of certifications (given <b>and</b> received) to become a referring member",
      "SENTRIES_FORMULA": "Required number of certifications to become a referring member (formula)",
      "XPERCENT":"Minimum percent of referring member to reach to match the distance rule",
      "AVG_GEN_TIME": "The average time between 2 blocks",
      "CURRENT": "current",
      "MATH_CEILING": "CEILING",
      "DISPLAY_ALL_RULES": "Display all rules?",
      "BTN_SHOW_LICENSE": "Show license",
      "WOT_DIVIDER": "Web of trust"
    },
    "LICENSE": {
      "TITLE": "Currency license",
      "BTN_DOWNLOAD": "Download file",
      "NO_LICENSE_FILE": "License file not found."
    }
  },
  "NETWORK": {
    "VIEW": {
      "MEDIAN_TIME": "Blockchain time",
      "LOADING_PEERS": "Loading peers...",
      "NODE_ADDRESS": "Address:",
      "SOFTWARE": "Software:",
      "WARN_PRE_RELEASE": "Pre-release (latest stable: <b>{{version}}</b>)",
      "WARN_NEW_RELEASE": "Version <b>{{version}}</b> available",
      "WS2PID": "Identifier:",
      "PRIVATE_ACCESS": "Private access",
      "POW_PREFIX": "Proof of work prefix:",
      "ENDPOINTS": {
        "BMAS": "Secure endpoint (SSL)",
        "BMATOR": "TOR endpoint",
        "WS2P": "WS2P endpoint",
        "ES_USER_API": "Cesium+ data node"
      }
    },
    "INFO": {
      "ONLY_SSL_PEERS": "Non-SSL nodes have a degraded display because Cesium works in HTTPS mode."
    }
  },
  "PEER": {
    "PEERS": "Peers",
    "SIGNED_ON_BLOCK": "Signed on block",
    "MIRROR": "mirror",
    "MIRRORS": "Mirror peers",
    "PEER_LIST" : "Peer's list",
    "MEMBERS" : "Member peers",
    "ALL_PEERS" : "All peers",
    "DIFFICULTY" : "Difficulty",
    "API" : "API",
    "CURRENT_BLOCK" : "Block #",
    "POPOVER_FILTER_TITLE": "Filter",
    "OFFLINE": "Offline peers",
    "BTN_SHOW_PEER": "Show peer",
    "VIEW": {
      "TITLE": "Peer",
      "OWNER": "Owned by ",
      "SHOW_RAW_PEERING": "See peering document",
      "SHOW_RAW_CURRENT_BLOCK": "See current block (raw format)",
      "LAST_BLOCKS": "Last blocks",
      "KNOWN_PEERS": "Known peers :",
      "GENERAL_DIVIDER": "General information",
      "ERROR": {
        "LOADING_TOR_NODE_ERROR": "Could not get peer data, using the TOR network.",
        "LOADING_NODE_ERROR": "Could not get peer data"
      }
    }
  },
  "WOT": {
    "SEARCH_HELP": "Search (member or public key)",
    "SEARCH_INIT_PHASE_WARNING": "During the pre-registration phase, the search for pending registrations <b>may be long</b>. Please wait ...",
    "REGISTERED_SINCE": "Registered on",
    "REGISTERED_SINCE_BLOCK": "Registered since block #",
    "NO_CERTIFICATION": "No validated certification",
    "NO_GIVEN_CERTIFICATION": "No given certification",
    "NOT_MEMBER_PARENTHESIS": "(non-member)",
    "IDENTITY_REVOKED_PARENTHESIS": "(identity revoked)",
    "MEMBER_PENDING_REVOCATION_PARENTHESIS": "(being revoked)",
    "EXPIRE_IN": "Expires",
    "NOT_WRITTEN_EXPIRE_IN": "Deadline<br/>treatment",
    "EXPIRED": "Expired",
    "PSEUDO": "Pseudonym",
    "SIGNED_ON_BLOCK": "Emitted on block #{{block}}",
    "WRITTEN_ON_BLOCK": "Written on block #{{block}}",
    "GENERAL_DIVIDER": "General information",
    "NOT_MEMBER_ACCOUNT": "Non-member account",
    "NOT_MEMBER_ACCOUNT_HELP": "This is a simple wallet, with no pending membership application.",
    "TECHNICAL_DIVIDER": "Technical data",
    "BTN_CERTIFY": "Certify",
    "BTN_YES_CERTIFY": "Yes, certify",
    "BTN_SELECT_AND_CERTIFY": "New certification",
    "ACCOUNT_OPERATIONS": "Account operations",
    "VIEW": {
      "POPOVER_SHARE_TITLE": "Identity {{title}}"
    },
    "LOOKUP": {
      "TITLE": "Registry",
      "NEWCOMERS": "New members:",
      "NEWCOMERS_COUNT": "{{count}} members",
      "PENDING": "Pending registrations:",
      "PENDING_COUNT": "{{count}} pending registrations",
      "REGISTERED": "Registered {{sigDate | formatFromNow}}",
      "MEMBER_FROM": "Member since {{memberDate|formatFromNowShort}}",
      "BTN_NEWCOMERS": "Latest members",
      "BTN_PENDING": "Pending registrations",
      "SHOW_MORE": "Show more",
      "SHOW_MORE_COUNT": "(current limit to {{limit}})",
      "NO_PENDING": "No pending registrations.",
      "NO_NEWCOMERS": "No members."
    },
    "CONTACTS": {
      "TITLE": "Contacts"
    },
    "MODAL": {
      "TITLE": "Search"
    },
    "CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certifications",
      "SUMMARY": "Received certifications",
      "LIST": "Details of received certifications",
      "PENDING_LIST": "Pending certifications",
      "RECEIVED": "Received certifications",
      "RECEIVED_BY": "Certifications received by {{uid}}",
      "ERROR": "Received certifications in error",
      "SENTRY_MEMBER": "Referring member"
    },
    "OPERATIONS": {
      "TITLE": "{{uid}} - Operations"
    },
    "GIVEN_CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certifications sent",
      "SUMMARY": "Sent certifications",
      "LIST": "Details of sent certifications",
      "PENDING_LIST": "Pending certifications",
      "SENT": "Sent certifications",
      "SENT_BY": "Certifications sent by {{uid}}",
      "ERROR": "Sent certifications with error"
    }
  },
  "LOGIN": {
    "TITLE": "<i class=\"icon ion-log-in\"></i> Login",
    "SCRYPT_FORM_HELP": "Please enter your credentials. <br> Remember to check the public key for your account.",
    "PUBKEY_FORM_HELP": "Please enter a public account key:",
    "FILE_FORM_HELP": "Choose the keychain file to use:",
    "SALT": "Secret identifier",
    "SALT_HELP": "Secret identifier",
    "SHOW_SALT": "Display secret identifier?",
    "PASSWORD": "Password",
    "PASSWORD_HELP": "Password",
    "PUBKEY_HELP": "Example: « AbsxSY4qoZRzyV2irfep1V9xw1EMNyKJw2TkuVD4N1mv »",
    "NO_ACCOUNT_QUESTION": "Don't have an account yet?",
    "CREATE_ACCOUNT": "Create an account",
    "FORGOTTEN_ID": "Forgot password?",
    "ASSOCIATED_PUBKEY": "Public key :",
    "BTN_METHODS": "Other methods",
    "BTN_METHODS_DOTS": "Change method...",
    "METHOD_POPOVER_TITLE": "Methods",
    "MEMORIZE_AUTH_FILE": "Memorize this keychain during the navigation session",
    "SCRYPT_PARAMETERS": "Paramètres (Scrypt) :",
    "AUTO_LOGOUT": {
      "TITLE": "Information",
      "MESSAGE": "<i class=\"ion-android-time\"></i> You were <b>logout</ b> automatically, due to prolonged inactivity.",
      "BTN_RELOGIN": "Sign In",
      "IDLE_WARNING": "You will be logout... {{countdown}}"
    },
    "METHOD": {
      "SCRYPT_DEFAULT": "Standard salt (default)",
      "SCRYPT_ADVANCED": "Advanced salt",
      "FILE": "Keychain file",
      "PUBKEY": "Public key only"
    },
    "SCRYPT": {
      "SIMPLE": "Light salt",
      "DEFAULT": "Standard salt",
      "SECURE": "Secure salt",
      "HARDEST": "Hardest salt",
      "EXTREME": "Extreme salt",
      "USER": "Personal value",
      "N": "N (Loop):",
      "r": "r (RAM):",
      "p": "p (CPU):"
    },
    "FILE": {
      "DATE" : "Date:",
      "TYPE" : "Type:",
      "SIZE": "Size:",
      "VALIDATING": "Validating...",
      "HELP": "Expected file format: <b>.dunikey</b> (type PubSec). Other formats are under development (EWIF, WIF)."
    }
  },
  "AUTH": {
    "TITLE": "<i class=\"icon ion-locked\"></i> Authentication",
    "METHOD_LABEL": "Authentication method",
    "BTN_AUTH": "Authenticate",
    "SCRYPT_FORM_HELP": "Please authenticate yourself:"
  },
  "ACCOUNT": {
    "TITLE": "My Account",
    "BALANCE": "Balance",
    "LAST_TX": "Latest transactions",
    "BALANCE_ACCOUNT": "Account balance",
    "NO_TX": "No transaction",
    "SHOW_MORE_TX": "Show more",
    "SHOW_ALL_TX": "Show all",
    "TX_FROM_DATE": "(current limit to {{fromTime|formatFromNowShort}})",
    "PENDING_TX": "Pending transactions",
    "ERROR_TX": "Transaction not executed",
    "ERROR_TX_SENT": "Sent transactions",
    "PENDING_TX_RECEIVED": "Transactions awaiting receipt",
    "EVENTS": "Events",
    "WAITING_MEMBERSHIP": "Membership application sent. Waiting validation.",
    "WAITING_CERTIFICATIONS": "You need {{needCertificationCount}} certification(s) to become a member",
    "WILL_MISSING_CERTIFICATIONS": "You will <b>lack certifications</b> soon (at least {{willNeedCertificationCount}} more are needed)",
    "WILL_NEED_RENEW_MEMBERSHIP": "Your membership <b>will expire {{membershipExpiresIn|formatDurationTo}}</b>. Remember to <a ng-click=\"doQuickFix('renew')\">renew your membership</a> before then.",
    "NEED_RENEW_MEMBERSHIP": "You are no longer a member because your membership <b>has expired</b>. Remember to <a ng-click=\"doQuickFix('renew')\">renew your membership</a>.",
    "NO_WAITING_MEMBERSHIP": "No membership application pending. If you'd like to <b>become a member</ b>, please <a ng-click=\"doQuickFix('membership')\">send the membership application</a>.",
    "CERTIFICATION_COUNT": "Received certifications",
    "CERTIFICATION_COUNT_SHORT": "Certifications",
    "SIG_STOCK": "Stock of certifications to give",
    "BTN_RECEIVE_MONEY": "Receive",
    "BTN_SELECT_ALTERNATIVES_IDENTITIES": "Switch to another identity...",
    "BTN_MEMBERSHIP_RENEW": "Renew membership",
    "BTN_MEMBERSHIP_RENEW_DOTS": "Renew membership...",
    "BTN_MEMBERSHIP_OUT_DOTS": "Revoke membership...",
    "BTN_SECURITY_DOTS": "Sign-in and security...",
    "BTN_SHOW_DETAILS": "Display technical data",
    "LOCKED_OUTPUTS_POPOVER": {
      "TITLE": "Locked amount",
      "DESCRIPTION": "Here are the conditions for unlocking this amount:",
      "DESCRIPTION_MANY": "This transaction consists of several parts, of which the unlock conditions are:",
      "LOCKED_AMOUNT": "Conditions for the amount:"
    },
    "NEW": {
      "TITLE": "Registration",
      "INTRO_WARNING_TIME": "Creating an account on {{name|capitalize}} is very simple. Please take sufficient time to do this correctly (not to forget the usernames, passwords, etc.).",
      "INTRO_WARNING_SECURITY": "Check that the hardware you are currently using (computer, tablet, phone) <b>is secure and trustworthy </b>.",
      "INTRO_WARNING_SECURITY_HELP": "Up-to-date anti-virus, firewall enabled, session protected by password or pin code...",
      "INTRO_HELP": "Click <b> {{'COMMON.BTN_START'|translate}}</b> to begin creating an account. You will be guided step by step.",
      "REGISTRATION_NODE": "Your registration will be registered via the Duniter peer <b>{{server}}</b> node, which will then be distributed to the rest of the currency network.",
      "REGISTRATION_NODE_HELP": "If you do not trust this peer, please change <a ng-click=\"doQuickFix('settings')\">in the settings</a> of Cesium.",
      "SELECT_ACCOUNT_TYPE": "Choose the type of account to create:",
      "MEMBER_ACCOUNT": "Member account",
      "MEMBER_ACCOUNT_TITLE": "Create a member account",
      "MEMBER_ACCOUNT_HELP": "If you are not yet registered as an individual (one account possible per individual).",
      "WALLET_ACCOUNT": "Simple wallet",
      "WALLET_ACCOUNT_TITLE": "Create a wallet",
      "WALLET_ACCOUNT_HELP": "If you represent a company, association, etc. or simply need an additional wallet. No universal dividend will be created by this account.",
      "SALT_WARNING": "Choose a secret identifier.<br/>You need it for each connection to this account.<br/><br/><b>Make sure to remember this identifier</b>.<br/>If lost, there are no means to retrieve it!",
      "PASSWORD_WARNING": "Choose a password.<br/>You need it for each connection to this account.<br/><br/><b>Make sure to remember this password</b>.<br/>If lost, there are no means to retrieve it!",
      "PSEUDO_WARNING": "Choose a pseudonym.<br/>It may be used by other people to find you more easily.<br/><br/>.Use of <b>commas, spaces and accents</b> is not allowed.<br/><div class='hidden-xs'><br/>Example: <span class='gray'>JohnDalton, JackieChan, etc.</span>",
      "PSEUDO": "Pseudonym",
      "PSEUDO_HELP": "joe123",
      "SALT_CONFIRM": "Confirm",
      "SALT_CONFIRM_HELP": "Confirm the secret identifier",
      "PASSWORD_CONFIRM": "Confirm",
      "PASSWORD_CONFIRM_HELP": "Confirm the password",
      "SLIDE_6_TITLE": "Confirmation:",
      "COMPUTING_PUBKEY": "Computing...",
      "LAST_SLIDE_CONGRATULATION": "You completed all required fields.<br/><b>You can send the account creation request</b>.<br/><br/>For information, the public key below identifies your future account.<br/>It can be communicated to third parties to receive their payment.<br/>Once your account has been approved, you can find this key under <b>{{'ACCOUNT.TITLE'|translate}}</b>.",
      "CONFIRMATION_MEMBER_ACCOUNT": "<b class=\"assertive\">Warning:</b> your secret identifier, password and pseudonym can not be changed.<br/><b>Make sure you always remember it!</b><br/><b>Are you sure</b> you want to send this account creation request?",
      "CONFIRMATION_WALLET_ACCOUNT": "<b class=\"assertive\">Warning:</b> your password and pseudonym can not be changed.<br/><b>Make sure you always remember it!</b><br/><b>Are you sure</b> you want to continue?",
      "CHECKING_PSEUDO": "Checking...",
      "PSEUDO_AVAILABLE": "This pseudonym is available",
      "PSEUDO_NOT_AVAILABLE": "This pseudonym is not available",
      "INFO_LICENSE": "To be able to adhere to the currency, we ask you to kindly read and accept this license.",
      "BTN_ACCEPT": "I accept",
      "BTN_ACCEPT_LICENSE": "I accept the license"
    },
    "POPUP_REGISTER": {
      "TITLE": "Enter a pseudonym",
      "HELP": "A pseudonym is needed to let other members find you."
    },
    "SELECT_IDENTITY_MODAL": {
      "TITLE": "Identity selection",
      "HELP": "Several <b>different identities</b> have been sent, for the public key <span class=\"gray\"> <i class=\"ion-key\"></i> {{pubkey | formatPubkey}}</span>.<br/>Please select the identity to use:"
    },
    "SECURITY":{
      "ADD_QUESTION" : "Add custom question",
      "BTN_CLEAN" : "Clean",
      "BTN_RESET" : "Reset",
      "DOWNLOAD_REVOKE": "Save a revocation file",
      "DOWNLOAD_REVOKE_HELP" : "Having a revocation file is important, for example in case of loss of identifiers. It allows you to <b>get this account out of the Web Of Trust</b>, thus becoming a simple wallet.",
      "MEMBERSHIP_IN": "Register as member...",
      "MEMBERSHIP_IN_HELP": "Allows you to <b>transform </b> a simple wallet account <b>into a member account</b>, by sending a membership request. Useful only if you do not already have another member account.",
      "SEND_IDENTITY": "Publish identity...",
      "SEND_IDENTITY_HELP": "Allows you to associate a pseudonym to this account, but <b>without applying for membership</b> to become a member. This is not very useful because the validity of this pseudonym association is limited in time.",
      "HELP_LEVEL": "Choose <strong> at least {{nb}} questions </strong> :",
      "LEVEL": "Security level",
      "LOW_LEVEL": "Low <span class=\"hidden-xs\">(2 questions minimum)</span>",
      "MEDIUM_LEVEL": "Medium <span class=\"hidden-xs\">(4 questions minimum)</span>",
      "QUESTION_1": "What was your best friend's name when you were a teen ?",
      "QUESTION_2": "What was the name of your first pet ?",
      "QUESTION_3": "What is the first meal you have learned to cook ?",
      "QUESTION_4": "What is the first movie you saw in the cinema?",
      "QUESTION_5": "Where did you go the first time you flew ?",
      "QUESTION_6": "What was your favorite elementary school teacher's name  ?",
      "QUESTION_7": "What would you consider the ideal job ?",
      "QUESTION_8": "Which children's book do you prefer?",
      "QUESTION_9": "What was the model of your first vehicle?",
      "QUESTION_10": "What was your nickname when you were a child ?",
      "QUESTION_11": "What was your favorite movie character or actor when you were a student ?",
      "QUESTION_12": "What was your favorite singer or band when you were a student ?",
      "QUESTION_13": "In which city did your parents meet ?",
      "QUESTION_14": "What was the name of your first boss ?",
      "QUESTION_15": "What is the name of the street where you grew up ?",
      "QUESTION_16": "What is the name of the first beach where you go swim ?",
      "QUESTION_17": "QWhat is the first album you bought ?",
      "QUESTION_18": "What is the name of your favorite sport team ?",
      "QUESTION_19": "What was your grand-father's job ?",
      "RECOVER_ID": "Recover my password...",
      "RECOVER_ID_HELP": "If you have a <b>backup file of your identifiers</b>, you can find them by answering your personal questions correctly.",
      "REVOCATION_WITH_FILE" : "Rekoke my member account...",
      "REVOCATION_WITH_FILE_DESCRIPTION": "If you have <b>permanently lost your member account credentials (or if account security is compromised), you can use <b>the revocation file</b> of the account <b>to quit the Web Of Trust</b>.",
      "REVOCATION_WITH_FILE_HELP": "To <b>permanently revoke</ b> a member account, please drag the revocation file in the box below, or click in the box to search for a file.",
      "REVOCATION_WALLET": "Revoke this account immediately",
      "REVOCATION_WALLET_HELP": "Requesting revocation of your identity causes <b>will revoke your membership</ b> (definitely for the associated pseudonym and public key). The account will no longer be able to produce a Universal Dividend.<br/>However, you can still use it as a simple wallet.",
      "REVOCATION_FILENAME": "revocation-{{uid}}-{{pubkey|formatPubkey}}-{{currency}}.txt",
      "SAVE_ID": "Save my credentials...",
      "SAVE_ID_HELP": "Creating a backup file, to <b>retrieve your password</b> (and the secret identifier) <b> in case of forgetting</b>. The file is <b>secured</ b> (encrypted) using personal questions.",
      "STRONG_LEVEL": "Strong <span class=\"hidden-xs \">(6 questions minimum)</span>",
      "TITLE": "Account and security"
    },
    "FILE_NAME": "{{currency}} - Account statement {{pubkey|formatPubkey}} to {{currentTime|formatDateForFile}}.csv",
    "HEADERS": {
      "TIME": "Date",
      "AMOUNT": "Amount",
      "COMMENT": "Comment"
    }
  },
  "TRANSFER": {
    "TITLE": "Transfer",
    "SUB_TITLE": "Transfer money",
    "FROM": "From",
    "TO": "To",
    "AMOUNT": "Amount",
    "AMOUNT_HELP": "Amount",
    "COMMENT": "Comment",
    "COMMENT_HELP": "Comment (optional)",
    "BTN_SEND": "Send",
    "BTN_ADD_COMMENT": "Add a comment",
    "WARN_COMMENT_IS_PUBLIC": "Please note that <b>comments are public</b> (not encrypted).",
    "MODAL": {
      "TITLE": "Transfer"
    }
  },
  "ERROR": {
    "POPUP_TITLE": "Error",
    "UNKNOWN_ERROR": "Unknown error",
    "CRYPTO_UNKNOWN_ERROR": "Your browser is not compatible with cryptographic features.",
    "EQUALS_TO_PSEUDO": "Must be different from pseudonym",
    "EQUALS_TO_SALT": "Must be different from secret identifier",
    "FIELD_REQUIRED": "This field is required.",
    "FIELD_TOO_SHORT": "This field value is too short.",
    "FIELD_TOO_SHORT_WITH_LENGTH": "Value is too short (min {{minLength]] characters).",
    "FIELD_TOO_LONG": "Value is exceeding max length.",
    "FIELD_TOO_LONG_WITH_LENGTH": "Value is too long (max {{maxLength}} characters).",
    "FIELD_MIN": "Minimum value: {{min}}",
    "FIELD_MAX": "Maximal value: {{max}}",
    "FIELD_ACCENT": "Commas and accent characters not allowed",
    "FIELD_NOT_NUMBER": "Value is not a number",
    "FIELD_NOT_INT": "Value is not an integer",
    "FIELD_NOT_EMAIL": "Email adress not valid",
    "PASSWORD_NOT_CONFIRMED": "Must match previous password.",
    "SALT_NOT_CONFIRMED": "Must match previous identifier.",
    "SEND_IDENTITY_FAILED": "Error while trying to register.",
    "SEND_CERTIFICATION_FAILED": "Could not certify identity.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY": "You could not send certification, because your account is <b>not a member account</b>.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY_HAS_SELF": "You could not send certification now, because your are <b>not a member</b> yet.<br/><br/>You still need certification to become a member.",
    "NOT_MEMBER_FOR_CERTIFICATION": "Your account is not a member account yet.",
    "IDENTITY_TO_CERTIFY_HAS_NO_SELF": "This account could not be certified. No registration found, or need to renew.",
    "LOGIN_FAILED": "Error while sign in.",
    "LOAD_IDENTITY_FAILED": "Could not load identity.",
    "LOAD_REQUIREMENTS_FAILED": "Could not load identity requirements.",
    "SEND_MEMBERSHIP_IN_FAILED": "Error while sending registration as member.",
    "SEND_MEMBERSHIP_OUT_FAILED": "Error while sending membership revocation.",
    "REFRESH_WALLET_DATA": "Could not refresh wallet.",
    "GET_CURRENCY_PARAMETER": "Could not get currency parameters.",
    "GET_CURRENCY_FAILED": "Could not load currency. Please retry later.",
    "SEND_TX_FAILED": "Could not send transaction.",
    "ALL_SOURCES_USED": "Please wait the next block computation (All transaction sources has been used).",
    "NOT_ENOUGH_SOURCES": "Not enough changes to send this amount in one time.<br/>Maximum amount: {{amount}} {{unit}}<sub>{{subUnit}}</sub>.",
    "ACCOUNT_CREATION_FAILED": "Error while creating your member account.",
    "RESTORE_WALLET_DATA_ERROR": "Error while reloading settings from local storage",
    "LOAD_WALLET_DATA_ERROR": "Error while loading wallet data.",
    "COPY_CLIPBOARD_FAILED": "Could not copy to clipboard",
    "TAKE_PICTURE_FAILED": "Could not get picture.",
    "SCAN_FAILED": "Could not scan QR code.",
    "SCAN_UNKNOWN_FORMAT": "Code not recognized.",
    "WOT_LOOKUP_FAILED": "Search failed.",
    "LOAD_PEER_DATA_FAILED": "Duniter peer not accessible. Please retry later.",
    "NEED_LOGIN_FIRST": "Please sign in first.",
    "AMOUNT_REQUIRED": "Amount is required.",
    "AMOUNT_NEGATIVE": "Negative amount not allowed.",
    "NOT_ENOUGH_CREDIT": "Not enough credit.",
    "INVALID_NODE_SUMMARY": "Unreachable peer or invalid address",
    "INVALID_USER_ID": "Field 'pseudonym' must not contains spaces or special characters.",
    "INVALID_COMMENT": "Field 'reference' has a bad format.",
    "INVALID_PUBKEY": "Public key has a bad format.",
    "IDENTITY_REVOKED": "This identity <b>has been revoked {{revocationTime|formatFromNow}}</b> ({{revocationTime|formatDate}}). It can no longer become a member.",
    "IDENTITY_PENDING_REVOCATION": "The <b>revocation of this identity</b> has been requested and is awaiting processing. Certification is therefore disabled.",
    "IDENTITY_INVALID_BLOCK_HASH": "This membership application is no longer valid (because it references a block that network peers are cancelled): the person must renew its application for membership <b>before</b> being certified.",
    "IDENTITY_EXPIRED": "This identity has expired: this person must re-apply <b>before</b> being certified.",
    "IDENTITY_SANDBOX_FULL": "Could not register, because peer's sandbox is full.<br/><br/>Please retry later or choose another Duniter peer (in <b>Settings</b>).",
    "IDENTITY_NOT_FOUND": "Identity not found",
    "IDENTITY_TX_FAILED": "Error while getting identity's transactions",
    "WOT_PENDING_INVALID_BLOCK_HASH": "Membership not valid.",
    "WALLET_INVALID_BLOCK_HASH": "Your membership application is no longer valid (because it references a block that network peers are cancelled).<br/>You must <a ng-click=\"doQuickFix('renew')\">renew your application for membership</a> to fix this issue.",
    "WALLET_IDENTITY_EXPIRED": "The publication of your identity <b>has expired</b>.<br/>You must <a ng-click=\"doQuickFix('fixIdentity')\">re-issue your identity</a> to resolve this issue.",
    "WALLET_REVOKED": "Your identity has been <b>revoked</b>: neither your pseudonym nor your public key will be used in the future for a member account.",
    "WALLET_HAS_NO_SELF": "Your identity must first have been published, and not expired.",
    "AUTH_REQUIRED": "Authentication required.",
    "AUTH_INVALID_PUBKEY": "The public key does not match the connected account.",
    "AUTH_INVALID_SCRYPT": "Invalid username or password.",
    "AUTH_INVALID_FILE": "Invalid keychain file.",
    "AUTH_FILE_ERROR": "Failed to open keychain file",
    "IDENTITY_ALREADY_CERTIFY": "You have <b>already certified</b> that identity.<br/><br/>Your certificate is still valid (expires {{expiresIn|formatDuration}}).",
    "IDENTITY_ALREADY_CERTIFY_PENDING": "You have <b>already certified</b> that identity.<br/><br/>Your certification is still pending (Deadline for treatment {{expiresIn|formatDuration}}).",
    "UNABLE_TO_CERTIFY_TITLE": "Unable to certify",
    "LOAD_NEWCOMERS_FAILED": "Unable to load new members.",
    "LOAD_PENDING_FAILED": "Unable to load pending registrations.",
    "ONLY_MEMBER_CAN_EXECUTE_THIS_ACTION": "You must <b>be a member</b> in order to perform this action.",
    "ONLY_SELF_CAN_EXECUTE_THIS_ACTION": "You must have <b>published your identity</b> in order to perform this action.",
    "GET_BLOCK_FAILED": "Error while getting block",
    "INVALID_BLOCK_HASH": "Block not found (incorrect hash)",
    "DOWNLOAD_REVOCATION_FAILED": "Error while downloading revocation file.",
    "REVOCATION_FAILED": "Error while trying to revoke the identity.",
    "SALT_OR_PASSWORD_NOT_CONFIRMED": "Wrong secret identifier or password ",
    "RECOVER_ID_FAILED": "Could not recover password",
    "LOAD_FILE_FAILED" : "Unable to load file",
    "NOT_VALID_REVOCATION_FILE": "Invalid revocation file (wrong file format)",
    "NOT_VALID_SAVE_ID_FILE": "Invalid credentials backup file (wrong file format)",
    "NOT_VALID_KEY_FILE": "Invalid keychain file (unrecognized format)",
    "EXISTING_ACCOUNT": "Your identifiers correspond to an already existing account, whose <a ng-click=\"showHelpModal('pubkey')\">public key</a> is:",
    "EXISTING_ACCOUNT_REQUEST": "Please modify your credentials so that they correspond to an unused account.",
    "GET_LICENSE_FILE_FAILED": "Unable to get license file",
    "CHECK_NETWORK_CONNECTION": "No peer appears to be accessible.<br/><br/>Please <b>check your Internet connection</b>.",
    "ISSUE_524_TX_FAILED": "Failed to transfer.<br/><br/>A message has been sent to developers to help solve the problem.<b>Thank you for your help</b>."
  },
  "INFO": {
    "POPUP_TITLE": "Information",
    "CERTIFICATION_DONE": "Identity successfully signed",
    "NOT_ENOUGH_CREDIT": "Not enough credit",
    "TRANSFER_SENT": "Transfer request successfully sent",
    "COPY_TO_CLIPBOARD_DONE": "Copy succeeded",
    "MEMBERSHIP_OUT_SENT": "Membership revocation sent",
    "NOT_NEED_MEMBERSHIP": "Already a member.",
    "IDENTITY_WILL_MISSING_CERTIFICATIONS": "This identity will soon lack certification (at least {{willNeedCertificationCount}}).",
    "IDENTITY_NEED_MEMBERSHIP": "This identity did not send a membership request. She will have to if she wishes to become a member.",
    "REVOCATION_SENT": "Revocation sent successfully",
    "REVOCATION_SENT_WAITING_PROCESS": "Revocation <b>has been sent successfully</b>. It is awaiting processing.",
    "FEATURES_NOT_IMPLEMENTED": "This features is not implemented yet.<br/><br/>Why not to contribute to get it faster? ;)",
    "EMPTY_TX_HISTORY": "No operations to export"
  },
  "CONFIRM": {
    "POPUP_TITLE": "<b>Confirmation</b>",
    "POPUP_WARNING_TITLE": "<b>Warning</b>",
    "POPUP_SECURITY_WARNING_TITLE": "<i class=\"icon ion-alert-circled\"></i> <b>Security warning</b>",
    "CERTIFY_RULES_TITLE_UID": "Certify {{uid}}",
    "CERTIFY_RULES": "<b class=\"assertive\">Don't certify an account</b> if you believe that: <ul><li>1.) the issuers identity might be faked.<li>2.) the issuer already has another certified account.<li>3.) the issuer purposely or carelessly violates rule 1 or 2 (he certifies faked or double accounts).</ul></small><br/>Are you sure you want to certify this identity?",
    "TRANSFER": "<b>Transfer summary:</b><br/><br/><ul><li> - From: <b>{{from}}</b></li><li> - To: <b>{{to}}</b></li><li> - Amount: <b>{{amount}} {{unit}}</b></li><li> - Comment: <i>{{comment}}</i></li></ul><br/><b>Are-you sure you want to do this transfer?</b>",
    "MEMBERSHIP_OUT": "This operation is <b>irreversible</b>.<br/></br/><b>Are you sure you want to terminate your membership?</b>",
    "MEMBERSHIP_OUT_2": "This operation is <b>irreversible</b>!<br/><br/>Are you sure you want to <b>terminate your membership</b>?",
    "LOGIN_UNUSED_WALLET_TITLE": "Typing error?",
    "LOGIN_UNUSED_WALLET": "The account seems to be <b>inactive</b>.<br/><br/>It's probably a <b>typing error</b> when sign in. Please try again, checking that <b>public key is yours<b/>.",
    "FIX_IDENTITY": "The pseudonym <b>{{uid}}</b> will be published again, replacing the old publication that has expired.<br/></br/><b>Are you sure</b> you want to continue?",
    "FIX_MEMBERSHIP": "Your application for membership will be sent.<br/></br/><b>Are you sure?</b>",
    "MEMBERSHIP": "Your membership request will be sent. <br/></br/><b>Are you sure?</b>",
    "RENEW_MEMBERSHIP": "Your membership will be renewed.<br/></br/><b>Are you sure?</b>",
    "REVOKE_IDENTITY": "You will <b>definitely revoke this identity</b>.<br/><br/>The public key and the associated pseudonym <b>will never be used again</b> (for a member account).<br/></br/><b>Are you sure</b> you want to revoke this identity?",
    "REVOKE_IDENTITY_2": "This operation is <b>irreversible</b>!<br/><br/>Are you sure you want to <b>revoke this identity</b>?",
    "NOT_NEED_RENEW_MEMBERSHIP": "Your membership does not need to be renewed (it will only expire in {{membershipExpiresIn|formatDuration}}).<br/></br/><b>Are you sure you</b> want to renew your membership?",
    "SAVE_BEFORE_LEAVE": "Do you want to <b>save your changes</b> before leaving the page?",
    "SAVE_BEFORE_LEAVE_TITLE": "Changes not saved",
    "LOGOUT": "Are you sure you want to logout?",
    "USE_FALLBACK_NODE": "Peer <b>{{old}}</b> unreachable or invalid address.<br/><br/>Do you want to temporarily use the <b>{{new}}</b> node?",
    "ISSUE_524_SEND_LOG": "The transaction was rejected because of a known problem (issue #524) but not reproduced.<br/><br/>To help developers correct this error, do you accept <b>the transmission of your logs</b> per message?<br/><small>(No confidential data is sent)</small>"
  },
  "DOWNLOAD": {
    "POPUP_TITLE": "<b>Revocation file</b>",
    "POPUP_REVOKE_MESSAGE": "To safeguard your account, please download the <b>account revocation document</b>. It will allow you to cancel your account (in case of account theft, ID, an incorrectly created account, etc.).<br/><br/><b>Please store it in a safe place.</b>"
  },
  "HELP": {
    "TITLE": "Online help",
    "JOIN": {
      "SECTION": "Join",
      "SALT": "The secret identifier is very important. It is used to hash you password, which in turn is used to calculate your <span class=\"text-italic\">public account key</span> (its number) and the private key to access it.<br/><b>Please remeber this identifier well</b>, because there is no way to recover it when lost.<br/>Furthermore, it cannot be changed without having to create a new account.<br/><br/>A good secret identifier must be sufficiently long (8 characters at the very least) and as original as possible.",
      "PASSWORD": "The password is very important. Together with the secret identifier, it is use to calculate your account number (pblic key) and the private key to access it.<br/><b>Please remember it well</b>, because there is no way to recover it when lost.<br/>Furthermore, it cannot be changed without having to create a new account.<br/><br/>A good password is made (ideally) of at least 8 characters, with at least one capital and one number.",
      "PSEUDO": "A pseudonym is used only when joining as <span class=\"text-italic\">member</span>. It is always associated with a wallet (by its <span class=\"text-italic\">public key</span>).<br/>It is published on the network so that other users may identify it, certify or send money to the account.<br/>A pseudonym must be unique among all members (current and past)."
    },
    "LOGIN": {
      "SECTION": "Log in",
      "PUBKEY": "Account public key",
      "PUBKEY_DEF": "The public key of the keychain is generated from the entered identifiers (any), but does not correspond to an account already used.<br/><b>Make sure your public key is the same as your account</b>. Otherwise, you will be logged into an account that is probably never used, as the risk of collision with an existing account is very small.<br/><a href=\"https://en.wikipedia.org/wiki/Elliptic_curve_cryptography\" target=\"_ system\">Learn more about cryptography</a> by public key.",
      "METHOD": "Connection methods",
      "METHOD_DEF": "Several options are available to connect to a portfolios: <br/> - The connection <b>with salt (simple or advanced)</b> mixes your password with the secret identifier, to limit the attempts of piracy<br/> - The connection <b>using public key</b> prevents you from entering your credentials, which you will be asked only when an operation need it.<br/> - The connection <b>using keychain file</b> will read the public and private keys of the account from a file without the need to enter credentials. Several file formats are possible."
    },
    "GLOSSARY": {
      "SECTION": "Glossary",
      "PUBKEY_DEF": "A public key always identifies a wallet. It may identify a member. In Cesium it is calculated using the secret identifier and the password.",
      "MEMBER": "Member",
      "MEMBER_DEF": "A member is a real and living human, wishing to participate freely to the monitary community. The member will receive universal dividend, according to the period and amount as defined in the <span class=\"text-italic\">currency parameters</span>.",
      "CURRENCY_RULES": "Currency rules",
      "CURRENCY_RULES_DEF": "The currency rules are defined only once, and for all. They set the parameters under which the currency will perform: universal dividend calculation, the amount of certifications needed to become a member, the maximum amount of certifications a member can send, etc.<br/><br/>The parameters cannot be modified because of the use of a <span class=\"text-italic\">Blockchain</span> which carries and executes these rules, and constantly verifies their correct application. <a href=\"#/app/currency\">See current parameters</a>.",
      "BLOCKCHAIN": "Blockchain",
      "BLOCKCHAIN_DEF": "The Blockchain is a decentralised system which, in case of Duniter, serves to carry and execute the <span class=\"text-italic\">currency rules</span>.<br/><a href=\"http://en.duniter.org/presentation/\" target=\"_blank\">Read more about Duniter</a> and the working of its blockchain.",
      "UNIVERSAL_DIVIDEND_DEF": "The Universal Dividend (UD) is the quantity of money co-created by each member, according to the period and the calculation defined in the <span class=\"text-italic\">currency rules</span>.<br/>Every term, the members receive an equal amount of new money on their account.<br/><br/>The UD undergoes a steady growth, to remain fair under its members (current and future), calculated by an average life expectancy, as demonstrated in the Relative Theory of Money (RTM).<br/><a href=\"http://trm.creationmonetaire.info\" target=\"_system\">Read more about RTM</a> and open money."
    },
    "TIP": {
      "MENU_BTN_CURRENCY": "Menu <b>{{'MENU.CURRENCY'|translate}}</b> allows discovery of <b>currency parameters</b> and its state.",
      "CURRENCY_WOT": "The <b>member count</b> shows the <b>community's weight and evolution</b>.",
      "CURRENCY_MASS": "Shown here is the <b>total amount</b> currently in circulation and its <b>average distribution</b> per member.<br/><br/>This allows to estimate the <b>worth of any amount</b>, in respect to what <b>others own</b> on their account (on average).",
      "CURRENCY_UNIT_RELATIVE": "The unit used here (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) signifies that the amounts in {{currency|capitalize}} have been devided by the <b>Universal Dividend</b> (UD).<br/><br/><small>This relative unit is <b>relevant</b> because it is stable in contrast to the permanently growing monitary mass.</small>",
      "CURRENCY_CHANGE_UNIT": "The option <b>{{'COMMON.BTN_RELATIVE_UNIT'|translate}}</b> allows to <b>switch the unit</b> to show amounts in <b>{{currency|capitalize}}</b>, undevided by the Universal Dividend (instead of in &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;).",
      "CURRENCY_CHANGE_UNIT_TO_RELATIVE": "The option <b>{{'COMMON.BTN_RELATIVE_UNIT'|translate}}</b> allows to <b>switch the unit</b> to show amounts in &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;, which is relative to the Universal Dividend (the amount co-produced by each member).",
      "CURRENCY_RULES": "The <b>rules</b> of the currency determine its <b>exact and predictible</b> performance.<br/><br/>As a true DNA of the currency these rules make the monetary code <b>transparent and understandable</b>.",
      "MENU_BTN_NETWORK": "Menu <b>{{'MENU.NETWORK'|translate}}</b> allows discovery of <b>network's state<b>.",
      "NETWORK_BLOCKCHAIN": "All monetary transactions are recoded in a <b>public and tamper proof</b> ledger, generally referred to as the <b>blockchain</b>.",
      "NETWORK_PEERS": "The <b>peers</b> shown here correspond to <b>computers that update and check</b> the blockchain.<br/><br/>The more active peers there are, the more <b>decentralised</b> and therefore trustworhty the currency becomes.",
      "NETWORK_PEERS_BLOCK_NUMBER": "This <b>number</b> (in green) indicates the peer's <b>latest validated block</b> (last page written in the ledger).<br/><br/>Green indicates that the block was equally validated by the <b>majority of other peers</b>.",
      "NETWORK_PEERS_PARTICIPATE": "<b>Each member</b>, equiped with a computer with Internet, <b>can participate, adding a peer</b> simply by <b>installing the Duniter software</b> (free/libre). <a target=\"_new\" href=\"{{installDocUrl}}\" target=\"_system\">Read the installation manual &gt;&gt;</a>.",
      "MENU_BTN_ACCOUNT": "<b>{{'ACCOUNT.TITLE'|translate}}</b> allows access to your account balance and transaction history.",
      "MENU_BTN_ACCOUNT_MEMBER": "Here you can consult your account status, transaction history and your certifications.",
      "WALLET_CERTIFICATIONS": "Click here to reveiw the details of your certifications (given and received).",
      "WALLET_RECEIVED_CERTIFICATIONS": "Click here to review the details of your <b>received certifications</b>.",
      "WALLET_GIVEN_CERTIFICATIONS": "Click here to review the details of your <b>given certifications</b>.",
      "WALLET_BALANCE": "Your account <b>balance</b> is shown here.",
      "WALLET_BALANCE_RELATIVE": "{{'HELP.TIP.WALLET_BALANCE'|translate}}<br/><br/>The used unit (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) signifies that the amount in {{currency|capitalize}} has been divided by the <b>Universal Dividend</b> (UD) co-created by each member.<br/>At this moment, 1 UD equals {{currentUD}} {{currency|capitalize}}.",
      "WALLET_BALANCE_CHANGE_UNIT": "You can <b>change the unit</b> in which amounts are shown in <b><i class=\"icon ion-android-settings\"></i>&nbsp;{{'MENU.SETTINGS'|translate}}</b>.<br/><br/>For example, to display amounts <b>directly in {{currency|capitalize}}</b> instead of relative amounts.",
      "WALLET_PUBKEY": "This is your account public key. You can communicate it to a third party so that it more easily identifies your account.",
      "WALLET_SEND": "Issue a payment in just a few clicks.",
      "WALLET_SEND_NO_MONEY": "Issue a payment in just a few clicks.<br/>(Your balance does not allow this yet)",
      "WALLET_OPTIONS": "Please note that this button allows access to <b>other, less used actions</b>.<br/><br/>Don't forget to take a quick look, when you have a moment!",
      "WALLET_RECEIVED_CERTS": "This shows the list of persons that certified you.",
      "WALLET_CERTIFY": "The button <b>{{'WOT.BTN_SELECT_AND_CERTIFY'|translate}}</b> allows selecting an identity and certifying it.<br/><br/>Only users that are <b>already member</b> may certify others.",
      "WALLET_CERT_STOCK": "Your supply of certifications (to send) is limited to <b>{{sigStock}} certifications</b>.<br/><br/>This supply will replete itself over time, as and when earlier certifications expire.",
      "MENU_BTN_TX_MEMBER": "<b>{{'MENU.TRANSACTIONS'|translate}}</b> allow access to transactions history, and send new payments.",
      "MENU_BTN_TX": "View the history of <b>your transactions</b> here and send new payments.",
      "MENU_BTN_WOT": "The menu <b>{{'MENU.WOT'|translate}}</b> allows searching <b>users</b> of the currency (member or not).",
      "WOT_SEARCH_TEXT_XS": "To search in the registry, type the <b>first letters of a users pseudonym or public key</b>.<br/><br/>The search will start automatically.",
      "WOT_SEARCH_TEXT": "To search in the registry, type the <b>first letters of a users pseudonym or public key</b>.<br/><br/>Then hit <b>Enter</b> to start the search.",
      "WOT_SEARCH_RESULT": "Simply click a user row to view the details sheet.",
      "WOT_VIEW_CERTIFICATIONS": "The row <b>{{'ACCOUNT.CERTIFICATION_COUNT'|translate}}</b> shows how many members members validated this identity.<br/><br/>These certifications testify that the account belongs to <b>a living human</b> and this person has <b>no other member account</b>.",
      "WOT_VIEW_CERTIFICATIONS_COUNT": "There are at least <b>{{sigQty}} certifications</b> needed to become a member and receive the <b>Universal Dividend</b>.",
      "WOT_VIEW_CERTIFICATIONS_CLICK": "Click here to open <b>a list of all certifications</b> given to and by this identity.",
      "WOT_VIEW_CERTIFY": "The button <b>{{'WOT.BTN_CERTIFY'|translate}}</b> allows to add your certification to this identity.",
      "CERTIFY_RULES": "<b>Attention:</b> Only certify <b>real and living persons</b> that do not own any other certified account.<br/><br/>The trust carried by the currency depends on each member's vigilance!",
      "MENU_BTN_SETTINGS": "The <b>{{'MENU.SETTINGS'|translate}}</b> allow you to configure the Cesium application.<br/><br/>For example, you can <b>change the unit</b> in which the currency will be shown.",
      "HEADER_BAR_BTN_PROFILE": "Click here to access your <b>user profile</b>",
      "SETTINGS_CHANGE_UNIT": "You can <b>change the display unit</b> of amounts by clicking here.<br/><br/>- Deactivate the option to show amounts in {{currency|capitalize}}.<br/>- Activate the option for relative amounts in {{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub> (<b>divided</b> by the current Universal Dividend).",
      "END_LOGIN": "This guided visit has <b>ended</b>.<br/><br/>Welcome to the <b>free economy</b>!",
      "END_NOT_LOGIN": "This guided visit has <b>ended</b>.<br/><br/>If you wish to join the currency {{currency|capitalize}}, simply click <b>{{'LOGIN.CREATE_ACCOUNT'|translate}}</b> below."
    }
  },
  "API" :{
    "COMMON": {
      "LINK_DOC": "API documentation",
      "LINK_DOC_HELP": "API documentation for developers",
      "LINK_STANDARD_APP": "Standard version",
      "LINK_STANDARD_APP_HELP": "Open standard version of {{'COMMON.APP_NAME'|translate}}"
    },
    "HOME": {
      "TITLE": "{{'COMMON.APP_NAME'|translate}} API Documentation",
      "MESSAGE": "Welcome to the {{'COMMON.APP_NAME'|translate}} <b>API documentation </b>.<br/>Connect your web site to <a href=\"http://duniter.org\" target=\"_system\">Duniter</a> very easily!",
      "MESSAGE_SHORT": "Connect your websites to <a href=\"http://duniter.org\" target=\"_system\">Duniter</a> very easily!",
      "DOC_HEADER": "Available services:"
    },
    "TRANSFER": {
      "TITLE": "{{'COMMON.APP_NAME'|translate}} - Online payment",
      "TITLE_SHORT": "Online payment",
      "SUMMARY": "Order summary:",
      "AMOUNT": "Amount:",
      "NAME": "Name:",
      "PUBKEY": "Public key of the recipient:",
      "COMMENT": "Order reference:",
      "DEMO": {
        "SALT": "demo",
        "PASSWORD": "demo",
        "PUBKEY": "3G28bL6deXQBYpPBpLFuECo46d3kfYMJwst7uhdVBnD1",
        "HELP": "<b>Demonstration mode</b>: No payment will actually be sent during this simulation.<br/>Please use credentials: <b>{{'API.TRANSFER.DEMO.SALT'|translate}} / {{'API.TRANSFER.DEMO.PASSWORD'|translate}}</b>",
        "BAD_CREDENTIALS": "Invalid credentials.<br/>In demonstration mode, credentials should be: {{'API.TRANSFER.DEMO.SALT'|translate}} / {{'API.TRANSFER.DEMO.PASSWORD'|translate}}"
      },
      "INFO": {
        "SUCCESS_REDIRECTING_WITH_NAME": "Payment sent.<br/>Redirect to <b>{{name}}</b>...",
        "SUCCESS_REDIRECTING": "Payment sent.<br/>Redirect to the seller's website...",
        "CANCEL_REDIRECTING_WITH_NAME": "Payment cancelled.<br/>Redirect to <b>{{name}}</b>...",
        "CANCEL_REDIRECTING": "Payment cancelled.<br/>Redirect to the seller's website..."
      },
      "ERROR": {
        "TRANSFER_FAILED": "Payment failed"
      }
    },
    "DOC": {
      "DESCRIPTION_DIVIDER": "Description",
      "URL_DIVIDER": "Calling address",
      "PARAMETERS_DIVIDER": "Parameters",
      "AVAILABLE_PARAMETERS": "Here is the list of al available parameters:",
      "DEMO_DIVIDER": "Try it !",
      "DEMO_HELP": "To test this service, click on this button. The result content will be display below.",
      "DEMO_RESULT": "Result returned by call:",
      "DEMO_SUCCEED": "<i class=\"icon ion-checkmark\"></i> Success!",
      "DEMO_CANCELLED": "<i class=\"icon ion-close\"></i> Canceled by user",
      "INTEGRATE_DIVIDER": "Website integration",
      "INTEGRATE_CODE": "Code:",
      "INTEGRATE_RESULT": "Result preview:",
      "INTEGRATE_PARAMETERS": "Parameters",
      "TRANSFER": {
        "TITLE": "Payments",
        "DESCRIPTION": "From a site (eg online marketplace) you can delegate payment in free currency to Cesium API. To do this, simply open a page at the following address:",
        "PARAM_PUBKEY": "Recipient's public key",
        "PARAM_PUBKEY_HELP": "Recipient's public key (required)",
        "PARAM_AMOUNT": "Amount",
        "PARAM_AMOUNT_HELP": "Transaction amount (required)",
        "PARAM_COMMENT": "Reference (or comment)",
        "PARAM_COMMENT_HELP": "Reference or comment. You will allow for example to identify the payment in the BlockChain.",
        "PARAM_NAME": "Name (of recipient or website)",
        "PARAM_NAME_HELP": "The name of your website. This can be a readable name (eg \"My online site\"), or a web address (eg \"www.MySite.com\").",
        "PARAM_REDIRECT_URL": "URL redirection",
        "PARAM_REDIRECT_URL_HELP": "URL redirection after sending payment, after the payment has been sent. Can contain the following strings, which will be replaced by the values of the transaction: \"{tx}\", \"{hash}\", \"{comment}\", \"{amount}\" and \"{pubkey}\".",
        "PARAM_CANCEL_URL": "URL if cancelled",
        "PARAM_CANCEL_URL_HELP": "URL in case of cancellation.  Can contain the following strings, which will be replaced: \"{comment}\", \"{amount}\" and \"{pubkey}\".",
        "EXAMPLES_HELP": "Examples of integration:",
        "EXAMPLE_BUTTON": "HTML Button",
        "EXAMPLE_BUTTON_DEFAULT_TEXT": "Pay in {{currency|abbreviate}}",
        "EXAMPLE_BUTTON_DEFAULT_STYLE": "Custom style",
        "EXAMPLE_BUTTON_TEXT_HELP": "Button text",
        "EXAMPLE_BUTTON_BG_COLOR": "Background color",
        "EXAMPLE_BUTTON_BG_COLOR_HELP": "eg: #fbc14c, yellow, lightgrey, rgb(180,180,180)",
        "EXAMPLE_BUTTON_FONT_COLOR": "Font color",
        "EXAMPLE_BUTTON_FONT_COLOR_HELP": "eg: black, orange, rgb(180,180,180)",
        "EXAMPLE_BUTTON_TEXT_ICON": "Icon",
        "EXAMPLE_BUTTON_TEXT_WIDTH": "Width",
        "EXAMPLE_BUTTON_TEXT_WIDTH_HELP": "eg: 200px, 50%",
        "EXAMPLE_BUTTON_ICON_NONE": "No icon",
        "EXAMPLE_BUTTON_ICON_DUNITER": "Duniter logo",
        "EXAMPLE_BUTTON_ICON_CESIUM": "Cesium logo",
        "EXAMPLE_BUTTON_ICON_G1_COLOR": "Ğ1 logo",
        "EXAMPLE_BUTTON_ICON_G1_BLACK": "Ğ1 logo (outline)"
      }
    }
  }
}
);

$translateProvider.translations("en", {
  "COMMON": {
    "APP_NAME": "Cesium",
    "APP_VERSION": "v{{version}}",
    "APP_BUILD": "build {{build}}",
    "PUBKEY": "Public key",
    "MEMBER": "Member",
    "BLOCK" : "Block",
    "BTN_OK": "OK",
    "BTN_YES": "Yes",
    "BTN_NO": "No",
    "BTN_SEND": "Send",
    "BTN_SEND_MONEY": "Transfer money",
    "BTN_SEND_MONEY_SHORT": "Transfer",
    "BTN_SAVE": "Save",
    "BTN_YES_SAVE": "Yes, Save",
    "BTN_YES_CONTINUE": "Yes, Continue",
    "BTN_SHOW": "Show",
    "BTN_SHOW_PUBKEY": "Show key",
    "BTN_RELATIVE_UNIT": "Use relative unit",
    "BTN_BACK": "Back",
    "BTN_NEXT": "Next",
    "BTN_CANCEL": "Cancel",
    "BTN_CLOSE": "Close",
    "BTN_LATER": "Later",
    "BTN_LOGIN": "Sign In",
    "BTN_LOGOUT": "Logout",
    "BTN_ADD_ACCOUNT": "New Account",
    "BTN_SHARE": "Share",
    "BTN_EDIT": "Edit",
    "BTN_DELETE": "Delete",
    "BTN_ADD": "Add",
    "BTN_SEARCH": "Search",
    "BTN_REFRESH": "Refresh",
    "BTN_RETRY": "Retry",
    "BTN_START": "Start",
    "BTN_CONTINUE": "Continue",
    "BTN_CREATE": "Create",
    "BTN_UNDERSTOOD": "I understood",
    "BTN_OPTIONS": "Options",
    "BTN_HELP_TOUR": "Features tour",
    "BTN_HELP_TOUR_SCREEN": "Discover this screen",
    "BTN_DOWNLOAD": "Download",
    "BTN_DOWNLOAD_ACCOUNT_STATEMENT": "Download account statement",
    "BTN_MODIFY": "Modify",
    "CHOOSE_FILE": "Drag your file<br/>or click to select",
    "DAYS": "days",
    "NO_ACCOUNT_QUESTION": "Not a member yet? Register now!",
    "SEARCH_NO_RESULT": "No result found",
    "LOADING": "Loading...",
    "SEARCHING": "Searching...",
    "FROM": "From",
    "TO": "To",
    "COPY": "Copy",
    "LANGUAGE": "Language",
    "UNIVERSAL_DIVIDEND": "Universal dividend",
    "UD": "UD",
    "DATE_PATTERN": "MM/DD/YYYY HH:mm",
    "DATE_FILE_PATTERN": "YYYY-MM-DD",
    "DATE_SHORT_PATTERN": "MM/DD/YY",
    "DATE_MONTH_YEAR_PATTERN": "MM/YYYY",
    "EMPTY_PARENTHESIS": "(empty)",
    "UID": "Pseudonym",
    "ENABLE": "Enabled",
    "DISABLE": "Disabled",
    "RESULTS_LIST": "Results:",
    "RESULTS_COUNT": "{{count}} results",
    "EXECUTION_TIME": "executed in {{duration|formatDurationMs}}",
    "SHOW_VALUES": "Display values openly?",
    "POPOVER_ACTIONS_TITLE": "Options",
    "POPOVER_FILTER_TITLE": "Filters",
    "SHOW_MORE": "Show more",
    "SHOW_MORE_COUNT": "(current limit at {{limit}})",
    "POPOVER_SHARE": {
      "TITLE": "Share",
      "SHARE_ON_TWITTER": "Share on Twitter",
      "SHARE_ON_FACEBOOK": "Share on Facebook",
      "SHARE_ON_DIASPORA": "Share on Diaspora*",
      "SHARE_ON_GOOGLEPLUS": "Share on Google+"
    }
  },
  "SYSTEM": {
    "PICTURE_CHOOSE_TYPE": "Choose source:",
    "BTN_PICTURE_GALLERY": "Gallery",
    "BTN_PICTURE_CAMERA": "<b>Camera</b>"
  },
  "MENU": {
    "HOME": "Home",
    "WOT": "Registry",
    "CURRENCY": "Currency",
    "ACCOUNT": "My Account",
    "TRANSFER": "Transfer",
    "SCAN": "Scan",
    "SETTINGS": "Settings",
    "NETWORK": "Network",
    "TRANSACTIONS": "My transactions"
  },
  "ABOUT": {
    "TITLE": "About",
    "LICENSE": "<b>Free/libre software</b> (License GNU GPLv3).",
    "LATEST_RELEASE": "There is a <b>newer version</ b> of {{'COMMON.APP_NAME' | translate}} (<b>v{{version}}</b>)",
    "PLEASE_UPDATE": "Please update {{'COMMON.APP_NAME' | translate}} (latest version: <b>v{{version}}</b>)",
    "CODE": "Source code:",
    "DEVELOPERS": "Developers:",
    "FORUM": "Forum:",
    "PLEASE_REPORT_ISSUE": "Please report any issue to us!",
    "REPORT_ISSUE": "Report an issue"
  },
  "HOME": {
    "TITLE": "Cesium",
    "WELCOME": "Welcome to the Cesium Application!",
    "MESSAGE": "Follow your {{currency|abbreviate}} wallets easily",
    "BTN_CURRENCY": "Explore currency",
    "BTN_ABOUT": "about",
    "BTN_HELP": "Help",
    "REPORT_ISSUE": "Report an issue",
    "NOT_YOUR_ACCOUNT_QUESTION" : "You do not own the account <b><i class=\"ion-key\"></i> {{pubkey|formatPubkey}}</b>?",
    "BTN_CHANGE_ACCOUNT": "Disconnect this account",
    "CONNECTION_ERROR": "Peer <b>{{server}}</b> unreachable or invalid address.<br/><br/>Check your Internet connection, or change node <a class=\"positive\" ng-click=\"doQuickFix('settings')\">in the settings</a>."
  },
  "SETTINGS": {
    "TITLE": "Settings",
    "NETWORK_SETTINGS": "Network",
    "PEER": "Duniter peer address",
    "PEER_CHANGED_TEMPORARY": "Address used temporarily",
    "USE_LOCAL_STORAGE": "Enable local storage",
    "USE_LOCAL_STORAGE_HELP": "Allows you to save your settings",
    "ENABLE_HELPTIP": "Enable contextual help tips",
    "ENABLE_UI_EFFECTS": "Enable visual effects",
    "HISTORY_SETTINGS": "Account operations",
    "DISPLAY_UD_HISTORY": "Display produced dividends?",
    "AUTHENTICATION_SETTINGS": "Authentication",
    "KEEP_AUTH": "Expiration of authentication",
    "KEEP_AUTH_HELP": "Define when authentication is cleared from memory",
    "KEEP_AUTH_OPTION": {
      "NEVER": "After each operation",
      "SECONDS": "After {{value}}s of inactivity",
      "MINUTE": "After {{value}}min of inactivity",
      "MINUTES": "After {{value}}min of inactivity",
      "HOUR": "After {{value}}h of inactivity",
      "ALWAYS": "At the end of the session"
    },
    "REMEMBER_ME": "Remember me ?",
    "REMEMBER_ME_HELP": "Allows to remain identified from one session to another, keeping the public key locally.",
    "PLUGINS_SETTINGS": "Extensions",
    "BTN_RESET": "Restore default values",
    "EXPERT_MODE": "Enable expert mode",
    "EXPERT_MODE_HELP": "Allow to see more details",
    "POPUP_PEER": {
      "TITLE" : "Duniter peer",
      "HOST" : "Address",
      "HOST_HELP": "Address: server:port",
      "USE_SSL" : "Secured?",
      "USE_SSL_HELP" : "(SSL Encryption)",
      "BTN_SHOW_LIST" : "Peer's list"
    }
  },
  "BLOCKCHAIN": {
    "HASH": "Hash: {{hash}}",
    "VIEW": {
      "HEADER_TITLE": "Block #{{number}}-{{hash|formatHash}}",
      "TITLE_CURRENT": "Current block",
      "TITLE": "Block #{{number|formatInteger}}",
      "COMPUTED_BY": "Computed by",
      "SHOW_RAW": "Show raw data",
      "TECHNICAL_DIVIDER": "Technical informations",
      "VERSION": "Format version",
      "HASH": "Computed hash",
      "UNIVERSAL_DIVIDEND_HELP": "Money co-produced by each of the {{membersCount}} members",
      "EMPTY": "Aucune donnée dans ce bloc",
      "POW_MIN": "Minimal difficulty",
      "POW_MIN_HELP": "Difficulty imposed in calculating hash",
      "DATA_DIVIDER": "Data",
      "IDENTITIES_COUNT": "New identities",
      "JOINERS_COUNT": "Joiners",
      "ACTIVES_COUNT": "Renewals",
      "ACTIVES_COUNT_HELP": "Members having renewed their membership",
      "LEAVERS_COUNT": "Leavers",
      "LEAVERS_COUNT_HELP": "Members that now refused certification",
      "EXCLUDED_COUNT": "Excluded members",
      "EXCLUDED_COUNT_HELP": "Old members, excluded because missing membreship renewal or certifications",
      "REVOKED_COUNT": "Revoked identities",
      "REVOKED_COUNT_HELP": "These accounts may no longer be member",
      "TX_COUNT": "Transactions",
      "CERT_COUNT": "Certifications",
      "TX_TO_HIMSELF": "Change",
      "TX_OUTPUT_UNLOCK_CONDITIONS": "Unlock conditions",
      "TX_OUTPUT_OPERATOR": {
        "AND": "and",
        "OR": "or"
      },
      "TX_OUTPUT_FUNCTION": {
        "SIG": "<b>Sign</b> of the public key",
        "XHX": "<b>Password</b>, including SHA256 =",
        "CSV": "Blocked during",
        "CLTV": "Bloqué until"
      }
    },
    "LOOKUP": {
      "TITLE": "Blocks",
      "NO_BLOCK": "No bloc",
      "LAST_BLOCKS": "Last blocks:",
      "BTN_COMPACT": "Compact"
    }
  },
  "CURRENCY": {
    "VIEW": {
      "TITLE": "Currency",
      "TAB_CURRENCY": "Currency",
      "TAB_WOT": "Web of trust",
      "TAB_NETWORK": "Network",
      "TAB_BLOCKS": "Blocks",
      "CURRENCY_SHORT_DESCRIPTION": "{{currency|capitalize}} is a <b>libre money</b>, started {{firstBlockTime | formatFromNow}}. It currently counts <b>{{N}} members </b>, who produce and collect a <a ng-click=\"showHelpModal('ud')\">Universal Dividend</a> (DU), each {{dt | formatPeriod}}.",
      "NETWORK_RULES_DIVIDER": "Network rules",
      "CURRENCY_NAME": "Currency name",
      "MEMBERS": "Members count",
      "MEMBERS_VARIATION": "Variation since {{duration|formatDuration}} (since last UD)",
      "MONEY_DIVIDER": "Money",
      "MASS": "Monetary mass",
      "SHARE": "Money share",
      "UD": "Universal Dividend",
      "C_ACTUAL": "Current growth",
      "MEDIAN_TIME": "Current blockchain time",
      "POW_MIN": "Common difficulty",
      "MONEY_RULES_DIVIDER": "Rules of currency",
      "C_RULE": "Theoretical growth target",
      "UD_RULE": "Universal dividend (formula)",
      "DT_REEVAL": "Period between two re-evaluation of the UD",
      "REEVAL_SYMBOL": "reeval",
      "DT_REEVAL_VALUE": "Every <b>{{dtReeval|formatDuration}}</b> ({{dtReeval/86400}} {{'COMMON.DAYS'|translate}})",
      "UD_REEVAL_TIME0": "Date of first reevaluation of the UD",
      "SIG_QTY_RULE": "Required number of certifications to become a member",
      "SIG_STOCK": "Maximum number of certifications sent by a member",
      "SIG_PERIOD": "Minimum delay between 2 certifications sent by one and the same issuer.",
      "SIG_WINDOW": "Maximum delay before a certification will be treated",
      "SIG_VALIDITY": "Lifetime of a certification that has been treated",
      "MS_WINDOW": "Maximum delay before a pending membership will be treated",
      "MS_VALIDITY": "Lifetime of a membership that has been treated",
      "STEP_MAX": "Maximum distance between a newcomer and each referring members.",
      "WOT_RULES_DIVIDER": "Rules for web of trust",
      "SENTRIES": "Required number of certifications (given <b>and</b> received) to become a referring member",
      "SENTRIES_FORMULA": "Required number of certifications to become a referring member (formula)",
      "XPERCENT":"Minimum percent of referring member to reach to match the distance rule",
      "AVG_GEN_TIME": "The average time between 2 blocks",
      "CURRENT": "current",
      "MATH_CEILING": "CEILING",
      "DISPLAY_ALL_RULES": "Display all rules?",
      "BTN_SHOW_LICENSE": "Show license",
      "WOT_DIVIDER": "Web of trust"
    },
    "LICENSE": {
      "TITLE": "Currency license",
      "BTN_DOWNLOAD": "Download file",
      "NO_LICENSE_FILE": "License file not found."
    }
  },
  "NETWORK": {
    "VIEW": {
      "MEDIAN_TIME": "Blockchain time",
      "LOADING_PEERS": "Loading peers...",
      "NODE_ADDRESS": "Address:",
      "SOFTWARE": "Software:",
      "WARN_PRE_RELEASE": "Pre-release (latest stable: <b>{{version}}</b>)",
      "WARN_NEW_RELEASE": "Version <b>{{version}}</b> available",
      "WS2PID": "Identifier:",
      "PRIVATE_ACCESS": "Private access",
      "POW_PREFIX": "Proof of work prefix:",
      "ENDPOINTS": {
        "BMAS": "Secure endpoint (SSL)",
        "BMATOR": "TOR endpoint",
        "WS2P": "WS2P endpoint",
        "ES_USER_API": "Cesium+ data node"
      }
    },
    "INFO": {
      "ONLY_SSL_PEERS": "Non-SSL nodes have a degraded display because Cesium works in HTTPS mode."
    }
  },
  "PEER": {
    "PEERS": "Peers",
    "SIGNED_ON_BLOCK": "Signed on block",
    "MIRROR": "mirror",
    "MIRRORS": "Mirror peers",
    "PEER_LIST" : "Peer's list",
    "MEMBERS" : "Member peers",
    "ALL_PEERS" : "All peers",
    "DIFFICULTY" : "Difficulty",
    "API" : "API",
    "CURRENT_BLOCK" : "Block #",
    "POPOVER_FILTER_TITLE": "Filter",
    "OFFLINE": "Offline peers",
    "BTN_SHOW_PEER": "Show peer",
    "VIEW": {
      "TITLE": "Peer",
      "OWNER": "Owned by ",
      "SHOW_RAW_PEERING": "See peering document",
      "SHOW_RAW_CURRENT_BLOCK": "See current block (raw format)",
      "LAST_BLOCKS": "Last blocks",
      "KNOWN_PEERS": "Known peers :",
      "GENERAL_DIVIDER": "General information",
      "ERROR": {
        "LOADING_TOR_NODE_ERROR": "Could not get peer data, using the TOR network.",
        "LOADING_NODE_ERROR": "Could not get peer data"
      }
    }
  },
  "WOT": {
    "SEARCH_HELP": "Search (member or public key)",
    "SEARCH_INIT_PHASE_WARNING": "During the pre-registration phase, the search for pending registrations <b>may be long</b>. Please wait ...",
    "REGISTERED_SINCE": "Registered on",
    "REGISTERED_SINCE_BLOCK": "Registered since block #",
    "NO_CERTIFICATION": "No validated certification",
    "NO_GIVEN_CERTIFICATION": "No given certification",
    "NOT_MEMBER_PARENTHESIS": "(non-member)",
    "IDENTITY_REVOKED_PARENTHESIS": "(identity revoked)",
    "MEMBER_PENDING_REVOCATION_PARENTHESIS": "(being revoked)",
    "EXPIRE_IN": "Expires",
    "NOT_WRITTEN_EXPIRE_IN": "Deadline<br/>treatment",
    "EXPIRED": "Expired",
    "PSEUDO": "Pseudonym",
    "SIGNED_ON_BLOCK": "Emitted on block #{{block}}",
    "WRITTEN_ON_BLOCK": "Written on block #{{block}}",
    "GENERAL_DIVIDER": "General information",
    "NOT_MEMBER_ACCOUNT": "Non-member account",
    "NOT_MEMBER_ACCOUNT_HELP": "This is a simple wallet, with no pending membership application.",
    "TECHNICAL_DIVIDER": "Technical data",
    "BTN_CERTIFY": "Certify",
    "BTN_YES_CERTIFY": "Yes, certify",
    "BTN_SELECT_AND_CERTIFY": "New certification",
    "ACCOUNT_OPERATIONS": "Account operations",
    "VIEW": {
      "POPOVER_SHARE_TITLE": "Identity {{title}}"
    },
    "LOOKUP": {
      "TITLE": "Registry",
      "NEWCOMERS": "New members:",
      "NEWCOMERS_COUNT": "{{count}} members",
      "PENDING": "Pending registrations:",
      "PENDING_COUNT": "{{count}} pending registrations",
      "REGISTERED": "Registered {{sigDate | formatFromNow}}",
      "MEMBER_FROM": "Member since {{memberDate|formatFromNowShort}}",
      "BTN_NEWCOMERS": "Latest members",
      "BTN_PENDING": "Pending registrations",
      "SHOW_MORE": "Show more",
      "SHOW_MORE_COUNT": "(current limit to {{limit}})",
      "NO_PENDING": "No pending registrations.",
      "NO_NEWCOMERS": "No members."
    },
    "CONTACTS": {
      "TITLE": "Contacts"
    },
    "MODAL": {
      "TITLE": "Search"
    },
    "CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certifications",
      "SUMMARY": "Received certifications",
      "LIST": "Details of received certifications",
      "PENDING_LIST": "Pending certifications",
      "RECEIVED": "Received certifications",
      "RECEIVED_BY": "Certifications received by {{uid}}",
      "ERROR": "Received certifications in error",
      "SENTRY_MEMBER": "Referring member"
    },
    "OPERATIONS": {
      "TITLE": "{{uid}} - Operations"
    },
    "GIVEN_CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certifications sent",
      "SUMMARY": "Sent certifications",
      "LIST": "Details of sent certifications",
      "PENDING_LIST": "Pending certifications",
      "SENT": "Sent certifications",
      "SENT_BY": "Certifications sent by {{uid}}",
      "ERROR": "Sent certifications with error"
    }
  },
  "LOGIN": {
    "TITLE": "<i class=\"icon ion-log-in\"></i> Login",
    "SCRYPT_FORM_HELP": "Please enter your credentials. <br> Remember to check the public key for your account.",
    "PUBKEY_FORM_HELP": "Please enter a public account key:",
    "FILE_FORM_HELP": "Choose the keychain file to use:",
    "SALT": "Secret identifier",
    "SALT_HELP": "Secret identifier",
    "SHOW_SALT": "Display secret identifier?",
    "PASSWORD": "Password",
    "PASSWORD_HELP": "Password",
    "PUBKEY_HELP": "Example: « AbsxSY4qoZRzyV2irfep1V9xw1EMNyKJw2TkuVD4N1mv »",
    "NO_ACCOUNT_QUESTION": "Don't have an account yet?",
    "CREATE_ACCOUNT": "Create an account",
    "FORGOTTEN_ID": "Forgot password?",
    "ASSOCIATED_PUBKEY": "Public key :",
    "BTN_METHODS": "Other methods",
    "BTN_METHODS_DOTS": "Change method...",
    "METHOD_POPOVER_TITLE": "Methods",
    "MEMORIZE_AUTH_FILE": "Memorize this keychain during the navigation session",
    "SCRYPT_PARAMETERS": "Paramètres (Scrypt) :",
    "AUTO_LOGOUT": {
      "TITLE": "Information",
      "MESSAGE": "<i class=\"ion-android-time\"></i> You were <b>logout</ b> automatically, due to prolonged inactivity.",
      "BTN_RELOGIN": "Sign In",
      "IDLE_WARNING": "You will be logout... {{countdown}}"
    },
    "METHOD": {
      "SCRYPT_DEFAULT": "Standard salt (default)",
      "SCRYPT_ADVANCED": "Advanced salt",
      "FILE": "Keychain file",
      "PUBKEY": "Public key only"
    },
    "SCRYPT": {
      "SIMPLE": "Light salt",
      "DEFAULT": "Standard salt",
      "SECURE": "Secure salt",
      "HARDEST": "Hardest salt",
      "EXTREME": "Extreme salt",
      "USER": "Personal value",
      "N": "N (Loop):",
      "r": "r (RAM):",
      "p": "p (CPU):"
    },
    "FILE": {
      "DATE" : "Date:",
      "TYPE" : "Type:",
      "SIZE": "Size:",
      "VALIDATING": "Validating...",
      "HELP": "Expected file format: <b>.dunikey</b> (type PubSec). Other formats are under development (EWIF, WIF)."
    }
  },
  "AUTH": {
    "TITLE": "<i class=\"icon ion-locked\"></i> Authentication",
    "METHOD_LABEL": "Authentication method",
    "BTN_AUTH": "Authenticate",
    "SCRYPT_FORM_HELP": "Please authenticate yourself:"
  },
  "ACCOUNT": {
    "TITLE": "My Account",
    "BALANCE": "Balance",
    "LAST_TX": "Latest transactions",
    "BALANCE_ACCOUNT": "Account balance",
    "NO_TX": "No transaction",
    "SHOW_MORE_TX": "Show more",
    "SHOW_ALL_TX": "Show all",
    "TX_FROM_DATE": "(current limit to {{fromTime|formatFromNowShort}})",
    "PENDING_TX": "Pending transactions",
    "ERROR_TX": "Transaction not executed",
    "ERROR_TX_SENT": "Sent transactions",
    "PENDING_TX_RECEIVED": "Transactions awaiting receipt",
    "EVENTS": "Events",
    "WAITING_MEMBERSHIP": "Membership application sent. Waiting validation.",
    "WAITING_CERTIFICATIONS": "You need {{needCertificationCount}} certification(s) to become a member",
    "WILL_MISSING_CERTIFICATIONS": "You will <b>lack certifications</b> soon (at least {{willNeedCertificationCount}} more are needed)",
    "WILL_NEED_RENEW_MEMBERSHIP": "Your membership <b>will expire {{membershipExpiresIn|formatDurationTo}}</b>. Remember to <a ng-click=\"doQuickFix('renew')\">renew your membership</a> before then.",
    "NEED_RENEW_MEMBERSHIP": "You are no longer a member because your membership <b>has expired</b>. Remember to <a ng-click=\"doQuickFix('renew')\">renew your membership</a>.",
    "NO_WAITING_MEMBERSHIP": "No membership application pending. If you'd like to <b>become a member</ b>, please <a ng-click=\"doQuickFix('membership')\">send the membership application</a>.",
    "CERTIFICATION_COUNT": "Received certifications",
    "CERTIFICATION_COUNT_SHORT": "Certifications",
    "SIG_STOCK": "Stock of certifications to give",
    "BTN_RECEIVE_MONEY": "Receive",
    "BTN_SELECT_ALTERNATIVES_IDENTITIES": "Switch to another identity...",
    "BTN_MEMBERSHIP_RENEW": "Renew membership",
    "BTN_MEMBERSHIP_RENEW_DOTS": "Renew membership...",
    "BTN_MEMBERSHIP_OUT_DOTS": "Revoke membership...",
    "BTN_SECURITY_DOTS": "Sign-in and security...",
    "BTN_SHOW_DETAILS": "Display technical data",
    "LOCKED_OUTPUTS_POPOVER": {
      "TITLE": "Locked amount",
      "DESCRIPTION": "Here are the conditions for unlocking this amount:",
      "DESCRIPTION_MANY": "This transaction consists of several parts, of which the unlock conditions are:",
      "LOCKED_AMOUNT": "Conditions for the amount:"
    },
    "NEW": {
      "TITLE": "Registration",
      "INTRO_WARNING_TIME": "Creating an account on {{name|capitalize}} is very simple. Please take sufficient time to do this correctly (not to forget the usernames, passwords, etc.).",
      "INTRO_WARNING_SECURITY": "Check that the hardware you are currently using (computer, tablet, phone) <b>is secure and trustworthy </b>.",
      "INTRO_WARNING_SECURITY_HELP": "Up-to-date anti-virus, firewall enabled, session protected by password or pin code...",
      "INTRO_HELP": "Click <b> {{'COMMON.BTN_START'|translate}}</b> to begin creating an account. You will be guided step by step.",
      "REGISTRATION_NODE": "Your registration will be registered via the Duniter peer <b>{{server}}</b> node, which will then be distributed to the rest of the currency network.",
      "REGISTRATION_NODE_HELP": "If you do not trust this peer, please change <a ng-click=\"doQuickFix('settings')\">in the settings</a> of Cesium.",
      "SELECT_ACCOUNT_TYPE": "Choose the type of account to create:",
      "MEMBER_ACCOUNT": "Member account",
      "MEMBER_ACCOUNT_TITLE": "Create a member account",
      "MEMBER_ACCOUNT_HELP": "If you are not yet registered as an individual (one account possible per individual).",
      "WALLET_ACCOUNT": "Simple wallet",
      "WALLET_ACCOUNT_TITLE": "Create a wallet",
      "WALLET_ACCOUNT_HELP": "If you represent a company, association, etc. or simply need an additional wallet. No universal dividend will be created by this account.",
      "SALT_WARNING": "Choose a secret identifier.<br/>You need it for each connection to this account.<br/><br/><b>Make sure to remember this identifier</b>.<br/>If lost, there are no means to retrieve it!",
      "PASSWORD_WARNING": "Choose a password.<br/>You need it for each connection to this account.<br/><br/><b>Make sure to remember this password</b>.<br/>If lost, there are no means to retrieve it!",
      "PSEUDO_WARNING": "Choose a pseudonym.<br/>It may be used by other people to find you more easily.<br/><br/>.Use of <b>commas, spaces and accents</b> is not allowed.<br/><div class='hidden-xs'><br/>Example: <span class='gray'>JohnDalton, JackieChan, etc.</span>",
      "PSEUDO": "Pseudonym",
      "PSEUDO_HELP": "joe123",
      "SALT_CONFIRM": "Confirm",
      "SALT_CONFIRM_HELP": "Confirm the secret identifier",
      "PASSWORD_CONFIRM": "Confirm",
      "PASSWORD_CONFIRM_HELP": "Confirm the password",
      "SLIDE_6_TITLE": "Confirmation:",
      "COMPUTING_PUBKEY": "Computing...",
      "LAST_SLIDE_CONGRATULATION": "You completed all required fields.<br/><b>You can send the account creation request</b>.<br/><br/>For information, the public key below identifies your future account.<br/>It can be communicated to third parties to receive their payment.<br/>Once your account has been approved, you can find this key under <b>{{'ACCOUNT.TITLE'|translate}}</b>.",
      "CONFIRMATION_MEMBER_ACCOUNT": "<b class=\"assertive\">Warning:</b> your secret identifier, password and pseudonym can not be changed.<br/><b>Make sure you always remember it!</b><br/><b>Are you sure</b> you want to send this account creation request?",
      "CONFIRMATION_WALLET_ACCOUNT": "<b class=\"assertive\">Warning:</b> your password and pseudonym can not be changed.<br/><b>Make sure you always remember it!</b><br/><b>Are you sure</b> you want to continue?",
      "CHECKING_PSEUDO": "Checking...",
      "PSEUDO_AVAILABLE": "This pseudonym is available",
      "PSEUDO_NOT_AVAILABLE": "This pseudonym is not available",
      "INFO_LICENSE": "To be able to adhere to the currency, we ask you to kindly read and accept this license.",
      "BTN_ACCEPT": "I accept",
      "BTN_ACCEPT_LICENSE": "I accept the license"
    },
    "POPUP_REGISTER": {
      "TITLE": "Enter a pseudonym",
      "HELP": "A pseudonym is needed to let other members find you."
    },
    "SELECT_IDENTITY_MODAL": {
      "TITLE": "Identity selection",
      "HELP": "Several <b>different identities</b> have been sent, for the public key <span class=\"gray\"> <i class=\"ion-key\"></i> {{pubkey | formatPubkey}}</span>.<br/>Please select the identity to use:"
    },
    "SECURITY":{
      "ADD_QUESTION" : "Add custom question",
      "BTN_CLEAN" : "Clean",
      "BTN_RESET" : "Reset",
      "DOWNLOAD_REVOKE": "Save a revocation file",
      "DOWNLOAD_REVOKE_HELP" : "Having a revocation file is important, for example in case of loss of identifiers. It allows you to <b>get this account out of the Web Of Trust</b>, thus becoming a simple wallet.",
      "MEMBERSHIP_IN": "Register as member...",
      "MEMBERSHIP_IN_HELP": "Allows you to <b>transform </b> a simple wallet account <b>into a member account</b>, by sending a membership request. Useful only if you do not already have another member account.",
      "SEND_IDENTITY": "Publish identity...",
      "SEND_IDENTITY_HELP": "Allows you to associate a pseudonym to this account, but <b>without applying for membership</b> to become a member. This is not very useful because the validity of this pseudonym association is limited in time.",
      "HELP_LEVEL": "Choose <strong> at least {{nb}} questions </strong> :",
      "LEVEL": "Security level",
      "LOW_LEVEL": "Low <span class=\"hidden-xs\">(2 questions minimum)</span>",
      "MEDIUM_LEVEL": "Medium <span class=\"hidden-xs\">(4 questions minimum)</span>",
      "QUESTION_1": "What was your best friend's name when you were a teen ?",
      "QUESTION_2": "What was the name of your first pet ?",
      "QUESTION_3": "What is the first meal you have learned to cook ?",
      "QUESTION_4": "What is the first movie you saw in the cinema?",
      "QUESTION_5": "Where did you go the first time you flew ?",
      "QUESTION_6": "What was your favorite elementary school teacher's name  ?",
      "QUESTION_7": "What would you consider the ideal job ?",
      "QUESTION_8": "Which children's book do you prefer?",
      "QUESTION_9": "What was the model of your first vehicle?",
      "QUESTION_10": "What was your nickname when you were a child ?",
      "QUESTION_11": "What was your favorite movie character or actor when you were a student ?",
      "QUESTION_12": "What was your favorite singer or band when you were a student ?",
      "QUESTION_13": "In which city did your parents meet ?",
      "QUESTION_14": "What was the name of your first boss ?",
      "QUESTION_15": "What is the name of the street where you grew up ?",
      "QUESTION_16": "What is the name of the first beach where you go swim ?",
      "QUESTION_17": "QWhat is the first album you bought ?",
      "QUESTION_18": "What is the name of your favorite sport team ?",
      "QUESTION_19": "What was your grand-father's job ?",
      "RECOVER_ID": "Recover my password...",
      "RECOVER_ID_HELP": "If you have a <b>backup file of your identifiers</b>, you can find them by answering your personal questions correctly.",
      "REVOCATION_WITH_FILE" : "Rekoke my member account...",
      "REVOCATION_WITH_FILE_DESCRIPTION": "If you have <b>permanently lost your member account credentials (or if account security is compromised), you can use <b>the revocation file</b> of the account <b>to quit the Web Of Trust</b>.",
      "REVOCATION_WITH_FILE_HELP": "To <b>permanently revoke</ b> a member account, please drag the revocation file in the box below, or click in the box to search for a file.",
      "REVOCATION_WALLET": "Revoke this account immediately",
      "REVOCATION_WALLET_HELP": "Requesting revocation of your identity causes <b>will revoke your membership</ b> (definitely for the associated pseudonym and public key). The account will no longer be able to produce a Universal Dividend.<br/>However, you can still use it as a simple wallet.",
      "REVOCATION_FILENAME": "revocation-{{uid}}-{{pubkey|formatPubkey}}-{{currency}}.txt",
      "SAVE_ID": "Save my credentials...",
      "SAVE_ID_HELP": "Creating a backup file, to <b>retrieve your password</b> (and the secret identifier) <b> in case of forgetting</b>. The file is <b>secured</ b> (encrypted) using personal questions.",
      "STRONG_LEVEL": "Strong <span class=\"hidden-xs \">(6 questions minimum)</span>",
      "TITLE": "Account and security"
    },
    "FILE_NAME": "{{currency}} - Account statement {{pubkey|formatPubkey}} to {{currentTime|formatDateForFile}}.csv",
    "HEADERS": {
      "TIME": "Date",
      "AMOUNT": "Amount",
      "COMMENT": "Comment"
    }
  },
  "TRANSFER": {
    "TITLE": "Transfer",
    "SUB_TITLE": "Transfer money",
    "FROM": "From",
    "TO": "To",
    "AMOUNT": "Amount",
    "AMOUNT_HELP": "Amount",
    "COMMENT": "Comment",
    "COMMENT_HELP": "Comment (optional)",
    "BTN_SEND": "Send",
    "BTN_ADD_COMMENT": "Add a comment",
    "WARN_COMMENT_IS_PUBLIC": "Please note that <b>comments are public</b> (not encrypted).",
    "MODAL": {
      "TITLE": "Transfer"
    }
  },
  "ERROR": {
    "POPUP_TITLE": "Error",
    "UNKNOWN_ERROR": "Unknown error",
    "CRYPTO_UNKNOWN_ERROR": "Your browser is not compatible with cryptographic features.",
    "EQUALS_TO_PSEUDO": "Must be different from pseudonym",
    "EQUALS_TO_SALT": "Must be different from secret identifier",
    "FIELD_REQUIRED": "This field is required.",
    "FIELD_TOO_SHORT": "This field value is too short.",
    "FIELD_TOO_SHORT_WITH_LENGTH": "Value is too short (min {{minLength]] characters).",
    "FIELD_TOO_LONG": "Value is exceeding max length.",
    "FIELD_TOO_LONG_WITH_LENGTH": "Value is too long (max {{maxLength}} characters).",
    "FIELD_MIN": "Minimum value: {{min}}",
    "FIELD_MAX": "Maximal value: {{max}}",
    "FIELD_ACCENT": "Commas and accent characters not allowed",
    "FIELD_NOT_NUMBER": "Value is not a number",
    "FIELD_NOT_INT": "Value is not an integer",
    "FIELD_NOT_EMAIL": "Email adress not valid",
    "PASSWORD_NOT_CONFIRMED": "Must match previous password.",
    "SALT_NOT_CONFIRMED": "Must match previous identifier.",
    "SEND_IDENTITY_FAILED": "Error while trying to register.",
    "SEND_CERTIFICATION_FAILED": "Could not certify identity.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY": "You could not send certification, because your account is <b>not a member account</b>.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY_HAS_SELF": "You could not send certification now, because your are <b>not a member</b> yet.<br/><br/>You still need certification to become a member.",
    "NOT_MEMBER_FOR_CERTIFICATION": "Your account is not a member account yet.",
    "IDENTITY_TO_CERTIFY_HAS_NO_SELF": "This account could not be certified. No registration found, or need to renew.",
    "LOGIN_FAILED": "Error while sign in.",
    "LOAD_IDENTITY_FAILED": "Could not load identity.",
    "LOAD_REQUIREMENTS_FAILED": "Could not load identity requirements.",
    "SEND_MEMBERSHIP_IN_FAILED": "Error while sending registration as member.",
    "SEND_MEMBERSHIP_OUT_FAILED": "Error while sending membership revocation.",
    "REFRESH_WALLET_DATA": "Could not refresh wallet.",
    "GET_CURRENCY_PARAMETER": "Could not get currency parameters.",
    "GET_CURRENCY_FAILED": "Could not load currency. Please retry later.",
    "SEND_TX_FAILED": "Could not send transaction.",
    "ALL_SOURCES_USED": "Please wait the next block computation (All transaction sources has been used).",
    "NOT_ENOUGH_SOURCES": "Not enough changes to send this amount in one time.<br/>Maximum amount: {{amount}} {{unit}}<sub>{{subUnit}}</sub>.",
    "ACCOUNT_CREATION_FAILED": "Error while creating your member account.",
    "RESTORE_WALLET_DATA_ERROR": "Error while reloading settings from local storage",
    "LOAD_WALLET_DATA_ERROR": "Error while loading wallet data.",
    "COPY_CLIPBOARD_FAILED": "Could not copy to clipboard",
    "TAKE_PICTURE_FAILED": "Could not get picture.",
    "SCAN_FAILED": "Could not scan QR code.",
    "SCAN_UNKNOWN_FORMAT": "Code not recognized.",
    "WOT_LOOKUP_FAILED": "Search failed.",
    "LOAD_PEER_DATA_FAILED": "Duniter peer not accessible. Please retry later.",
    "NEED_LOGIN_FIRST": "Please sign in first.",
    "AMOUNT_REQUIRED": "Amount is required.",
    "AMOUNT_NEGATIVE": "Negative amount not allowed.",
    "NOT_ENOUGH_CREDIT": "Not enough credit.",
    "INVALID_NODE_SUMMARY": "Unreachable peer or invalid address",
    "INVALID_USER_ID": "Field 'pseudonym' must not contains spaces or special characters.",
    "INVALID_COMMENT": "Field 'reference' has a bad format.",
    "INVALID_PUBKEY": "Public key has a bad format.",
    "IDENTITY_REVOKED": "This identity <b>has been revoked {{revocationTime|formatFromNow}}</b> ({{revocationTime|formatDate}}). It can no longer become a member.",
    "IDENTITY_PENDING_REVOCATION": "The <b>revocation of this identity</b> has been requested and is awaiting processing. Certification is therefore disabled.",
    "IDENTITY_INVALID_BLOCK_HASH": "This membership application is no longer valid (because it references a block that network peers are cancelled): the person must renew its application for membership <b>before</b> being certified.",
    "IDENTITY_EXPIRED": "This identity has expired: this person must re-apply <b>before</b> being certified.",
    "IDENTITY_SANDBOX_FULL": "Could not register, because peer's sandbox is full.<br/><br/>Please retry later or choose another Duniter peer (in <b>Settings</b>).",
    "IDENTITY_NOT_FOUND": "Identity not found",
    "IDENTITY_TX_FAILED": "Error while getting identity's transactions",
    "WOT_PENDING_INVALID_BLOCK_HASH": "Membership not valid.",
    "WALLET_INVALID_BLOCK_HASH": "Your membership application is no longer valid (because it references a block that network peers are cancelled).<br/>You must <a ng-click=\"doQuickFix('renew')\">renew your application for membership</a> to fix this issue.",
    "WALLET_IDENTITY_EXPIRED": "The publication of your identity <b>has expired</b>.<br/>You must <a ng-click=\"doQuickFix('fixIdentity')\">re-issue your identity</a> to resolve this issue.",
    "WALLET_REVOKED": "Your identity has been <b>revoked</b>: neither your pseudonym nor your public key will be used in the future for a member account.",
    "WALLET_HAS_NO_SELF": "Your identity must first have been published, and not expired.",
    "AUTH_REQUIRED": "Authentication required.",
    "AUTH_INVALID_PUBKEY": "The public key does not match the connected account.",
    "AUTH_INVALID_SCRYPT": "Invalid username or password.",
    "AUTH_INVALID_FILE": "Invalid keychain file.",
    "AUTH_FILE_ERROR": "Failed to open keychain file",
    "IDENTITY_ALREADY_CERTIFY": "You have <b>already certified</b> that identity.<br/><br/>Your certificate is still valid (expires {{expiresIn|formatDuration}}).",
    "IDENTITY_ALREADY_CERTIFY_PENDING": "You have <b>already certified</b> that identity.<br/><br/>Your certification is still pending (Deadline for treatment {{expiresIn|formatDuration}}).",
    "UNABLE_TO_CERTIFY_TITLE": "Unable to certify",
    "LOAD_NEWCOMERS_FAILED": "Unable to load new members.",
    "LOAD_PENDING_FAILED": "Unable to load pending registrations.",
    "ONLY_MEMBER_CAN_EXECUTE_THIS_ACTION": "You must <b>be a member</b> in order to perform this action.",
    "ONLY_SELF_CAN_EXECUTE_THIS_ACTION": "You must have <b>published your identity</b> in order to perform this action.",
    "GET_BLOCK_FAILED": "Error while getting block",
    "INVALID_BLOCK_HASH": "Block not found (incorrect hash)",
    "DOWNLOAD_REVOCATION_FAILED": "Error while downloading revocation file.",
    "REVOCATION_FAILED": "Error while trying to revoke the identity.",
    "SALT_OR_PASSWORD_NOT_CONFIRMED": "Wrong secret identifier or password ",
    "RECOVER_ID_FAILED": "Could not recover password",
    "LOAD_FILE_FAILED" : "Unable to load file",
    "NOT_VALID_REVOCATION_FILE": "Invalid revocation file (wrong file format)",
    "NOT_VALID_SAVE_ID_FILE": "Invalid credentials backup file (wrong file format)",
    "NOT_VALID_KEY_FILE": "Invalid keychain file (unrecognized format)",
    "EXISTING_ACCOUNT": "Your identifiers correspond to an already existing account, whose <a ng-click=\"showHelpModal('pubkey')\">public key</a> is:",
    "EXISTING_ACCOUNT_REQUEST": "Please modify your credentials so that they correspond to an unused account.",
    "GET_LICENSE_FILE_FAILED": "Unable to get license file",
    "CHECK_NETWORK_CONNECTION": "No peer appears to be accessible.<br/><br/>Please <b>check your Internet connection</b>.",
    "ISSUE_524_TX_FAILED": "Failed to transfer.<br/><br/>A message has been sent to developers to help solve the problem.<b>Thank you for your help</b>."
  },
  "INFO": {
    "POPUP_TITLE": "Information",
    "CERTIFICATION_DONE": "Identity successfully signed",
    "NOT_ENOUGH_CREDIT": "Not enough credit",
    "TRANSFER_SENT": "Transfer request successfully sent",
    "COPY_TO_CLIPBOARD_DONE": "Copy succeeded",
    "MEMBERSHIP_OUT_SENT": "Membership revocation sent",
    "NOT_NEED_MEMBERSHIP": "Already a member.",
    "IDENTITY_WILL_MISSING_CERTIFICATIONS": "This identity will soon lack certification (at least {{willNeedCertificationCount}}).",
    "IDENTITY_NEED_MEMBERSHIP": "This identity did not send a membership request. She will have to if she wishes to become a member.",
    "REVOCATION_SENT": "Revocation sent successfully",
    "REVOCATION_SENT_WAITING_PROCESS": "Revocation <b>has been sent successfully</b>. It is awaiting processing.",
    "FEATURES_NOT_IMPLEMENTED": "This features is not implemented yet.<br/><br/>Why not to contribute to get it faster? ;)",
    "EMPTY_TX_HISTORY": "No operations to export"
  },
  "CONFIRM": {
    "POPUP_TITLE": "<b>Confirmation</b>",
    "POPUP_WARNING_TITLE": "<b>Warning</b>",
    "POPUP_SECURITY_WARNING_TITLE": "<i class=\"icon ion-alert-circled\"></i> <b>Security warning</b>",
    "CERTIFY_RULES_TITLE_UID": "Certify {{uid}}",
    "CERTIFY_RULES": "<b class=\"assertive\">Don't certify an account</b> if you believe that: <ul><li>1.) the issuers identity might be faked.<li>2.) the issuer already has another certified account.<li>3.) the issuer purposely or carelessly violates rule 1 or 2 (he certifies faked or double accounts).</ul></small><br/>Are you sure you want to certify this identity?",
    "TRANSFER": "<b>Transfer summary:</b><br/><br/><ul><li> - From: <b>{{from}}</b></li><li> - To: <b>{{to}}</b></li><li> - Amount: <b>{{amount}} {{unit}}</b></li><li> - Comment: <i>{{comment}}</i></li></ul><br/><b>Are-you sure you want to do this transfer?</b>",
    "MEMBERSHIP_OUT": "This operation is <b>irreversible</b>.<br/></br/><b>Are you sure you want to terminate your membership?</b>",
    "MEMBERSHIP_OUT_2": "This operation is <b>irreversible</b>!<br/><br/>Are you sure you want to <b>terminate your membership</b>?",
    "LOGIN_UNUSED_WALLET_TITLE": "Typing error?",
    "LOGIN_UNUSED_WALLET": "The account seems to be <b>inactive</b>.<br/><br/>It's probably a <b>typing error</b> when sign in. Please try again, checking that <b>public key is yours<b/>.",
    "FIX_IDENTITY": "The pseudonym <b>{{uid}}</b> will be published again, replacing the old publication that has expired.<br/></br/><b>Are you sure</b> you want to continue?",
    "FIX_MEMBERSHIP": "Your application for membership will be sent.<br/></br/><b>Are you sure?</b>",
    "MEMBERSHIP": "Your membership request will be sent. <br/></br/><b>Are you sure?</b>",
    "RENEW_MEMBERSHIP": "Your membership will be renewed.<br/></br/><b>Are you sure?</b>",
    "REVOKE_IDENTITY": "You will <b>definitely revoke this identity</b>.<br/><br/>The public key and the associated pseudonym <b>will never be used again</b> (for a member account).<br/></br/><b>Are you sure</b> you want to revoke this identity?",
    "REVOKE_IDENTITY_2": "This operation is <b>irreversible</b>!<br/><br/>Are you sure you want to <b>revoke this identity</b>?",
    "NOT_NEED_RENEW_MEMBERSHIP": "Your membership does not need to be renewed (it will only expire in {{membershipExpiresIn|formatDuration}}).<br/></br/><b>Are you sure you</b> want to renew your membership?",
    "SAVE_BEFORE_LEAVE": "Do you want to <b>save your changes</b> before leaving the page?",
    "SAVE_BEFORE_LEAVE_TITLE": "Changes not saved",
    "LOGOUT": "Are you sure you want to logout?",
    "USE_FALLBACK_NODE": "Peer <b>{{old}}</b> unreachable or invalid address.<br/><br/>Do you want to temporarily use the <b>{{new}}</b> node?",
    "ISSUE_524_SEND_LOG": "The transaction was rejected because of a known problem (issue #524) but not reproduced.<br/><br/>To help developers correct this error, do you accept <b>the transmission of your logs</b> per message?<br/><small>(No confidential data is sent)</small>"
  },
  "DOWNLOAD": {
    "POPUP_TITLE": "<b>Revocation file</b>",
    "POPUP_REVOKE_MESSAGE": "To safeguard your account, please download the <b>account revocation document</b>. It will allow you to cancel your account (in case of account theft, ID, an incorrectly created account, etc.).<br/><br/><b>Please store it in a safe place.</b>"
  },
  "HELP": {
    "TITLE": "Online help",
    "JOIN": {
      "SECTION": "Join",
      "SALT": "The secret identifier is very important. It is used to hash you password, which in turn is used to calculate your <span class=\"text-italic\">public account key</span> (its number) and the private key to access it.<br/><b>Please remeber this identifier well</b>, because there is no way to recover it when lost.<br/>Furthermore, it cannot be changed without having to create a new account.<br/><br/>A good secret identifier must be sufficiently long (8 characters at the very least) and as original as possible.",
      "PASSWORD": "The password is very important. Together with the secret identifier, it is use to calculate your account number (pblic key) and the private key to access it.<br/><b>Please remember it well</b>, because there is no way to recover it when lost.<br/>Furthermore, it cannot be changed without having to create a new account.<br/><br/>A good password is made (ideally) of at least 8 characters, with at least one capital and one number.",
      "PSEUDO": "A pseudonym is used only when joining as <span class=\"text-italic\">member</span>. It is always associated with a wallet (by its <span class=\"text-italic\">public key</span>).<br/>It is published on the network so that other users may identify it, certify or send money to the account.<br/>A pseudonym must be unique among all members (current and past)."
    },
    "LOGIN": {
      "SECTION": "Log in",
      "PUBKEY": "Account public key",
      "PUBKEY_DEF": "The public key of the keychain is generated from the entered identifiers (any), but does not correspond to an account already used.<br/><b>Make sure your public key is the same as your account</b>. Otherwise, you will be logged into an account that is probably never used, as the risk of collision with an existing account is very small.<br/><a href=\"https://en.wikipedia.org/wiki/Elliptic_curve_cryptography\" target=\"_ system\">Learn more about cryptography</a> by public key.",
      "METHOD": "Connection methods",
      "METHOD_DEF": "Several options are available to connect to a portfolios: <br/> - The connection <b>with salt (simple or advanced)</b> mixes your password with the secret identifier, to limit the attempts of piracy<br/> - The connection <b>using public key</b> prevents you from entering your credentials, which you will be asked only when an operation need it.<br/> - The connection <b>using keychain file</b> will read the public and private keys of the account from a file without the need to enter credentials. Several file formats are possible."
    },
    "GLOSSARY": {
      "SECTION": "Glossary",
      "PUBKEY_DEF": "A public key always identifies a wallet. It may identify a member. In Cesium it is calculated using the secret identifier and the password.",
      "MEMBER": "Member",
      "MEMBER_DEF": "A member is a real and living human, wishing to participate freely to the monitary community. The member will receive universal dividend, according to the period and amount as defined in the <span class=\"text-italic\">currency parameters</span>.",
      "CURRENCY_RULES": "Currency rules",
      "CURRENCY_RULES_DEF": "The currency rules are defined only once, and for all. They set the parameters under which the currency will perform: universal dividend calculation, the amount of certifications needed to become a member, the maximum amount of certifications a member can send, etc.<br/><br/>The parameters cannot be modified because of the use of a <span class=\"text-italic\">Blockchain</span> which carries and executes these rules, and constantly verifies their correct application. <a href=\"#/app/currency\">See current parameters</a>.",
      "BLOCKCHAIN": "Blockchain",
      "BLOCKCHAIN_DEF": "The Blockchain is a decentralised system which, in case of Duniter, serves to carry and execute the <span class=\"text-italic\">currency rules</span>.<br/><a href=\"http://en.duniter.org/presentation/\" target=\"_blank\">Read more about Duniter</a> and the working of its blockchain.",
      "UNIVERSAL_DIVIDEND_DEF": "The Universal Dividend (UD) is the quantity of money co-created by each member, according to the period and the calculation defined in the <span class=\"text-italic\">currency rules</span>.<br/>Every term, the members receive an equal amount of new money on their account.<br/><br/>The UD undergoes a steady growth, to remain fair under its members (current and future), calculated by an average life expectancy, as demonstrated in the Relative Theory of Money (RTM).<br/><a href=\"http://trm.creationmonetaire.info\" target=\"_system\">Read more about RTM</a> and open money."
    },
    "TIP": {
      "MENU_BTN_CURRENCY": "Menu <b>{{'MENU.CURRENCY'|translate}}</b> allows discovery of <b>currency parameters</b> and its state.",
      "CURRENCY_WOT": "The <b>member count</b> shows the <b>community's weight and evolution</b>.",
      "CURRENCY_MASS": "Shown here is the <b>total amount</b> currently in circulation and its <b>average distribution</b> per member.<br/><br/>This allows to estimate the <b>worth of any amount</b>, in respect to what <b>others own</b> on their account (on average).",
      "CURRENCY_UNIT_RELATIVE": "The unit used here (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) signifies that the amounts in {{currency|capitalize}} have been devided by the <b>Universal Dividend</b> (UD).<br/><br/><small>This relative unit is <b>relevant</b> because it is stable in contrast to the permanently growing monitary mass.</small>",
      "CURRENCY_CHANGE_UNIT": "The option <b>{{'COMMON.BTN_RELATIVE_UNIT'|translate}}</b> allows to <b>switch the unit</b> to show amounts in <b>{{currency|capitalize}}</b>, undevided by the Universal Dividend (instead of in &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;).",
      "CURRENCY_CHANGE_UNIT_TO_RELATIVE": "The option <b>{{'COMMON.BTN_RELATIVE_UNIT'|translate}}</b> allows to <b>switch the unit</b> to show amounts in &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;, which is relative to the Universal Dividend (the amount co-produced by each member).",
      "CURRENCY_RULES": "The <b>rules</b> of the currency determine its <b>exact and predictible</b> performance.<br/><br/>As a true DNA of the currency these rules make the monetary code <b>transparent and understandable</b>.",
      "MENU_BTN_NETWORK": "Menu <b>{{'MENU.NETWORK'|translate}}</b> allows discovery of <b>network's state<b>.",
      "NETWORK_BLOCKCHAIN": "All monetary transactions are recoded in a <b>public and tamper proof</b> ledger, generally referred to as the <b>blockchain</b>.",
      "NETWORK_PEERS": "The <b>peers</b> shown here correspond to <b>computers that update and check</b> the blockchain.<br/><br/>The more active peers there are, the more <b>decentralised</b> and therefore trustworhty the currency becomes.",
      "NETWORK_PEERS_BLOCK_NUMBER": "This <b>number</b> (in green) indicates the peer's <b>latest validated block</b> (last page written in the ledger).<br/><br/>Green indicates that the block was equally validated by the <b>majority of other peers</b>.",
      "NETWORK_PEERS_PARTICIPATE": "<b>Each member</b>, equiped with a computer with Internet, <b>can participate, adding a peer</b> simply by <b>installing the Duniter software</b> (free/libre). <a target=\"_new\" href=\"{{installDocUrl}}\" target=\"_system\">Read the installation manual &gt;&gt;</a>.",
      "MENU_BTN_ACCOUNT": "<b>{{'ACCOUNT.TITLE'|translate}}</b> allows access to your account balance and transaction history.",
      "MENU_BTN_ACCOUNT_MEMBER": "Here you can consult your account status, transaction history and your certifications.",
      "WALLET_CERTIFICATIONS": "Click here to reveiw the details of your certifications (given and received).",
      "WALLET_RECEIVED_CERTIFICATIONS": "Click here to review the details of your <b>received certifications</b>.",
      "WALLET_GIVEN_CERTIFICATIONS": "Click here to review the details of your <b>given certifications</b>.",
      "WALLET_BALANCE": "Your account <b>balance</b> is shown here.",
      "WALLET_BALANCE_RELATIVE": "{{'HELP.TIP.WALLET_BALANCE'|translate}}<br/><br/>The used unit (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) signifies that the amount in {{currency|capitalize}} has been divided by the <b>Universal Dividend</b> (UD) co-created by each member.<br/>At this moment, 1 UD equals {{currentUD}} {{currency|capitalize}}.",
      "WALLET_BALANCE_CHANGE_UNIT": "You can <b>change the unit</b> in which amounts are shown in <b><i class=\"icon ion-android-settings\"></i>&nbsp;{{'MENU.SETTINGS'|translate}}</b>.<br/><br/>For example, to display amounts <b>directly in {{currency|capitalize}}</b> instead of relative amounts.",
      "WALLET_PUBKEY": "This is your account public key. You can communicate it to a third party so that it more easily identifies your account.",
      "WALLET_SEND": "Issue a payment in just a few clicks.",
      "WALLET_SEND_NO_MONEY": "Issue a payment in just a few clicks.<br/>(Your balance does not allow this yet)",
      "WALLET_OPTIONS": "Please note that this button allows access to <b>other, less used actions</b>.<br/><br/>Don't forget to take a quick look, when you have a moment!",
      "WALLET_RECEIVED_CERTS": "This shows the list of persons that certified you.",
      "WALLET_CERTIFY": "The button <b>{{'WOT.BTN_SELECT_AND_CERTIFY'|translate}}</b> allows selecting an identity and certifying it.<br/><br/>Only users that are <b>already member</b> may certify others.",
      "WALLET_CERT_STOCK": "Your supply of certifications (to send) is limited to <b>{{sigStock}} certifications</b>.<br/><br/>This supply will replete itself over time, as and when earlier certifications expire.",
      "MENU_BTN_TX_MEMBER": "<b>{{'MENU.TRANSACTIONS'|translate}}</b> allow access to transactions history, and send new payments.",
      "MENU_BTN_TX": "View the history of <b>your transactions</b> here and send new payments.",
      "MENU_BTN_WOT": "The menu <b>{{'MENU.WOT'|translate}}</b> allows searching <b>users</b> of the currency (member or not).",
      "WOT_SEARCH_TEXT_XS": "To search in the registry, type the <b>first letters of a users pseudonym or public key</b>.<br/><br/>The search will start automatically.",
      "WOT_SEARCH_TEXT": "To search in the registry, type the <b>first letters of a users pseudonym or public key</b>.<br/><br/>Then hit <b>Enter</b> to start the search.",
      "WOT_SEARCH_RESULT": "Simply click a user row to view the details sheet.",
      "WOT_VIEW_CERTIFICATIONS": "The row <b>{{'ACCOUNT.CERTIFICATION_COUNT'|translate}}</b> shows how many members members validated this identity.<br/><br/>These certifications testify that the account belongs to <b>a living human</b> and this person has <b>no other member account</b>.",
      "WOT_VIEW_CERTIFICATIONS_COUNT": "There are at least <b>{{sigQty}} certifications</b> needed to become a member and receive the <b>Universal Dividend</b>.",
      "WOT_VIEW_CERTIFICATIONS_CLICK": "Click here to open <b>a list of all certifications</b> given to and by this identity.",
      "WOT_VIEW_CERTIFY": "The button <b>{{'WOT.BTN_CERTIFY'|translate}}</b> allows to add your certification to this identity.",
      "CERTIFY_RULES": "<b>Attention:</b> Only certify <b>real and living persons</b> that do not own any other certified account.<br/><br/>The trust carried by the currency depends on each member's vigilance!",
      "MENU_BTN_SETTINGS": "The <b>{{'MENU.SETTINGS'|translate}}</b> allow you to configure the Cesium application.<br/><br/>For example, you can <b>change the unit</b> in which the currency will be shown.",
      "HEADER_BAR_BTN_PROFILE": "Click here to access your <b>user profile</b>",
      "SETTINGS_CHANGE_UNIT": "You can <b>change the display unit</b> of amounts by clicking here.<br/><br/>- Deactivate the option to show amounts in {{currency|capitalize}}.<br/>- Activate the option for relative amounts in {{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub> (<b>divided</b> by the current Universal Dividend).",
      "END_LOGIN": "This guided visit has <b>ended</b>.<br/><br/>Welcome to the <b>free economy</b>!",
      "END_NOT_LOGIN": "This guided visit has <b>ended</b>.<br/><br/>If you wish to join the currency {{currency|capitalize}}, simply click <b>{{'LOGIN.CREATE_ACCOUNT'|translate}}</b> below."
    }
  },
  "API" :{
    "COMMON": {
      "LINK_DOC": "API documentation",
      "LINK_DOC_HELP": "API documentation for developers",
      "LINK_STANDARD_APP": "Standard version",
      "LINK_STANDARD_APP_HELP": "Open standard version of {{'COMMON.APP_NAME'|translate}}"
    },
    "HOME": {
      "TITLE": "{{'COMMON.APP_NAME'|translate}} API Documentation",
      "MESSAGE": "Welcome to the {{'COMMON.APP_NAME'|translate}} <b>API documentation </b>.<br/>Connect your web site to <a href=\"http://duniter.org\" target=\"_system\">Duniter</a> very easily!",
      "MESSAGE_SHORT": "Connect your websites to <a href=\"http://duniter.org\" target=\"_system\">Duniter</a> very easily!",
      "DOC_HEADER": "Available services:"
    },
    "TRANSFER": {
      "TITLE": "{{'COMMON.APP_NAME'|translate}} - Online payment",
      "TITLE_SHORT": "Online payment",
      "SUMMARY": "Order summary:",
      "AMOUNT": "Amount:",
      "NAME": "Name:",
      "PUBKEY": "Public key of the recipient:",
      "COMMENT": "Order reference:",
      "DEMO": {
        "SALT": "demo",
        "PASSWORD": "demo",
        "PUBKEY": "3G28bL6deXQBYpPBpLFuECo46d3kfYMJwst7uhdVBnD1",
        "HELP": "<b>Demonstration mode</b>: No payment will actually be sent during this simulation.<br/>Please use credentials: <b>{{'API.TRANSFER.DEMO.SALT'|translate}} / {{'API.TRANSFER.DEMO.PASSWORD'|translate}}</b>",
        "BAD_CREDENTIALS": "Invalid credentials.<br/>In demonstration mode, credentials should be: {{'API.TRANSFER.DEMO.SALT'|translate}} / {{'API.TRANSFER.DEMO.PASSWORD'|translate}}"
      },
      "INFO": {
        "SUCCESS_REDIRECTING_WITH_NAME": "Payment sent.<br/>Redirect to <b>{{name}}</b>...",
        "SUCCESS_REDIRECTING": "Payment sent.<br/>Redirect to the seller's website...",
        "CANCEL_REDIRECTING_WITH_NAME": "Payment cancelled.<br/>Redirect to <b>{{name}}</b>...",
        "CANCEL_REDIRECTING": "Payment cancelled.<br/>Redirect to the seller's website..."
      },
      "ERROR": {
        "TRANSFER_FAILED": "Payment failed"
      }
    },
    "DOC": {
      "DESCRIPTION_DIVIDER": "Description",
      "URL_DIVIDER": "Calling address",
      "PARAMETERS_DIVIDER": "Parameters",
      "AVAILABLE_PARAMETERS": "Here is the list of al available parameters:",
      "DEMO_DIVIDER": "Try it !",
      "DEMO_HELP": "To test this service, click on this button. The result content will be display below.",
      "DEMO_RESULT": "Result returned by call:",
      "DEMO_SUCCEED": "<i class=\"icon ion-checkmark\"></i> Success!",
      "DEMO_CANCELLED": "<i class=\"icon ion-close\"></i> Canceled by user",
      "INTEGRATE_DIVIDER": "Website integration",
      "INTEGRATE_CODE": "Code:",
      "INTEGRATE_RESULT": "Result preview:",
      "INTEGRATE_PARAMETERS": "Parameters",
      "TRANSFER": {
        "TITLE": "Payments",
        "DESCRIPTION": "From a site (eg online marketplace) you can delegate payment in free currency to Cesium API. To do this, simply open a page at the following address:",
        "PARAM_PUBKEY": "Recipient's public key",
        "PARAM_PUBKEY_HELP": "Recipient's public key (required)",
        "PARAM_AMOUNT": "Amount",
        "PARAM_AMOUNT_HELP": "Transaction amount (required)",
        "PARAM_COMMENT": "Reference (or comment)",
        "PARAM_COMMENT_HELP": "Reference or comment. You will allow for example to identify the payment in the BlockChain.",
        "PARAM_NAME": "Name (of recipient or website)",
        "PARAM_NAME_HELP": "The name of your website. This can be a readable name (eg \"My online site\"), or a web address (eg \"www.MySite.com\").",
        "PARAM_REDIRECT_URL": "URL redirection",
        "PARAM_REDIRECT_URL_HELP": "URL redirection after sending payment, after the payment has been sent. Can contain the following strings, which will be replaced by the values of the transaction: \"{tx}\", \"{hash}\", \"{comment}\", \"{amount}\" and \"{pubkey}\".",
        "PARAM_CANCEL_URL": "URL if cancelled",
        "PARAM_CANCEL_URL_HELP": "URL in case of cancellation.  Can contain the following strings, which will be replaced: \"{comment}\", \"{amount}\" and \"{pubkey}\".",
        "EXAMPLES_HELP": "Examples of integration:",
        "EXAMPLE_BUTTON": "HTML Button",
        "EXAMPLE_BUTTON_DEFAULT_TEXT": "Pay in {{currency|abbreviate}}",
        "EXAMPLE_BUTTON_DEFAULT_STYLE": "Custom style",
        "EXAMPLE_BUTTON_TEXT_HELP": "Button text",
        "EXAMPLE_BUTTON_BG_COLOR": "Background color",
        "EXAMPLE_BUTTON_BG_COLOR_HELP": "eg: #fbc14c, yellow, lightgrey, rgb(180,180,180)",
        "EXAMPLE_BUTTON_FONT_COLOR": "Font color",
        "EXAMPLE_BUTTON_FONT_COLOR_HELP": "eg: black, orange, rgb(180,180,180)",
        "EXAMPLE_BUTTON_TEXT_ICON": "Icon",
        "EXAMPLE_BUTTON_TEXT_WIDTH": "Width",
        "EXAMPLE_BUTTON_TEXT_WIDTH_HELP": "eg: 200px, 50%",
        "EXAMPLE_BUTTON_ICON_NONE": "No icon",
        "EXAMPLE_BUTTON_ICON_DUNITER": "Duniter logo",
        "EXAMPLE_BUTTON_ICON_CESIUM": "Cesium logo",
        "EXAMPLE_BUTTON_ICON_G1_COLOR": "Ğ1 logo",
        "EXAMPLE_BUTTON_ICON_G1_BLACK": "Ğ1 logo (outline)"
      }
    }
  }
}
);

$translateProvider.translations("es-ES", {
  "COMMON": {
    "APP_NAME": "Cesium",
    "APP_VERSION": "v{{version}}",
    "APP_BUILD": "fecha : {{build}}",
    "PUBKEY": "Llave publica",
    "MEMBER": "Miembro",
    "BLOCK" : "Bloque",
    "BTN_OK": "OK",
    "BTN_YES": "Si",
    "BTN_NO": "No",
    "BTN_SEND": "Mandar",
    "BTN_SEND_MONEY": "Hacer una transferencia",
    "BTN_SEND_MONEY_SHORT": "Transferencia",
    "BTN_SAVE": "Registrar",
    "BTN_YES_SAVE": "Si, Registrar",
    "BTN_YES_CONTINUE": "Si, Continuar",
    "BTN_SHOW": "Ver",
    "BTN_SHOW_PUBKEY": "Ver la llave",
    "BTN_RELATIVE_UNIT": "Visualizar en unidad relativa ?",
    "BTN_BACK": "Regreso",
    "BTN_NEXT": "Siguiente",
    "BTN_CANCEL": "Anular",
    "BTN_CLOSE": "Cerrar",
    "BTN_LATER": "Más tarde",
    "BTN_LOGIN": "Connectarse",
    "BTN_LOGOUT": "Desconexión",
    "BTN_ADD_ACCOUNT": "Nueva cuenta",
    "BTN_SHARE": "Compartir",
    "BTN_EDIT": "Modificar",
    "BTN_DELETE": "Suprimir",
    "BTN_ADD": "Añadir",
    "BTN_SEARCH": "Buscar",
    "BTN_REFRESH": "Actualisar",
    "BTN_RETRY": "Empezar de nuevo",
    "BTN_START": "Empezar",
    "BTN_CONTINUE": "Continuar",
    "BTN_CREATE": "Crear",
    "BTN_UNDERSTOOD": "He entendido",
    "BTN_OPTIONS": "Opciónes",
    "BTN_HELP_TOUR": "Visita guiada",
    "BTN_HELP_TOUR_SCREEN": "Descubrir esta pantalla",
    "BTN_DOWNLOAD": "Descargar",
    "BTN_DOWNLOAD_ACCOUNT_STATEMENT": "Descargar el estado de cuenta",
    "BTN_MODIFY": "Cambio",
    "CHOOSE_FILE": "Publique archivo<br/>o haga clic para seleccionar la",
    "DAYS": "Dias",
    "NO_ACCOUNT_QUESTION": "Todavía no miembre ? Creer una cuenta !",
    "SEARCH_NO_RESULT": "Ninguno resultado encontrado",
    "LOADING": "Espera por favor...",
    "SEARCHING": "Búsqueda en proceso...",
    "FROM": "De",
    "TO": "A",
    "COPY": "Copiar",
    "LANGUAGE": "Idioma",
    "UNIVERSAL_DIVIDEND": "Dividendo universal",
    "UD": "DU",
    "DATE_PATTERN": "DD/MM/YYYY HH:mm",
    "DATE_FILE_PATTERN": "YYYY-MM-DD",
    "DATE_SHORT_PATTERN": "DD/MM/YY",
    "DATE_MONTH_YEAR_PATTERN": "MM/YYYY",
    "EMPTY_PARENTHESIS": "(vacío)",
    "UID": "Seudónimo",
    "ENABLE": "Activado",
    "DISABLE": "Desactivado",
    "RESULTS_LIST": "Resultados :",
    "RESULTS_COUNT": "{{count}} resultados",
    "EXECUTION_TIME": "ejecutado en {{duration|formatDurationMs}}",
    "SHOW_VALUES": "Publicar los valores no codificados ?",
    "POPOVER_ACTIONS_TITLE": "Opciónes",
    "POPOVER_FILTER_TITLE": "Filtros",
    "SHOW_MORE": "Publicar más",
    "SHOW_MORE_COUNT": "(límite actual a {{limit}})",
    "POPOVER_SHARE": {
      "TITLE": "Compartir",
      "SHARE_ON_TWITTER": "Compartir sobre Twitter",
      "SHARE_ON_FACEBOOK": "Compartir sobre Facebook",
      "SHARE_ON_DIASPORA": "Compartir sobre Diaspora*",
      "SHARE_ON_GOOGLEPLUS": "Compartir sobre Google+"
    }
  },
  "SYSTEM": {
    "PICTURE_CHOOSE_TYPE": "Eligir la fuente :",
    "BTN_PICTURE_GALLERY": "Galería",
    "BTN_PICTURE_CAMERA": "<b>Cámara</b>"
  },
  "MENU": {
    "HOME": "Recepción",
    "WOT": "Anuario",
    "CURRENCY": "Moneda",
    "ACCOUNT": "Mi cuenta",
    "TRANSFER": "Transferencia",
    "SCAN": "Escáner",
    "SETTINGS": "Configuraciónes",
    "NETWORK": "Red",
    "TRANSACTIONS": "Mis operaciónes"
  },
  "ABOUT": {
    "TITLE": "A propósito ",
    "LICENSE": "Aplicación <b>libre</b> (licencia GNU GPLv3).",
    "LATEST_RELEASE": "Hay una <b>versión más nueva</b> de {{'COMMON.APP_NAME' | translate}} (<b>v{{version}}</b>)",
    "PLEASE_UPDATE": "Por favor actualice {{'COMMON.APP_NAME' | translate}} (última versión: <b>v{{version}}</b>)",
    "CODE": "Codigo fuente :",
    "DEVELOPERS": "Desarrollado por :",
    "FORUM": "Foro :",
    "PLEASE_REPORT_ISSUE": "No duda a informarnos de las anomalías encontradas",
    "REPORT_ISSUE": "Informar de un problema"
  },
  "HOME": {
    "TITLE": "Cesium",
    "WELCOME": "Bienvenida en la aplicación Cesium !",
    "MESSAGE": "Sigue sus cuentas de {{currency|abbreviate}} con facilidad",
    "BTN_CURRENCY": "Explorar la moneda",
    "BTN_ABOUT": "A propósito",
    "BTN_HELP": "Ayuda en línea",
    "REPORT_ISSUE": "anomalía",
    "NOT_YOUR_ACCOUNT_QUESTION" : "Usted no es dueño de la cuenta <<b><i class=\"ion-key\"></i> {{pubkey|formatPubkey}}</b>?",
    "BTN_CHANGE_ACCOUNT": "Desconectar esta cuenta",
    "CONNECTION_ERROR": "Nodo <b>{{servidor}}</b> inalcanzable o dirección no válida.<br/><br/>Compruebe su conexión a Internet, o nodo de conmutación <a class=\"positive\" ng-click=\"doQuickFix('settings')\">en los parámetros</a>."
  },
  "SETTINGS": {
    "TITLE": "Configuraciónes",
    "NETWORK_SETTINGS": "Red",
    "PEER": "Dirección del nodo Duniter",
    "PEER_CHANGED_TEMPORARY": "Dirección utiliza temporalmente",
    "USE_LOCAL_STORAGE": "Activar el almacenamiento local",
    "USE_LOCAL_STORAGE_HELP": "Permitir el ahorro de almacenamiento local",
    "ENABLE_HELPTIP": "Activar bocadillos contextuales de ayuda",
    "ENABLE_UI_EFFECTS": "Activar los efectos visuales",
    "HISTORY_SETTINGS": "Operaciones de cuentas",
    "DISPLAY_UD_HISTORY": "Publicar los dividendos producidos ?",
    "AUTHENTICATION_SETTINGS": "Autenticación",
    "KEEP_AUTH": "Caducidad de la autenticación",
    "KEEP_AUTH_HELP": "Definir cuándo se borra la autenticación de la memoria",
    "KEEP_AUTH_OPTION": {
      "NEVER": "Despues de cada operacion",
      "SECONDS": "Después de {{value}} segundos de inactividad",
      "MINUTE": "Después de {{value}} minuto de inactividad",
      "MINUTES": "Después de {{value}} minutos de inactividad",
      "HOUR": "Después de {{value}} hora de inactividad",
      "ALWAYS": "Al final de la sesión"
    },
    "REMEMBER_ME": "Recordarme",
    "REMEMBER_ME_HELP": "Siempre mantenerse conectado (no recomendado).",
    "PLUGINS_SETTINGS": "Extensiónes",
    "BTN_RESET": "Restaurar los valores por defecto" ,
    "EXPERT_MODE": "Activar el modo experto",
    "EXPERT_MODE_HELP": "Permite una visualización más detallada",
    "POPUP_PEER": {
      "TITLE" : "Nodo Duniter",
      "HOST" : "Dirección",
      "HOST_HELP": "Dirección : servidor:puerto",
      "USE_SSL" : "Segura ?",
      "USE_SSL_HELP" : "(Cifrado SSL)",
      "BTN_SHOW_LIST" : "Lista de nodos"
    }
  },
  "BLOCKCHAIN": {
    "HASH": "Hash : {{hash}}",
    "VIEW": {
      "HEADER_TITLE": "Bloque #{{number}}-{{hash|formatHash}}",
      "TITLE_CURRENT": "Bloque corriente",
      "TITLE": "Bloque #{{number|formatInteger}}",
      "COMPUTED_BY": "Calculado por el nodo de",
      "SHOW_RAW": "Ver el fichero en bruto",
      "TECHNICAL_DIVIDER": "Informaciónes técnicas",
      "VERSION": "Versión del formato",
      "HASH": "Hash calculado",
      "UNIVERSAL_DIVIDEND_HELP": "Moneda co-producida por cada uno de los {{membersCount}} miembros",
      "EMPTY": "Ninguno dato en este bloque",
      "POW_MIN": "Dificultad mínima",
      "POW_MIN_HELP": "Dificultad impuesta por el cálculo del hash",
      "DATA_DIVIDER": "Datos",
      "IDENTITIES_COUNT": "Nuevas identidades",
      "JOINERS_COUNT": "Nuevos miembros",
      "ACTIVES_COUNT": "Renovaciónes",
      "ACTIVES_COUNT_HELP": "Miembros que han renovado sus adhesiónes ",
      "LEAVERS_COUNT": "Miembros salientes",
      "LEAVERS_COUNT_HELP": "Miembros que no quieran certificación",
      "EXCLUDED_COUNT": "Miembros excluidos",
      "EXCLUDED_COUNT_HELP": "Antiguos miembros excluidos por no renovación o falta de certificaciónes",
      "REVOKED_COUNT": "Identidades revocadas",
      "REVOKED_COUNT_HELP": "Estas cuentas no podrán estar miembros",
      "TX_COUNT": "Transacciónes",
      "CERT_COUNT": "Certificaciónes",
      "TX_TO_HIMSELF": "Operación de cambio",
      "TX_OUTPUT_UNLOCK_CONDITIONS": "Condiciónes de desbloqueo",
      "TX_OUTPUT_OPERATOR": {
        "AND": "y",
        "OR": "o"
      },
      "TX_OUTPUT_FUNCTION": {
        "SIG": "<b>Firma</b> de ",
        "XHX": "<b>Contraseña</b>, dont SHA256 =",
        "CSV": "Bloqueado durante",
        "CLTV": "Bloqueado hasta"
      }
    },
    "LOOKUP": {
      "TITLE": "Bloques",
      "NO_BLOCK": "Ningun bloque",
      "LAST_BLOCKS": "últimos bloques :",
      "BTN_COMPACT": "Compactar"
    }
  },
  "CURRENCY": {
    "VIEW": {
      "TITLE": "Moneda",
      "TAB_CURRENCY": "Moneda",
      "TAB_WOT": "Red de confianza",
      "TAB_NETWORK": "Red",
      "TAB_BLOCKS": "Bloques",
      "CURRENCY_SHORT_DESCRIPTION": "{{currency|capitalizar}} es un <b>moneda libre</b>, iniciado {{firstBlockTime|formatFromNow}}. Ella actualmente <b>{{N}} miembros</b>, que producen y recibir un <a ng-click=\"showHelpModal('ud')\">Dividendo universal</a> (DU), cada {{dt|formatPeriod}}.",
      "NETWORK_RULES_DIVIDER": "Reglas de la red",
      "CURRENCY_NAME": "Nombre de la moneda",
      "MEMBERS": "Número de miembros",
      "MEMBERS_VARIATION": "variaciónes el último DU",
      "MONEY_DIVIDER": "Moneda",
      "MASS": "Masa monetaria",
      "SHARE": "Masa por miembro",
      "UD": "Dividendo universal",
      "C_ACTUAL": "Crecimiento actual",
      "MEDIAN_TIME": "Hora de la blockchain",
      "POW_MIN": "Nivel mínimo de dificultad de cálculo",
      "MONEY_RULES_DIVIDER": "Reglas de la moneda",
      "C_RULE": "Crecimiento teórico objetivo",
      "UD_RULE": "Cálculo del dividendo universal",
      "DT_REEVAL": "Periodo de revalorización del DU",
      "REEVAL_SYMBOL": "reval",
      "DT_REEVAL_VALUE": "Todos los <b>{{dtReeval|formatDuration}}</b> ({{dtReeval/86400}} {{'COMMON.DAYS'|translate}})",
      "UD_REEVAL_TIME0": "Fecha de la primera revalorización",
      "SIG_QTY_RULE": "Número de certificaciónes requeridas para estar miembro",
      "SIG_STOCK": "Número máximo de certificaciónes emitidas por miembros",
      "SIG_PERIOD": "Plazo mínimo de espera entre 2 certificaciónes sucesivas emitidas por la misma persona",
      "SIG_WINDOW": "Plazo límite para toma en cuenta de una certificación",
      "SIG_VALIDITY": "Duración de una certificación que se ha tenido en cuenta.",
      "MS_WINDOW": "Duración límite para toma en cuenta de una adhesión",
      "MS_VALIDITY": "Duración de una adhesión que se ha tenido en cuenta",
      "STEP_MAX": "Distancia máxima entre un nuevo entrante y los miembros referentes",
      "WOT_RULES_DIVIDER": "Regla de la red de confianza",
      "SENTRIES": "Número de certificaciónes necesarias para hacerse miembro referente",
      "SENTRIES_FORMULA": "Número de certificaciónes necesarias para hacerse miembro referente (fórmula)",
      "XPERCENT":"Porcentaje mínimo de miembros referentes a alcanzar para respetar la regla de distancia",
      "AVG_GEN_TIME": "Tiempo medio entre dos bloques",
      "CURRENT": "actual",
      "MATH_CEILING": "TECHO",
      "DISPLAY_ALL_RULES": "Ver todas las reglas?",
      "BTN_SHOW_LICENSE": "Ver la licencia",
      "WOT_DIVIDER": "Red de confianza"
    },
    "LICENSE": {
      "TITLE": "License de la monnaie",
      "BTN_DOWNLOAD": "Télécharger le fichier",
      "NO_LICENSE_FILE": "Fichier de license non trouvé."
    }
  },
  "NETWORK": {
    "VIEW": {
      "MEDIAN_TIME": "Hora de la blockchain",
      "LOADING_PEERS": "Carga de los nodos...",
      "NODE_ADDRESS": "Dirección :",
      "SOFTWARE": "Software :",
      "WARN_PRE_RELEASE": "prelanzamiento (última versión estable : <b>{{version}}</b>)",
      "WARN_NEW_RELEASE": "Versión <b>{{version}}</b> disponible",
      "WS2PID": "ID :",
      "POW_PREFIX": "Préfixe de preuve de travail :",
      "ENDPOINTS": {
        "BMAS": "Endpoint seguro (SSL)",
        "BMATOR": "TOR endpoint",
        "WS2P": "WS2P endpoint",
        "ES_USER_API": "Nodo de datos Cesium+"
      }
    },
    "INFO": {
      "ONLY_SSL_PEERS": "La visualización de los nodos no SSL es deteriorada, porque Cesium funciona en modo HTTPS."
    }
  },
  "PEER": {
    "PEERS": "Nodos",
    "SIGNED_ON_BLOCK": "Firmado sobre el bloque",
    "MIRROR": "espejo",
    "MIRRORS": "Nodos espejos",
    "PEER_LIST" : "Lista de nodos",
    "MEMBERS" : "Nodos miembros",
    "ALL_PEERS" : "Todos los nodos",
    "DIFFICULTY" : "Dificultad",
    "API" : "API",
    "CURRENT_BLOCK" : "Bloque #",
    "POPOVER_FILTER_TITLE": "Filtro",
    "OFFLINE": "Nodos fuera de línea",
    "BTN_SHOW_PEER": "Ver nodo",
    "VIEW": {
      "TITLE": "Nodo",
      "OWNER": "Propiedad de ",
      "SHOW_RAW_PEERING": "Ver la tarjeta de red",
      "SHOW_RAW_CURRENT_BLOCK": "Ver el último bloque (formato sin formato)",
      "LAST_BLOCKS": "Bloques recientes",
      "KNOWN_PEERS": "Nodos conocidos :",
      "GENERAL_DIVIDER": "Informaciónes generales",
      "ERROR": {
        "LOADING_TOR_NODE_ERROR": "Could not get peer data, using the TOR network.",
        "LOADING_NODE_ERROR": "Could not get peer data"
      }
    }
  },
  "WOT": {
    "SEARCH_HELP": "Búsqueda (seudónimo o llave publica)",
    "SEARCH_INIT_PHASE_WARNING": "Durante la etapa de preinscripción, la búsqueda de las inscripciónes en espera <b>puede ser largo</b>. Gracias por su paciencia...",
    "REGISTERED_SINCE": "Registrado en",
    "REGISTERED_SINCE_BLOCK": "Registrado al bloque #",
    "NO_CERTIFICATION": "Ninguna certificación validada",
    "NO_GIVEN_CERTIFICATION": "Ninguna certificación emitida",
    "NOT_MEMBER_PARENTHESIS": "(no miembro)",
    "IDENTITY_REVOKED_PARENTHESIS": "(identidad revocada)",
    "MEMBER_PENDING_REVOCATION_PARENTHESIS": "(revocación en proceso)",
    "EXPIRE_IN": "Expiración",
    "NOT_WRITTEN_EXPIRE_IN": "Fecha límite<br/>de tratamiento",
    "EXPIRED": "Expirado",
    "PSEUDO": "Seudónimo",
    "SIGNED_ON_BLOCK": "Emitida al bloque #{{block}}",
    "WRITTEN_ON_BLOCK": "Escrita al bloque #{{block}}",
    "GENERAL_DIVIDER": "Informaciónes generales",
    "NOT_MEMBER_ACCOUNT": "Cuenta no miembro",
    "NOT_MEMBER_ACCOUNT_HELP": "Se trata de un simple monedero, sin solicitud de adhesión en espera.",
    "TECHNICAL_DIVIDER": "Informaciónes técnicas",
    "BTN_CERTIFY": "Certificar",
    "BTN_YES_CERTIFY": "Si, certificar",
    "BTN_SELECT_AND_CERTIFY": "Nueva certificación",
    "ACCOUNT_OPERATIONS": "Operaciones de cuenta",
    "VIEW": {
      "POPOVER_SHARE_TITLE": "Identidad {{title}}"
    },
    "LOOKUP": {
      "TITLE": "Anuario",
      "NEWCOMERS": "Nuevos miembros :",
      "NEWCOMERS_COUNT": "{{count}} miembros",
      "PENDING": "Inscripciónes en espera :",
      "PENDING_COUNT": "{{count}} inscripciónes en espera",
      "REGISTERED": "Inscrito {{sigDate | formatFromNow}}",
      "MEMBER_FROM": "Miembro desde {{memberDate|formatFromNowShort}}",
      "BTN_NEWCOMERS": "Nuevos miembros",
      "BTN_PENDING": "Inscripciónes en espera",
      "SHOW_MORE": "Publicar más",
      "SHOW_MORE_COUNT": "(límite actual a {{limit}})",
      "NO_PENDING": "Ninguna inscripción en espera.",
      "NO_NEWCOMERS": "Ningun miembro."
    },
    "CONTACTS": {
      "TITLE": "Contactos"
    },
    "MODAL": {
      "TITLE": "Búsqueda"
    },
    "CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certificaciónes",
      "SUMMARY": "Certificaciónes recibidas",
      "LIST": "Detalle de las certificaciónes recibidas",
      "PENDING_LIST": "Certificaciónes en espera de tratamiento",
      "RECEIVED": "Certificaciónes recibidas",
      "RECEIVED_BY": "Certificaciónes recibidas por {{uid}}",
      "ERROR": "Certificaciónes recibidas por error",
      "SENTRY_MEMBER": "Miembro referente"
    },
    "OPERATIONS": {
      "TITLE": "{{uid}} - Operaciones"
    },
    "GIVEN_CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certificaciónes emitidas",
      "SUMMARY": "Certificaciónes emitidas",
      "LIST": "Detalle de las certificaciónes emitidas",
      "PENDING_LIST": "Certificaciónes en espera de tratamiento",
      "SENT": "Certificaciónes emitidas",
      "SENT_BY": "Certificaciónes emitidas por {{uid}}",
      "ERROR": "Certificaciónes emitidas por error"
    }
  },
  "LOGIN": {
    "TITLE": "<i class=\"icon ion-locked\"></i> Conexión",
    "SALT": "Identificador secreto",
    "SALT_HELP": "Identificador secreto",
    "SHOW_SALT": "Visualizar el identificador secreto ?",
    "PASSWORD": "Contraseña",
    "PASSWORD_HELP": "Contraseña",
    "PUBKEY_HELP": "Ejemplo : « AbsxSY4qoZRzyV2irfep1V9xw1EMNyKJw2TkuVD4N1mv »",
    "NO_ACCOUNT_QUESTION": "Ahora no tiene cuenta ?",
    "CREATE_ACCOUNT": "Creer una cuenta",
    "FORGOTTEN_ID": "Contraseña olvidada ?",
    "AUTO_LOGOUT": {
      "TITLE": "Información",
      "MESSAGE": "<i class=\"ion-android-time\"></i> Has sido <b>fuera de línea</b> de forma automática, después de una inactividad prolongada.",
      "BTN_RELOGIN": "Me vuelva a conectar",
      "IDLE_WARNING": "Se le desconectará... {{countdown}}"
    }
  },
  "AUTH": {
    "TITLE": "<i class=\"icon ion-locked\"></i> AAutenticación",
    "METHOD_LABEL": "Método de autenticación",
    "BTN_AUTH": "Autenticar",
    "SCRYPT_FORM_HELP": "Por favor autentícate :"
  },
  "ACCOUNT": {
    "TITLE": "Mi cuenta",
    "BALANCE": "Saldo",
    "LAST_TX": "última transacción",
    "BALANCE_ACCOUNT": "Saldo de la cuenta",
    "NO_TX": "Ninguna transacción",
    "SHOW_MORE_TX": "Publicar más",
    "SHOW_ALL_TX": "Publicar todo",
    "TX_FROM_DATE": "(límite actual a {{fromTime|formatFromNowShort}})",
    "PENDING_TX": "Transacciónes en proceso de tratamiento",
    "ERROR_TX": "Transacciónes no ejecutadas",
    "ERROR_TX_SENT": "Transacciónes mandadas",
    "PENDING_TX_RECEIVED": "Tansacciones en espera de la recepción",
    "EVENTS": "Eventos",
    "WAITING_MEMBERSHIP": "Solicitud de adhesión mandada. En espera de aceptación.",
    "WAITING_CERTIFICATIONS": "Debe obtener {{needCertificationCount}} certificación(es) para estar miembro.",
    "WILL_MISSING_CERTIFICATIONS": "Pronto, va a <b>faltar certificación</b> (al menos {{willNeedCertificationCount}} es necesario)",
    "WILL_NEED_RENEW_MEMBERSHIP": "Su adhesión como miembro <b>va a expirar {{membershipExpiresIn|formatDurationTo}}</b>. Piensa a <a ng-click=\"doQuickFix('renew')\">renovar su adhesión</a> mientras tanto.",
    "NEED_RENEW_MEMBERSHIP": "No esta miembro, porque su adhesión <b>ha expirado</b>. Pensez à <a ng-click=\"doQuickFix('renew')\">renovar su adhesión</a>.",
    "NO_WAITING_MEMBERSHIP": "No hay adhesión como miembro en espera. Si desea <b>convertirse en miembro</b>, por favor <a ng-click=\"doQuickFix('membership')\">envíe su adhesión como miembro</a>.",
    "CERTIFICATION_COUNT": "Certificacións recibidas",
    "CERTIFICATION_COUNT_SHORT": "Certificacións",
    "SIG_STOCK": "Certificacións mandadas",
    "BTN_RECEIVE_MONEY": "Cobrar",
    "BTN_SELECT_ALTERNATIVES_IDENTITIES": "Cambiar a otra identidad ...",
    "BTN_MEMBERSHIP_IN_DOTS": "Estar miembro...",
    "BTN_MEMBERSHIP_RENEW": "Renovar la adhesión",
    "BTN_MEMBERSHIP_RENEW_DOTS": "Renovar la adhesión...",
    "BTN_MEMBERSHIP_OUT_DOTS": "Parar la adhesión...",
    "BTN_SEND_IDENTITY_DOTS": "Publicar su identidad...",
    "BTN_SECURITY_DOTS": "Cuenta y securidad...",
    "BTN_SHOW_DETAILS": "Publicar las infos técnicas",
    "LOCKED_OUTPUTS_POPOVER": {
      "TITLE": "Importe bloqueado",
      "DESCRIPTION": "Aquí está las condiciónes de desbloqueo de este importe :",
      "DESCRIPTION_MANY": "Esta transacción es compuesta de varias partes, cuyas tiene las condiciónes de desbloqueo :",
      "LOCKED_AMOUNT": "Condiciónes por el importe :"
    },
    "NEW": {
      "TITLE": "Inscripción",
      "INTRO_WARNING_TIME": "Crear una cuenta en {{name|capitalize}} es muy simple. Sin embargo, por favor tome el tiempo suficiente para tomar correctamente este paso (no olvidar los identificadores, contraseñas, etc.).",
      "INTRO_WARNING_SECURITY": "Asegúrate de que el equipo que se utiliza actualmente (ordenador, tableta, teléfono) <b>es seguro y digno de confianza</b>.",
      "INTRO_WARNING_SECURITY_HELP": "Actualizaciones de antivirus, firewall activado, sesión protegidos por contraseña o código PIN, etc.",
      "INTRO_HELP": "Haga clic en <b>{{'COMMON.BTN_START'|translate}}</b> para iniciar la creación de la cuenta. Se le guiará paso a paso.",
      "REGISTRATION_NODE": "Su registro será grabado a través del nodo Duniter <b>{{server}}</b>, que luego se transmitió al resto del sistema de moneda.",
      "REGISTRATION_NODE_HELP": "Si usted no confía en este nodo, <a ng-click=\"doQuickFix('settings')\">cambie la configuración</a> de Cesium.",
      "SELECT_ACCOUNT_TYPE": "Elegir el tipo de cuenta para crear:",
      "MEMBER_ACCOUNT": "Cuenta miembro",
      "MEMBER_ACCOUNT_TITLE": "Crear de un cuenta de miembro",
      "MEMBER_ACCOUNT_HELP": "Si ya no está inscrito como un individuo (Solamente una cuenta posible por individuo).",
      "WALLET_ACCOUNT": "Simple monedero",
      "WALLET_ACCOUNT_TITLE": "Crear de un simple cuenta",
      "WALLET_ACCOUNT_HELP": "Simple cartera por todos otros casos, por ejemplo si necesita una cuenta suplementaria.<br/>No Dividendo Universal sera creido por esta cuenta.",
      "SALT_WARNING": "Elige su identificador secreto.<br/>Se solicitará cada vez se conectará con esta cuenta.<br/><br/><b>Retiene bien este identificador secreto</b>.<br/>En caso de pérdida, nadie podrá acceder su cuenta !",
      "PASSWORD_WARNING": "Elige su contraseña.<br/>Se solicitará cada vez se conectará con esta cuenta.<br/><br/><b>Retiene bien esta contraseña</b>.<br/>En caso de pérdida, nadie podrá acceder su cuenta !",
      "PSEUDO_WARNING": "Elige un seudónimo.<br/>Sirbe a los otros miembros para encontrarse más fácilmente.<br/><br/>No debe contener <b>nni espacio ni carácter accentuado</b>.<div class='hidden-xs'><br/>Exemple : <span class='gray'>SophieDupond, MarcelChemin, etc.</span>",
      "PSEUDO": "Seudónimo",
      "PSEUDO_HELP": "Seudónimo",
      "SALT_CONFIRM": "Confirmación",
      "SALT_CONFIRM_HELP": "Confirmación de el identificador secreto",
      "PASSWORD_CONFIRM": "Confirmación",
      "PASSWORD_CONFIRM_HELP": "Confirmación de la contraseña",
      "SLIDE_6_TITLE": "Confirmación :",
      "COMPUTING_PUBKEY": "Cálculo en proceso...",
      "LAST_SLIDE_CONGRATULATION": "<b>Bravo !</b> Ha introducido todas las informaciónes necesarias.<br/><b>Puede mandar la solicitud</b> de creación de su cuenta.</b><br/><br/>Por su información, la llave pública más abajo identificará su cuenta futura.<br/>Podrá estar comunicada a terceros para recibir sus pagos.<br/>Sin embargo, <b>no es útil</b> anotarla aquí.",
      "CONFIRMATION_MEMBER_ACCOUNT": "<b class=\"assertive\">Advertencia :</b> el identificador secreto, la contraseña y el seudónimo no podrán estar modificados.<br/><b>Asegurase siempre se los recordar !</b><br/><br/><b>Está usted seguro</b> querer mandar esta solicitud de inscripción ?",
      "CONFIRMATION_WALLET_ACCOUNT": "<b class=\"assertive\">Advertencia :</b> el identificador secreto y la contraseña no podrán estar modificados.<br/><b>Asegurase siempre se los recordar !</b><br/><br/><b>Está usted seguro</b> querer continuar con estos identificadores ?",
      "CHECKING_PSEUDO": "Comprobación de disponibilidad...",
      "PSEUDO_AVAILABLE": "Este nombre está disponible",
      "PSEUDO_NOT_AVAILABLE": "Este nombre de usuario no está disponible",
      "INFO_LICENSE": "Para unirse a la moneda, le pedimos que leer y aceptar esta licencia.",
      "BTN_ACCEPT": "Acepto",
      "BTN_ACCEPT_LICENSE": "Acepto la licencia"
    },
    "POPUP_REGISTER": {
      "TITLE": "Elige un seudónimo",
      "HELP": "Un seudónimo es obligatorio para estar membre."
    },
    "SELECT_IDENTITY_MODAL": {
      "TITLE": "Selección de identidad",
      "HELP": "Se han enviado varias <b>identidades diferentes</b> para la clave pública <span class=\"gray\"><i class=\"ion-key\"></i>{{pubkey | formatPubkey}}</span>.<br/>Por favor, selecciona la carpeta para usar:"
    },
    "SECURITY": {
      "ADD_QUESTION": "Añadir una pregunta personalizada ",
      "BTN_CLEAN": "Vaciar",
      "BTN_RESET": "Reinicializar",
      "DOWNLOAD_REVOKE": "Salvar un fichero de revocación",
      "HELP_LEVEL": "Para generar un fichero de salvaguarda de sus identificadores, elige <strong> al menos {{nb}} preguntas :</strong>",
      "LEVEL": "Nivel de seguridad",
      "LOW_LEVEL": "Bajo <span class=\"hidden-xs\">(2 preguntas mínimo)</span>",
      "MEDIUM_LEVEL": "Medio <span class=\"hidden-xs\">(4 preguntas mínimo)</span>",
      "QUESTION_1": "Como se llamaba su mejor amigo cuando estuvo adolescente ?",
      "QUESTION_2": "Como se llamaba su primer animal de compañía ?",
      "QUESTION_3": "Cuál es el primer plato ha aprendido a cocinar ?",
      "QUESTION_4": "Cuál es la primera película ha visto al cine ?",
      "QUESTION_5": "Adonde fue la primera vez ha cogido el avión ?",
      "QUESTION_6": "Como se llamaba su preferido maestro a la escuela primaria ?",
      "QUESTION_7": "Cuál sería para usted lo mejor oficio ?",
      "QUESTION_8": "Cuál es el libro para niños usted prefiere ?",
      "QUESTION_9": "Cuál fue el modelo de su primero vehículo ?",
      "QUESTION_10": "Cuál fue su sobrenombre cuando estuvo niño/a ?",
      "QUESTION_11": "Cuál fue su personaje o actor/actriz preferido/a cuando estuvo estudiante ?",
      "QUESTION_12": " Cuál fue su cantante o grupo preferido/a cuando estuvo estudiante ?",
      "QUESTION_13": "En qué ciudad sus padres se han encontrado ?",
      "QUESTION_14": "Como se llamaba su primero/a jefe ?",
      "QUESTION_15": "Como se llama la calle donde creció ?",
      "QUESTION_16": "Como se llama la primera playa donde se bañó ?",
      "QUESTION_17": "Cuál es el primero álbum se compró ?",
      "QUESTION_18": "Cuál es el nombre de su equipo de deporte preferido ?",
      "QUESTION_19": "Cuál fue el oficio de su abuelo ?",
      "RECOVER_ID": "Recuperar sus identificadores",
      "REVOCATION_WITH_FILE": "Revocar una identidad a partir de un fichero",
      "REVOCATION_WITH_FILE_DESCRIPTION": "Si ha perdido las credenciales de su cuenta de miembro de forma permanente (o la seguridad de la cuenta se ve comprometida), puede usar <b>el archivo de revocación de la cuenta</b> para forzar publicación final de la web de confianza.",
      "REVOCATION_WITH_FILE_HELP": "Para <b>revocar permanentemente</b> una cuenta de miembro, arrastre el archivo de revocación en el cuadro siguiente o haga clic en el cuadro para buscar un archivo.",
      "REVOCATION_FILENAME": "revocation-{{uid}}-{{pubkey|formatPubkey}}-{{currency}}.txt",
      "REVOCATION_WALLET": "Revocar esta identidad",
      "SAVE_ID": "Salvar sus identificadores",
      "STRONG_LEVEL": "Alto <span class=\"hidden-xs \">(6 preguntas mínimo)</span>",
      "TITLE": "Cuenta y seguridad"
    },
    "FILE_NAME": "{{currency}} - Encuesta cuenta {{pubkey|formatPubkey}} a {{currentTime|formatDateForFile}}.csv",
    "HEADERS": {
      "TIME": "Fecha",
      "AMOUNT": "Cantidad",
      "COMMENT": "Comentario"
    }
  },
  "TRANSFER": {
    "TITLE": "Transferencia",
    "SUB_TITLE": "Hacer una transferencia",
    "FROM": "De",
    "TO": "A",
    "AMOUNT": "Importe",
    "AMOUNT_HELP": "Importe",
    "COMMENT": "Comentario",
    "COMMENT_HELP": "Comentario",
    "BTN_SEND": "Mandar",
    "BTN_ADD_COMMENT": "Añadir un comentario",
    "WARN_COMMENT_IS_PUBLIC": "Tenga en cuenta que los <b>comentarios son públicos</b> (sin encriptar).",
    "MODAL": {
      "TITLE": "Transferencia"
    }
  },
  "ERROR": {
    "POPUP_TITLE": "Error",
    "UNKNOWN_ERROR": "Error desconocida",
    "CRYPTO_UNKNOWN_ERROR": "Su navegador parece incompatible con las funcionalidades de cryptografía.",
    "EQUALS_TO_PSEUDO": "Debe ser diferente del seudónimo.",
    "EQUALS_TO_SALT": "Debe ser diferente del identificador secreto.",
    "FIELD_REQUIRED": "Campo obligatorio.",
    "FIELD_TOO_SHORT": "Valor demasiado corta.",
    "FIELD_TOO_SHORT_WITH_LENGTH": "Valor demasiado corta ({{minLength}} carácteres mín)",
    "FIELD_TOO_LONG": "Valor demasiado larga",
    "FIELD_TOO_LONG_WITH_LENGTH": "Valor demasiado larga ({{maxLength}} carácteres máx)",
    "FIELD_MIN": "Valor mínimo : {{min}}",
    "FIELD_MAX": "Valor máximo : {{max}}",
    "FIELD_ACCENT": "Carácteres acentuados y comas no autorizados",
    "FIELD_NOT_NUMBER": "Valor numérica esperada",
    "FIELD_NOT_INT": "Valor entera esperada",
    "FIELD_NOT_EMAIL": "Email no es válida",
    "PASSWORD_NOT_CONFIRMED": "No corresponde a la contraseña.",
    "SALT_NOT_CONFIRMED": "No corresponde al identificador secreto.",
    "SEND_IDENTITY_FAILED": "Fracaso de la inscripción.",
    "SEND_CERTIFICATION_FAILED": "Fracaso de la certificación.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY": "No puede realizar certificación, porque su cuenta no <b>está miembro</b>.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY_HAS_SELF": "No puede realizar certificación, porque su cuenta ya no está miembro.<br/><br/>Todavía se falta certificaciónes, o ahora no están validada.",
    "NOT_MEMBER_FOR_CERTIFICATION": "Su cuenta todavía no está miembro.",
    "IDENTITY_TO_CERTIFY_HAS_NO_SELF": "Cuenta no certificable. Ninguna solicitud de adhesión fue hecho, o no fue renovada.",
    "LOGIN_FAILED": "Error durante la authentificación.",
    "LOAD_IDENTITY_FAILED": "Error de carga de la identidad.",
    "LOAD_REQUIREMENTS_FAILED": "Error de carga de las condiciónes de la identidad.",
    "SEND_MEMBERSHIP_IN_FAILED": "Fracaso en el intento de entrada en la comunidad.",
    "SEND_MEMBERSHIP_OUT_FAILED": "Fracaso en la interrupción de adhesión.",
    "REFRESH_WALLET_DATA": "Fracaso en la actualización del monedero.",
    "GET_CURRENCY_PARAMETER": "Fracaso en la recuperación de las reglas de moneda.",
    "GET_CURRENCY_FAILED": "Carga de la moneda imposible. Por favor, intenta más tarde.",
    "SEND_TX_FAILED": "Fracaso en la transferencia.",
    "ALL_SOURCES_USED": "Por favor, espera el cálculo del bloque siguiente (Todas sus fuentes de moneda fueron utilizada).",
    "NOT_ENOUGH_SOURCES": "No lo bastante cambio para mandar este importe en una sola transacción.<br/>Importe máximo : {{amount}} {{unit}}<sub>{{subUnit}}</sub>.",
    "ACCOUNT_CREATION_FAILED": "Fracaso en la creación de la cuenta miembro.",
    "RESTORE_WALLET_DATA_ERROR": "Fracaso en la recarga de las configuración desde el almacenamiento local",
    "LOAD_WALLET_DATA_ERROR": "Fracaso en la carga de los datos del monedero.",
    "COPY_CLIPBOARD_FAILED": "Copia de la valor imposible.",
    "TAKE_PICTURE_FAILED": "Fracaso en la recuperación de la foto.",
    "SCAN_FAILED": "Fracaso en el escán del Codigo QR",
    "SCAN_UNKNOWN_FORMAT": "Codigo no reconocido.",
    "WOT_LOOKUP_FAILED": "Fracaso en la búsqueda",
    "LOAD_PEER_DATA_FAILED": "Lectura del nodo Duniter imposible. Por favor, intenta ulteriormente.",
    "NEED_LOGIN_FIRST": "Por favor, en primer lugar conectase.",
    "AMOUNT_REQUIRED": "El importe es obligatorio.",
    "AMOUNT_NEGATIVE": "Importe negativo no autorizado.",
    "NOT_ENOUGH_CREDIT": "Crédito insuficiente.",
    "INVALID_NODE_SUMMARY": "Nodo ilocalizable o dirección inválida.",
    "INVALID_USER_ID": "El seudónimo no debe contener ni espacio ni carácter especial o acentuado.",
    "INVALID_COMMENT": "El campo 'referencia’ no debe contener carácteres acentuados.",
    "INVALID_PUBKEY": "La llave pública no tiene el formato esperado.",
    "IDENTITY_REVOKED": "Esta identidad <b>fue revocada {{revocationTime|formatFromNow}}</b> ({{revocationTime|formatDate}}). No puede estar miembro.",
    "IDENTITY_PENDING_REVOCATION": "La <b>revocación de esta identidad</b> fue solicitado y esta en espera de tratamiento. Por lo que, la certificación es desactivada.",
    "IDENTITY_INVALID_BLOCK_HASH": "Esta solicitud de adhesión no es valida (porque denomina un bloque los nodos de la red han anulado) : esta persona debe renovelar su solicitud de adhesión <b>antes que</b> estar certificada.",
    "IDENTITY_EXPIRED": "La publicación de esta identidad ha caducada : esta persona debe realizar una nueva solicitud de adhesión <b>antes que</b> estar certificada.",
    "IDENTITY_SANDBOX_FULL": "EL nodo Duniter utilizado por Cesium ya no puede recibir más nuevas identidades, porque la fila de espera es llena.<br/><br/>Por favor, intenta ulteriormente o cambia de nodo (vía el menú <b>Paramètres</b>).",
    "IDENTITY_NOT_FOUND": "Identidad no encontrada",
    "IDENTITY_TX_FAILED": "Las operaciones de carga fallidos",
    "WOT_PENDING_INVALID_BLOCK_HASH": "Adhesión no validada.",
    "WALLET_INVALID_BLOCK_HASH": "Su solicitud de adhesión ya no está validada (porque denomina un bloque los nodos de la red han anulado).<br/>Debe <a ng-click=\"doQuickFix('fixMembership')\">mandar una nueva solicitud</a> para resolver este problema.",
    "WALLET_IDENTITY_EXPIRED": "La publicación de <b>su identidad ha caducada</b>.<br/>Debe <a ng-click=\"doQuickFix('fixIdentity')\">publicar une outra vez su identidad</a> para resolver este problema.",
    "WALLET_REVOKED": "Su identidad fue <b>revocada</b> : ni su seudónimo ni su llave pública podrán estar utilizados en el futuro por una cuenta miembro.",
    "WALLET_HAS_NO_SELF": "Su identidad debe en primer lugar haber estado publicado, y no estar caducada.",
    "AUTH_REQUIRED": "Autenticación requerida.",
    "AUTH_INVALID_PUBKEY": "La llave pública no se corresponde con la cuenta conectada",
    "AUTH_INVALID_SCRYPT": "De usuario o contraseña no válidos.",
    "AUTH_INVALID_FILE": "archivo de llave no válido.",
    "AUTH_FILE_ERROR": "No se pudo abrir el archivo de llave",
    "IDENTITY_ALREADY_CERTIFY": "Ha <b>ya certificado</b> esta identidad.<br/><br/>Esta certificación todavía es valida (expiration {{expiresIn|formatDurationTo}}).",
    "IDENTITY_ALREADY_CERTIFY_PENDING": "Ha <b>ya certificado</b> esta identidad.<br/><br/>Esta certificación está en espera de tratamiento (fecha límite de tratamiento {{expiresIn|formatDurationTo}}).",
    "UNABLE_TO_CERTIFY_TITLE": "Certificación imposible",
    "LOAD_NEWCOMERS_FAILED": "Fracaso el la carga de los miembros nuevos.",
    "LOAD_PENDING_FAILED": "Fracaso el la carga de las inscripciónes en espera.",
    "ONLY_MEMBER_CAN_EXECUTE_THIS_ACTION": "Debe <b>estar miembro</b> para poder realizar esta acción.",
    "ONLY_SELF_CAN_EXECUTE_THIS_ACTION": "Debe haber <b>publicado su identidad</b> para poder realizar esta acción.",
    "GET_BLOCK_FAILED": "Fracaso en la recuperación del bloque",
    "INVALID_BLOCK_HASH": "Bloque no encontrado (hash diferente)",
    "DOWNLOAD_REVOCATION_FAILED": "Debe seleccionar un fichero texto",
    "REVOCATION_FAILED": "Fracas en la revocación.",
    "SALT_OR_PASSWORD_NOT_CONFIRMED": "Identificador secreto o contraseña incorrectos",
    "RECOVER_ID_FAILED": "Fracaso en la recuperación de los identificadores",
    "LOAD_FILE_FAILED" : "Fracaso en la carga del fichero",
    "NOT_VALID_REVOCATION_FILE": "Revocación archivo no válido (formato de archivo malo)",
    "NOT_VALID_SAVE_ID_FILE": "Archivo no válido (formato de archivo malo)",
    "NOT_VALID_KEY_FILE": "Archivo no válido (formato de archivo malo)",
    "EXISTING_ACCOUNT": "Su contraseña corresponde a una cuenta existente, la <a ng-click=\"showHelpModal('pubkey')\">clave pública</a> es:",
    "EXISTING_ACCOUNT_REQUEST": "Por favor, cambie su contraseña para que coincida con una cuenta sin usar.",
    "GET_LICENSE_FILE_FAILED": "Obtener el archivo de licencia no puede",
    "CHECK_NETWORK_CONNECTION": "Sin nodo parece alcanzable.<br/><br/><b>Comprobar la conexión a Internet</b>."
  },
  "INFO": {
    "POPUP_TITLE": "Información",
    "CERTIFICATION_DONE": "Certificación mandada",
    "NOT_ENOUGH_CREDIT": "Crédito insuficiente",
    "TRANSFER_SENT": "Transferencia mandada",
    "COPY_TO_CLIPBOARD_DONE": "Copia realizada",
    "MEMBERSHIP_OUT_SENT": "Rescisión mandada",
    "NOT_NEED_MEMBERSHIP": "Ya está miembro.",
    "IDENTITY_WILL_MISSING_CERTIFICATIONS": "Esta identidad pronto va a faltar certificación (al menos {{willNeedCertificationCount}}).",
    "REVOCATION_SENT": "Revocación mandada",
    "REVOCATION_SENT_WAITING_PROCESS": "La <b>revocación de esta identidad</b> fue solicitada y está en espera de tratamiento.",
    "FEATURES_NOT_IMPLEMENTED": "Esta funcionalidad todavía está en proceso de desarrollo.<br/><br/>Porque no <b>contribuir ahora a Cesium</b>, para obtenerla más rápidamente ? ;)",
    "EMPTY_TX_HISTORY": "Sin exportación operación"
  },
  "CONFIRM": {
    "POPUP_TITLE": "<b>Confirmación</b>",
    "POPUP_WARNING_TITLE": "<b>Advertencia</b>",
    "POPUP_SECURITY_WARNING_TITLE": "<i class=\"icon ion-alert-circled\"></i> <b>Advertencia de seguridad</b>",
    "CERTIFY_RULES_TITLE_UID": "Certificar {{uid}}",
    "CERTIFY_RULES": "<b class=\"assertive\">NO CERTIFICAR</b> una cuenta si piense que :<br/><br/><ul><li>1.) no corresponde a una persona <b>física y viva</b>.<li>2.) su propietario <b>posee una otra cuenta</b> ya certificada.<li>3.) su propietario viola (voluntariamente o no) la regla 1 o 2 (por ejemplo certificando cuentas falsas o en doble).</ul><br/><b>Está usted seguro</b> sin embargo querer certificar esta identidad ?",
    "TRANSFER": "<b>Recapitulativo de la transferencia</b> :<br/><br/><ul><li> - De : {{from}}</li><li> - A : <b>{{to}}</b></li><li> - Importe : <b>{{amount}} {{unit}}</b></li><li> - Comentario : <i>{{comment}}</i></li></ul><br/><b>Está usted seguro querer realizar esta transferencia ?</b>",
    "MEMBERSHIP_OUT": "Esta operación es <b>irreversible</b>.<br/></br/>Está usted seguro querer <b>rescindir su cuenta miembro</b> ?",
    "MEMBERSHIP_OUT_2": "Esta operación es <b>irreversible</b> !<br/><br/>Está usted seguro querer <b>rescindir su adhesión</b> como miembro ?",
    "LOGIN_UNUSED_WALLET_TITLE": "Fracaso de introducción de datos ?",
    "LOGIN_UNUSED_WALLET": "Está usted conectado a une cuenta que parece <b>inactivo</b>.<br/><br/>Si esta cuenta n corresponde a el suyo, se trata probablemente de un <b>error en la introducción de datos</b> de sus identificadores de conexión.<br/></br/><b>Quiere usted sin embargo continuar con esta cuenta ?</b>",
    "FIX_IDENTITY": "El seudónimo <b>{{uid}}</b> va a estar publicado de nuevo, en reemplazo del antiguo quien caducó.<br/></br/><b>Está usted seguro</b> querer continuar ?",
    "FIX_MEMBERSHIP": "Su solicitud de adhesión como miembro va a estar mandado de nuevo.<br/></br/><b>Está usted seguro</b> querer continuar ?",
    "RENEW_MEMBERSHIP": "Su adhesión como miembro va a estar renovada.<br/></br/><b>Está usted seguro</b> querer continuar ?",
    "REVOKE_IDENTITY": "Va a <b>revocar definitivamente esta identidad</b>.<br/><br/>La llave pública y el seudónimo asociados <b>jamás no podrán estar utilizado</b> (para una cuenta miembro). <br/></br/><b>Está usted seguro</b> querer revocar definitivamente esta cuenta ?",
    "REVOKE_IDENTITY_2": "Esta operación es <b>irreversible</b> !<br/><br/>Está usted seguro querer <b>revocar definitivamente</b> esta cuenta ?",
    "NOT_NEED_RENEW_MEMBERSHIP": "Su adhesión no necesita estar renovada (solo va a caducar en {{membershipExpiresIn|formatDuration}}).<br/></br/><b>Está usted seguro</b> querer renovar su adhesión ?",
    "SAVE_BEFORE_LEAVE": "Quiere usted <b>guardar sus modificaciónes</b> antes dejar la página ?",
    "SAVE_BEFORE_LEAVE_TITLE": "Modificaciónes no registradas",
    "LOGOUT": "Está usted seguro querer desconectarse ?",
    "USE_FALLBACK_NODE": "Nodo <b>{{edad}}</ b> dirección inalcanzable o no válido.<br/><br/>Se desea utilizar temporalmente el nodo <b>{{nuevo}}</b> ?"
  },
  "DOWNLOAD": {
    "POPUP_TITLE": "<b>Revocación del archivo</b>",
    "POPUP_REVOKE_MESSAGE": "Para proteger su cuenta, descargar el <b>documento de revocación cuenta</b>. Usted si es necesario cancelar su cuenta (en caso de robo de cuenta, un cambio de identificador, una cuenta falsa creada, etc.).<br/><br/><b>Por favor, almacenarlo en un lugar seguro.</b>"
  },
  "HELP": {
    "TITLE": "Ayuda en línea",
    "JOIN": {
      "SECTION": "Inscripción",
      "SALT": "El identificador secreto es muy importante. Sirbe a mezclar la contraseña, antes que sirbe a calcular la <span class=\"text-italic\">llave pública</span> de su cuenta (su número) y la llave secreta para acceder a él.<br/><b>Por favor, memorizala mur bien</b>, porque actualmente no existe ninguna manera para encontrarla en caso de pérdida.<br/>Por otra parte, no puede estar modificado sin deber creer una nueva cuenta.<br/><br/>Un buen identificador secreto debe estar suficiente largo (al menos 8 carácteres) y lo más original posible.",
      "PASSWORD": "La contraseña es muy importante. Con el identificador secreto, sirbe a calcular el número (la llave pública) de su cuenta, y la llave secreta para acceder a él.<br/><b>Por favor, memorizala mur bien</b>, porque actualmente no existe ninguna manera para encontrarlo en caso de pérdida (excepto generar un fichero de guarda).<br/>Por otra parte, no puede estar modificado sin deber creer una nueva cuenta.<br/><br/>Una buena contraseña contiene (idealmente) al menos 8 carácteres, del que al menos una mayúscula y una cifra.",
      "PSEUDO": "El seudónimo es solamente utilizado en caso de inscripción como <span class=\"text-italic\">miembro</span>. Siempre es asociado a un monedero (vía su <span class=\"text-italic\">llave pública</span>).<br/>Es públicado en la red, para que los otros usuarios puedan identificarlo, certificarlo o mandar moneda en la cuenta.<br/>Un seudónimo debe estar único dentro de los miembros (<u>actuales</u> y antiguos)."
    },
    "GLOSSARY": {
      "SECTION": "Glosario",
      "PUBKEY_DEF": "Una llave pública identifica un monedero. Puede identificar un miembro. En Cesium se calcula con el identificador y la contraseña secreta.",
      "MEMBER": "Miembro",
      "MEMBER_DEF": "Un miembro es una persona humana física y viva, deseosa de participar libremente a la comunidad monetaria. Percibe un dividendo universal, dependiendo de un período y un importe como definido en las <span class=\"text-italic\">reglas de la moneda</span>",
      "CURRENCY_RULES": "Reglas de la moneda",
      "CURRENCY_RULES_DEF": "Las reglas de la moneda son definido una vez por todas. Fija el funcionamiento de la moneda : el cálculo del dividendo universal, el número de certificacións necesarias para ser miembro, el número de certificaciónes máximo que un miembro puede dar, etc. <a href=\"#/app/currency\">Ver las reglas actuales</a>.<br/>La no modificación de las reglas en el tiempo es posible por el uso de una <span class=\"text-italic\">BlockChain</span> que sostiene et ejecuta estas reglas, y verifica continuamente sus buena aplicación.",
      "BLOCKCHAIN": "Cadena de bloques (<span class=\"text-italic\">Blockchain</span>)",
      "BLOCKCHAIN_DEF": "La BlockChain es un sistema descentralizado, que, por el caso de Duniter, sirve a sostener y ejecutar las <span class=\"text-italic\">reglas de la moneda</span>.<br/><a href=\"http://duniter.org\" target=\"_system\">Saber más a proposito de Duniter</a> y el funcionamiento de su Blockchain.",
      "UNIVERSAL_DIVIDEND_DEF": "El Dividendo Universal (DU) es la cantidad de moneda co-creída por cada uno miembro, dependiendo del período y del cálculo como definido en las <span class=\"text-italic\">reglas de la moneda</span>.<br/>A cada plazo, los miembros reciben en sus cuentas la misma cantidad de moneda nueva.<br/><br/>El DU sube un crecimiento regular, para quedar justo entre los miembros (actuales y futuros), calculado en función de la esperanza de vida media, como demostrado en la Théorie Relative de la Monnaie (TRM) = Teoría Relativa de la Moneda, ya no está traducida en español, contactarnos para contribuir a su traducción.<br/><a href=\"http://trm.creationmonetaire.info\">Saber más a propósito de la TRM</a> y las monedas libres."
    },
    "TIP": {
      "MENU_BTN_CURRENCY": "El menú <b>{{'MENU.CURRENCY'|translate}}</b> permite la consulta de las <b>reglas de la moneda</b> et de su estado.",
      "CURRENCY_WOT": "El <b>número de miembros</b> demostra la importancia de la comunidad y permite de <b>seguir su evolución</b>.",
      "CURRENCY_MASS": "Sigue aquí la <b>cantidad total de moneda</b> existente y su <b>distribución mediana</b> por miembro.<br/><br/>Este permite a juzgar la<b>importancia de un importe</b>, en relación con lo que <b>poseen los otros</b> en sus cuentas (como media).",
      "CURRENCY_UNIT_RELATIVE": "La unidad utilizada (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) significa que los importes en {{currency|capitalize}} son divido por el <b>Dividendo Universal</b> (DU).<br/><br/><small> Esta unidad relativa es <b>pertinente</b>, porque estable a pesar de la cantidad de moneda que aumenta en permanencia.</small>",
      "CURRENCY_CHANGE_UNIT": "Este botón permite de <b>cambiar la unidad</b>, para visualizar los importes <b>directamente en {{currency|capitalize}}</b> (más bien que en &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;).",
      "CURRENCY_CHANGE_UNIT_TO_RELATIVE": "Este botón permite de <b>cambiar la unidad</b>, para visualizar los importes en &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;, es decir relativamente al Dividendo Universal (el importe co-producto por cada uno miembro).",
      "CURRENCY_RULES": "Las <b>reglas</b> de la moneda fijan su funcionamiento <b>exacto y previsible</b>.<br/><br/>Verdadero ADN de la moneda, hacen su código monetario <b>legible y transparente</b>.",
      "MENU_BTN_NETWORK": "El menú <b>{{'MENU.NETWORK'|translate}}</b> permite la consulta del estato de la red.",
      "NETWORK_BLOCKCHAIN": "Todas las operaciónes de la moneda están registradas dentro un grande libro de cuenta <b>público y infalsificable</b>, también llamado <b>cadena de bloques</b> (<em>BlockChain</em> en inglés).",
      "NETWORK_PEERS": "Los <b>nodos</b> visibles aquí corresponden a los <b>ordenadores que actualizan y controlan</b> la cadena de bloques.<br/><br/>Lo más hay nodos, más la moneda tiene una gestión <b>descentralizada</b> y digna de confianza.",
      "NETWORK_PEERS_BLOCK_NUMBER": "Este <b>número</b> (en verde) indica el <b>último bloque validado</b> por este nodo (última pagina escrita dentro el grande libro de cuentas).<br/><br/>El color verde indica que este bloque es también validado por <b>la mayoría de los otros nodos</b>.",
      "NETWORK_PEERS_PARTICIPATE": "<b>Cada miembro</b>, equipado de un ordenador con internet, <b>puede participar añando un nodo</b>. Necesito <b>instalar el programa Duniter</b> (libre y gratis). <a href=\"{{installDocUrl}}\" target=\"_system\">Ver el manual de uso &gt;&gt;</a>.",
      "MENU_BTN_ACCOUNT": "El menú <b>{{'ACCOUNT.TITLE'|translate}}</b> permite acceder a la gestión de su cuenta.",
      "MENU_BTN_ACCOUNT_MEMBER": "Consulta aquí el estado de su cuenta y las informaciónes sobre sus certificaciónes.",
      "WALLET_CERTIFICATIONS": "Hace un clic aquí para consultar el detalle de sus certificaciónes (recibidas y emitidas).",
      "WALLET_RECEIVED_CERTIFICATIONS": "Hace un clic aquí para consultar el detalle de sus <b>certificaciónes recibidas</b>.",
      "WALLET_GIVEN_CERTIFICATIONS": "Hace un clic aquí para consultar el detalle de sus <b>certificaciónes emitidas</b>.",
      "WALLET_BALANCE": "El <b>sueldo</b> de su cuenta se visualiza aquí.",
      "WALLET_BALANCE_RELATIVE": "{{'HELP.TIP.WALLET_BALANCE'|translate}}<br/><br/>L'unité utilisée (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) significa que el importe en {{currency|capitalize}} fue dividido por el <b>Dividendo Universal</b> (DU) co-creído por cada uno miembro.<br/><br/>Actualmente 1 DU vale {{currentUD|formatInteger}} {{currency|capitalize}}s.",
      "WALLET_BALANCE_CHANGE_UNIT": "Podrá <b>cambiar la unidad</b> de visualización de los importes en los <b><i class=\"icon ion-android-settings\"></i>&nbsp;{{'MENU.SETTINGS'|translate}}</b>.<br/><br/>Por ejemplo, para visualizar los importes <b>directamente en {{currency|capitalize}}</b>, más bien que en unidad relativa.",
      "WALLET_PUBKEY": "Este es la llave pública de su cuenta. Puede comunicarla a un tercero con el fin de que identifique más simplemente su cuenta.",
      "WALLET_SEND": "Realizar un pago en algunos clics",
      "WALLET_SEND_NO_MONEY": "Realizar un pago en algunos clics.<br/>(Su sueldo ya no lo permite)",
      "WALLET_OPTIONS": "Este botón permite acceder a las <b>acciónes de adhesión</b> y de seguridad.<br/><br/>No olvida echar un vistazo !",
      "WALLET_RECEIVED_CERTS": "Se exhibirá aquí la lista de las personas que le han certificado.",
      "WALLET_CERTIFY": "El botón <b>{{'WOT.BTN_SELECT_AND_CERTIFY'|translate}}</b> permite seleccionar una identidad y certificarla.<br/><br/>Sólo usuarios <b>ya miembros</b> pueden certificar otros.",
      "WALLET_CERT_STOCK": "Su stock de certificaciónes (emitidas) es limitado a <b>{{sigStock}} certificaciónes</b>.<br/><br/>Este stock se renueva con el tiempo, a medida que las certificaciónes se invalidan.",
      "MENU_BTN_TX_MEMBER": "El menú <b>{{'MENU.TRANSACTIONS'|translate}}</b> permite consultar su sueldo, el historial de sus transacciónes y mandar un pago.",
      "MENU_BTN_TX": "Consultar aquí <b>el historial de sus transacciónes</b> y efectuar nuevas operaciónes.",
      "MENU_BTN_WOT": "El menú <b>{{'MENU.WOT'|translate}}</b> permite buscar entre los <b>usuarios</b> de la moneda (miembro o no).",
      "WOT_SEARCH_TEXT_XS": "Para buscar en el anuario, toca las <b>primeras letras de un seudónimo</b> (o de una llave pública).<br/><br/>La búsqueda se iniciará automáticamente.",
      "WOT_SEARCH_TEXT": "Para buscar en el anuario, toca las <b>primeras letras de un seudónimo</b> (o de una llave pública). <br/><br/>Luego, apoya en <b>Entrada</b> para iniciar la búsqueda.",
      "WOT_SEARCH_RESULT": "Visualisa la ficha detallada simplemente <b>haciendo un clic</b> sobre una línea.",
      "WOT_VIEW_CERTIFICATIONS": "La línea <b>{{'ACCOUNT.CERTIFICATION_COUNT'|translate}}</b> demostra cuántos miembros han validado esta identidad.<br/><br/>Estas certificaciónes atestiguan que la cuenta pertenece a <b>una persona humana viva</b> que no tenga <b>ningúna otra cuenta miembro</b>.",
      "WOT_VIEW_CERTIFICATIONS_COUNT": "Necesita al menos <b>{{sigQty}} certificaciónes</b> para estar miembro y recibir el <b>Dividendo Universal</b>.",
      "WOT_VIEW_CERTIFICATIONS_CLICK": "Un clic aquí permite abrir <b>la lista de todas las certificaciónes</b> de la identidad (recibidas y emitidas).",
      "WOT_VIEW_CERTIFY": "El botón <b>{{'WOT.BTN_CERTIFY'|translate}}</b> permite añadir su certificación a esta identidad.",
      "CERTIFY_RULES": "<b>Atención :</b> Certificar solamente <b>personas físicas vivas</b>, que no posean ningúna otra cuenta miembro.<br/><br/>La seguridad de la moneda depende de la vigilancia de cada uno !",
      "MENU_BTN_SETTINGS": "Los <b>{{'MENU.SETTINGS'|translate}}</b> él permitirán configurar la aplicación.",
      "HEADER_BAR_BTN_PROFILE": "Hace un clic aquí para acceder a su <b>perfil de usuario</b>",
      "SETTINGS_CHANGE_UNIT": "Podrá <b>cambiar la unidad de visualización</b> de los importes haciendo un clic más arriba.<br/><br/>- Desactiva la opción por una visualización de los importes en {{currency|capitalize}}.<br/>- Activa la opción por una visualización relativa en {{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub> (todos los importes serán <b>dividido</b> por el Dividendo Universal corriente).",
      "END_LOGIN": "Esta Visita Guiada es <b>terminada</b> !<br/><br/>Buena continuación en este mundo nuevo de la <b>economia libre</b> !",
      "END_NOT_LOGIN": "Esta Visita Guiada es <b>terminada</b> !<br/><br/>Si quiere utilizar la moneda {{currency|capitalize}}, tiene que hacer un clic sobre <b>{{'LOGIN.CREATE_ACCOUNT'|translate}}</b> más abajo."
    }
  }
}
);

$translateProvider.translations("fr-FR", {
  "COMMON": {
    "APP_NAME": "Cesium",
    "APP_VERSION": "v{{version}}",
    "APP_BUILD": "date : {{build}}",
    "PUBKEY": "Clé publique",
    "MEMBER": "Membre",
    "BLOCK" : "Bloc",
    "BTN_OK": "OK",
    "BTN_YES": "Oui",
    "BTN_NO": "Non",
    "BTN_SEND": "Envoyer",
    "BTN_SEND_MONEY": "Faire un virement",
    "BTN_SEND_MONEY_SHORT": "Virement",
    "BTN_SAVE": "Enregistrer",
    "BTN_YES_SAVE": "Oui, Enregistrer",
    "BTN_YES_CONTINUE": "Oui, Continuer",
    "BTN_SHOW": "Voir",
    "BTN_SHOW_PUBKEY": "Afficher la clé publique",
    "BTN_RELATIVE_UNIT": "Afficher en unité relative ?",
    "BTN_BACK": "Retour",
    "BTN_NEXT": "Suivant",
    "BTN_CANCEL": "Annuler",
    "BTN_CLOSE": "Fermer",
    "BTN_LATER": "Plus tard",
    "BTN_LOGIN": "Se connecter",
    "BTN_LOGOUT": "Déconnexion",
    "BTN_ADD_ACCOUNT": "Nouveau compte",
    "BTN_SHARE": "Partager",
    "BTN_EDIT": "Modifier",
    "BTN_DELETE": "Supprimer",
    "BTN_ADD": "Ajouter",
    "BTN_SEARCH": "Rechercher",
    "BTN_REFRESH": "Actualiser",
    "BTN_RETRY": "Recommencer",
    "BTN_START": "Commencer",
    "BTN_CONTINUE": "Continuer",
    "BTN_CREATE": "Créer",
    "BTN_UNDERSTOOD": "J'ai compris",
    "BTN_OPTIONS": "Options",
    "BTN_HELP_TOUR": "Visite guidée",
    "BTN_HELP_TOUR_SCREEN": "Découvrir cet écran",
    "BTN_DOWNLOAD": "Télécharger",
    "BTN_DOWNLOAD_ACCOUNT_STATEMENT": "Télécharger le relevé du compte",
    "BTN_MODIFY": "Modifier",
    "CHOOSE_FILE": "Déposez votre fichier <br/>ou cliquez pour le sélectionner",
    "DAYS": "jours",
    "NO_ACCOUNT_QUESTION": "Pas de encore membre ? Créer un compte !",
    "SEARCH_NO_RESULT": "Aucun résultat trouvé",
    "LOADING": "Veuillez patienter...",
    "SEARCHING": "Recherche en cours...",
    "FROM": "De",
    "TO": "À",
    "COPY": "Copier",
    "LANGUAGE": "Langue",
    "UNIVERSAL_DIVIDEND": "Dividende universel",
    "UD": "DU",
    "DATE_PATTERN": "DD/MM/YY HH:mm",
    "DATE_FILE_PATTERN": "YYYY-MM-DD",
    "DATE_SHORT_PATTERN": "DD/MM/YY",
    "DATE_MONTH_YEAR_PATTERN": "MM/YYYY",
    "EMPTY_PARENTHESIS": "(vide)",
    "UID": "Pseudonyme",
    "ENABLE": "Activé",
    "DISABLE": "Désactivé",
    "RESULTS_LIST": "Résultats",
    "RESULTS_COUNT": "{{count}} résultats",
    "EXECUTION_TIME": "executé en {{duration|formatDurationMs}}",
    "SHOW_VALUES": "Afficher les valeurs en clair ?",
    "POPOVER_ACTIONS_TITLE": "Options",
    "POPOVER_FILTER_TITLE": "Filtres",
    "SHOW_MORE": "Afficher plus",
    "SHOW_MORE_COUNT": "(limite actuelle à {{limit}})",
    "POPOVER_SHARE": {
      "TITLE": "Partager",
      "SHARE_ON_TWITTER": "Partager sur Twitter",
      "SHARE_ON_FACEBOOK": "Partager sur Facebook",
      "SHARE_ON_DIASPORA": "Partager sur Diaspora*",
      "SHARE_ON_GOOGLEPLUS": "Partager sur Google+"
    }
  },
  "SYSTEM": {
    "PICTURE_CHOOSE_TYPE": "Choisir la source :",
    "BTN_PICTURE_GALLERY": "Galerie",
    "BTN_PICTURE_CAMERA": "<b>Caméra</b>"
  },
  "MENU": {
    "HOME": "Accueil",
    "WOT": "Annuaire",
    "CURRENCY": "Monnaie",
    "ACCOUNT": "Mon compte",
    "TRANSFER": "Virement",
    "SCAN": "Scanner",
    "SETTINGS": "Paramètres",
    "NETWORK": "Réseau",
    "TRANSACTIONS": "Mes opérations"
  },
  "ABOUT": {
    "TITLE": "À propos",
    "LICENSE": "Application <b>libre</b> (licence GNU GPLv3).",
    "LATEST_RELEASE": "Il existe une <b>version plus récente</b> de {{'COMMON.APP_NAME'|translate}} (<b>v{{version}}</b>)",
    "PLEASE_UPDATE": "Veuillez mettre à jour {{'COMMON.APP_NAME'|translate}} (dernière version : <b>v{{version}}</b>)",
    "CODE": "Code source :",
    "DEVELOPERS": "Développé par :",
    "FORUM": "Forum :",
    "PLEASE_REPORT_ISSUE": "N'hésitez pas à nous remonter les anomalies rencontrées",
    "REPORT_ISSUE": "Remonter un problème"
  },
  "HOME": {
    "TITLE": "Cesium",
    "WELCOME": "Bienvenue dans l'application Cesium !",
    "MESSAGE": "Suivez vos comptes {{currency|abbreviate}} en toute simplicité",
    "BTN_CURRENCY": "Explorer la monnaie {{name|abbreviate}}",
    "BTN_ABOUT": "à propos",
    "BTN_HELP": "Aide en ligne",
    "REPORT_ISSUE": "anomalie",
    "NOT_YOUR_ACCOUNT_QUESTION" : "Vous n'êtes pas propriétaire du compte <b><i class=\"ion-key\"></i> {{pubkey|formatPubkey}}</b> ?",
    "BTN_CHANGE_ACCOUNT": "Déconnecter ce compte",
    "CONNECTION_ERROR": "Nœud <b>{{server}}</b> injoignable ou adresse invalide.<br/><br/>Vérifiez votre connection Internet, ou changer de nœud <a class=\"positive\" ng-click=\"doQuickFix('settings')\">dans les paramètres</a>."
  },
  "SETTINGS": {
    "TITLE": "Paramètres",
    "NETWORK_SETTINGS": "Réseau",
    "PEER": "Adresse du nœud Duniter",
    "PEER_CHANGED_TEMPORARY": "Adresse utilisée temporairement",
    "USE_LOCAL_STORAGE": "Activer le stockage local",
    "USE_LOCAL_STORAGE_HELP": "Permet de sauvegarder vos paramètres",
    "ENABLE_HELPTIP": "Activer les bulles d'aide contextuelles",
    "ENABLE_UI_EFFECTS": "Activer les effets visuels",
    "HISTORY_SETTINGS": "Liste des opérations",
    "DISPLAY_UD_HISTORY": "Afficher les dividendes produits ?",
    "AUTHENTICATION_SETTINGS": "Authentification",
    "KEEP_AUTH": "Expiration de l'authentification",
    "KEEP_AUTH_HELP": "Défini le moment où l'authentification est nettoyée de la mémoire",
    "KEEP_AUTH_OPTION": {
      "NEVER": "Après chaque opération",
      "SECONDS": "Après {{value}}s d'inactivité",
      "MINUTE": "Après {{value}}min d'inactivité",
      "MINUTES": "Après {{value}}min d'inactivité",
      "HOUR": "Après {{value}}h d'inactivité",
      "ALWAYS": "A la fin de la session"
    },
    "REMEMBER_ME": "Se souvenir de moi ?",
    "REMEMBER_ME_HELP": "Permet de rester identifié d'une session à l'autre, en conservant localement la clé publique.",
    "PLUGINS_SETTINGS": "Extensions",
    "BTN_RESET": "Restaurer les valeurs par défaut",
    "EXPERT_MODE": "Activer le mode expert",
    "EXPERT_MODE_HELP": "Permet un affichage plus détaillé",
    "POPUP_PEER": {
      "TITLE" : "Nœud Duniter",
      "HOST" : "Adresse",
      "HOST_HELP": "Adresse : serveur:port",
      "USE_SSL" : "Sécurisé ?",
      "USE_SSL_HELP" : "(Chiffrement SSL)",
      "BTN_SHOW_LIST" : "Liste des noeuds"
    }
  },
  "BLOCKCHAIN": {
    "HASH": "Hash : {{hash}}",
    "VIEW": {
      "HEADER_TITLE": "Bloc #{{number}}-{{hash|formatHash}}",
      "TITLE_CURRENT": "Bloc courant",
      "TITLE": "Bloc #{{number|formatInteger}}",
      "COMPUTED_BY": "Calculé par le noeud de",
      "SHOW_RAW": "Voir le fichier brut",
      "TECHNICAL_DIVIDER": "Informations techniques",
      "VERSION": "Version du format",
      "HASH": "Hash calculé",
      "UNIVERSAL_DIVIDEND_HELP": "Monnaie co-produite par chacun des {{membersCount}} membres",
      "EMPTY": "Aucune donnée dans ce bloc",
      "POW_MIN": "Difficulté minimale",
      "POW_MIN_HELP": "Difficulté imposée pour le calcul du hash",
      "DATA_DIVIDER": "Données",
      "IDENTITIES_COUNT": "Nouvelles identités",
      "JOINERS_COUNT": "Nouveaux membres",
      "ACTIVES_COUNT": "Renouvellements",
      "ACTIVES_COUNT_HELP": "Membres ayant renouvellés leur adhésion",
      "LEAVERS_COUNT": "Membres sortants",
      "LEAVERS_COUNT_HELP": "Membres ne souhaitant plus de certification",
      "EXCLUDED_COUNT": "Membres exclus",
      "EXCLUDED_COUNT_HELP": "Anciens membres exclus par non renouvellement ou manque de certifications",
      "REVOKED_COUNT": "Identités revoquées",
      "REVOKED_COUNT_HELP": "Ces comptes ne pourront plus être membres",
      "TX_COUNT": "Transactions",
      "CERT_COUNT": "Certifications",
      "TX_TO_HIMSELF": "Opération de change",
      "TX_OUTPUT_UNLOCK_CONDITIONS": "Conditions de déblocage",
      "TX_OUTPUT_OPERATOR": {
        "AND": "et",
        "OR": "ou"
      },
      "TX_OUTPUT_FUNCTION": {
        "SIG": "<b>Signature</b> de ",
        "XHX": "<b>Mot de passe</b>, dont SHA256 =",
        "CSV": "Bloqué pendant",
        "CLTV": "Bloqué jusqu'à"
      }
    },
    "LOOKUP": {
      "TITLE": "Blocs",
      "NO_BLOCK": "Aucun bloc",
      "LAST_BLOCKS": "Derniers blocs :",
      "BTN_COMPACT": "Compacter"
    }
  },
  "CURRENCY": {
    "VIEW": {
      "TITLE": "Monnaie",
      "TAB_CURRENCY": "Monnaie",
      "TAB_WOT": "Toile de confiance",
      "TAB_NETWORK": "Réseau",
      "TAB_BLOCKS": "Blocs",
      "CURRENCY_SHORT_DESCRIPTION": "{{currency|abbreviate}} est une <b>monnaie libre</b>, démarrée {{firstBlockTime|formatFromNow}}. Elle compte actuellement <b>{{N}} membres</b>, qui produisent et perçoivent un <a ng-click=\"showHelpModal('ud')\">Dividende Universel</a> (DU), chaque {{dt|formatPeriod}}.",
      "NETWORK_RULES_DIVIDER": "Règles du réseau",
      "CURRENCY_NAME": "Nom de la monnaie",
      "MEMBERS": "Nombre de membres",
      "MEMBERS_VARIATION": "Variation depuis le dernier DU",
      "MONEY_DIVIDER": "Monnaie",
      "MASS": "Masse monétaire",
      "SHARE": "Masse par membre",
      "UD": "Dividende universel",
      "C_ACTUAL": "Croissance actuelle",
      "MEDIAN_TIME": "Heure de la blockchain",
      "POW_MIN": "Niveau minimal de difficulté de calcul",
      "MONEY_RULES_DIVIDER": "Règles de la monnaie",
      "C_RULE": "Croissance théorique cible",
      "UD_RULE": "Calcul du dividende universel",
      "DT_REEVAL": "Période de revalorisation du DU",
      "REEVAL_SYMBOL": "reval",
      "DT_REEVAL_VALUE": "Tous les <b>{{dtReeval|formatDuration}}</b> ({{dtReeval/86400}} {{'COMMON.DAYS'|translate}})",
      "UD_REEVAL_TIME0": "Date de la 1ère revalorisation",
      "SIG_QTY_RULE": "Nombre de certifications requises pour devenir membre",
      "SIG_STOCK": "Nombre maximal de certifications émises par membre",
      "SIG_PERIOD": "Délai minimal d'attente entre 2 certifications successives émises par une même personne",
      "SIG_WINDOW": "Délai limite de prise en compte d'une certification",
      "SIG_VALIDITY": "Durée de vie d'une certification qui a été prise en compte",
      "MS_WINDOW": "Délai limite de prise en compte d'une demande d'adhésion comme membre",
      "MS_VALIDITY": "Durée de vie d'une adhésion qui a été prise en compte",
      "STEP_MAX": "Distance maximale, par les certifications, entre un nouvel entrant et les membres référents",
      "WOT_RULES_DIVIDER": "Règles de la toile de confiance",
      "SENTRIES": "Nombre de certifications (émises <b>et</b> reçues) pour devenir membre référent",
      "SENTRIES_FORMULA": "Nombre de certification (émises <b>et</b> reçues) pour devenir membre référent (formule)",
      "XPERCENT":"Pourcentage minimum de membres référents à atteindre pour respecter la règle de distance",
      "AVG_GEN_TIME": "Temps moyen entre deux blocs",
      "CURRENT": "actuel",
      "MATH_CEILING": "PLAFOND",
      "DISPLAY_ALL_RULES": "Afficher toutes les règles ?",
      "BTN_SHOW_LICENSE": "Voir la licence",
      "WOT_DIVIDER": "Toile de confiance"
    },
    "LICENSE": {
      "TITLE": "Licence de la monnaie",
      "BTN_DOWNLOAD": "Télécharger le fichier",
      "NO_LICENSE_FILE": "Fichier de licence non trouvé."
    }
  },
  "NETWORK": {
    "VIEW": {
      "MEDIAN_TIME": "Heure de la blockchain",
      "LOADING_PEERS": "Chargement des noeuds...",
      "NODE_ADDRESS": "Adresse :",
      "SOFTWARE": "Logiciel",
      "WARN_PRE_RELEASE": "Pré-version (dernière version stable : <b>{{version}}</b>)",
      "WARN_NEW_RELEASE": "Version <b>{{version}}</b> disponible",
      "WS2PID": "Identifiant :",
      "PRIVATE_ACCESS": "Accès privé",
      "POW_PREFIX": "Préfixe de preuve de travail :",
      "ENDPOINTS": {
        "BMAS": "Interface sécurisée (SSL)",
        "BMATOR": "Interface réseau TOR",
        "WS2P": "Interface WS2P",
        "ES_USER_API": "Noeud de données Cesium+"
      }
    },
    "INFO": {
      "ONLY_SSL_PEERS": "Les noeuds non SSL ont un affichage dégradé, car Cesium fonctionne en mode HTTPS."
    }
  },
  "PEER": {
    "PEERS": "Nœuds",
    "SIGNED_ON_BLOCK": "Signé sur le bloc",
    "MIRROR": "miroir",
    "MIRRORS": "Nœuds miroirs",
    "PEER_LIST" : "Liste des nœuds",
    "MEMBERS" : "Nœuds membres",
    "ALL_PEERS" : "Tous les nœuds",
    "DIFFICULTY" : "Difficulté",
    "API" : "API",
    "CURRENT_BLOCK" : "Bloc #",
    "POPOVER_FILTER_TITLE": "Filtre",
    "OFFLINE": "Nœuds hors ligne",
    "BTN_SHOW_PEER": "Voir le nœud",
    "VIEW": {
      "TITLE": "Nœud",
      "OWNER": "Appartient à",
      "SHOW_RAW_PEERING": "Voir la fiche de pair",
      "SHOW_RAW_CURRENT_BLOCK": "Voir le dernier bloc (format brut)",
      "LAST_BLOCKS": "Derniers blocs connus",
      "KNOWN_PEERS": "Nœuds connus :",
      "GENERAL_DIVIDER": "Informations générales",
      "ERROR": {
        "LOADING_TOR_NODE_ERROR": "Récupération des informations du noeud impossible. Le délai d'attente est dépassé.",
        "LOADING_NODE_ERROR": "Récupération des informations du noeud impossible"
      }
    }
  },
  "WOT": {
    "SEARCH_HELP": "Recherche (pseudo ou clé publique)",
    "SEARCH_INIT_PHASE_WARNING": "Durant la phase de pré-inscription, la recherche des inscriptions en attente <b>peut-être longue</b>. Merci de patienter...",
    "REGISTERED_SINCE": "Inscrit le",
    "REGISTERED_SINCE_BLOCK": "Inscrit au block #",
    "NO_CERTIFICATION": "Aucune certification validée",
    "NO_GIVEN_CERTIFICATION": "Aucune certification émise",
    "NOT_MEMBER_PARENTHESIS": "(non membre)",
    "IDENTITY_REVOKED_PARENTHESIS": "(identité revoquée)",
    "MEMBER_PENDING_REVOCATION_PARENTHESIS": "(en cours de révocation)",
    "EXPIRE_IN": "Expiration",
    "NOT_WRITTEN_EXPIRE_IN": "Date limite<br/>de traitement",
    "EXPIRED": "Expiré",
    "PSEUDO": "Pseudonyme",
    "SIGNED_ON_BLOCK": "Emise au bloc #{{block}}",
    "WRITTEN_ON_BLOCK": "Ecrite au bloc #{{block}}",
    "GENERAL_DIVIDER": "Informations générales",
    "NOT_MEMBER_ACCOUNT": "Compte simple (non membre)",
    "NOT_MEMBER_ACCOUNT_HELP": "Il s'agit d'un simple portefeuille, sans demande d'adhésion en attente.",
    "TECHNICAL_DIVIDER": "Informations techniques",
    "BTN_CERTIFY": "Certifier",
    "BTN_YES_CERTIFY": "Oui, certifier",
    "BTN_SELECT_AND_CERTIFY": "Nouvelle certification",
    "ACCOUNT_OPERATIONS": "Opérations sur le compte",
    "VIEW": {
      "POPOVER_SHARE_TITLE": "Identité {{title}}"
    },
    "LOOKUP": {
      "TITLE": "Toile de confiance",
      "NEWCOMERS": "Nouveaux membres",
      "NEWCOMERS_COUNT": "{{count}} membres",
      "PENDING": "Inscriptions en attente",
      "PENDING_COUNT": "{{count}} inscriptions en attente",
      "REGISTERED": "Inscrit {{sigDate | formatFromNow}}",
      "MEMBER_FROM": "Membre depuis {{memberDate|formatFromNowShort}}",
      "BTN_NEWCOMERS": "Nouveaux membres",
      "BTN_PENDING": "Inscriptions en attente",
      "SHOW_MORE": "Afficher plus",
      "SHOW_MORE_COUNT": "(limite actuelle à {{limit}})",
      "NO_PENDING": "Aucune inscription en attente.",
      "NO_NEWCOMERS": "Aucun membre."
    },
    "CONTACTS": {
      "TITLE": "Contacts"
    },
    "MODAL": {
      "TITLE": "Recherche"
    },
    "CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certifications",
      "SUMMARY": "Certifications reçues",
      "LIST": "Détail des certifications reçues",
      "PENDING_LIST": "Certifications en attente de traitement",
      "RECEIVED": "Certifications reçues",
      "RECEIVED_BY": "Certifications reçues par {{uid}}",
      "ERROR": "Certifications reçues en erreur",
      "SENTRY_MEMBER": "Membre référent"
    },
    "OPERATIONS": {
      "TITLE": "{{uid}} - Opérations"
    },
    "GIVEN_CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certifications émises",
      "SUMMARY": "Certifications émises",
      "LIST": "Détail des certifications émises",
      "PENDING_LIST": "Certifications en attente de traitement",
      "SENT": "Certifications émises",
      "SENT_BY": "Certifications émises par {{uid}}",
      "ERROR": "Certifications émises en erreur"
    }
  },
  "LOGIN": {
    "TITLE": "<i class=\"icon ion-log-in\"></i> Connexion",
    "SCRYPT_FORM_HELP": "Veuillez saisir vos identifiants.<br>Pensez à vérifier que la clé publique est celle de votre compte.",
    "PUBKEY_FORM_HELP": "Veuillez saisir une clé publique de compte :",
    "FILE_FORM_HELP": "Choisissez le fichier de trousseau à utiliser :",
    "SALT": "Identifiant secret",
    "SALT_HELP": "Identifiant secret",
    "SHOW_SALT": "Afficher l'identifiant secret ?",
    "PASSWORD": "Mot de passe",
    "PASSWORD_HELP": "Mot de passe",
    "PUBKEY_HELP": "Exemple : « AbsxSY4qoZRzyV2irfep1V9xw1EMNyKJw2TkuVD4N1mv »",
    "NO_ACCOUNT_QUESTION": "Vous n'avez pas encore de compte ?",
    "CREATE_ACCOUNT": "Créer un compte...",
    "FORGOTTEN_ID": "Mot de passe oublié ?",
    "ASSOCIATED_PUBKEY": "Clé publique du trousseau :",
    "BTN_METHODS": "Autres méthodes",
    "BTN_METHODS_DOTS": "Changer de méthode...",
    "METHOD_POPOVER_TITLE": "Méthodes",
    "MEMORIZE_AUTH_FILE": "Mémoriser ce trousseau le temps de la session de navigation",
    "SCRYPT_PARAMETERS": "Paramètres (Scrypt) :",
    "AUTO_LOGOUT": {
      "TITLE": "Information",
      "MESSAGE": "<i class=\"ion-android-time\"></i> Vous avez été <b>déconnecté</b> automatiquement, suite à une inactivité prolongée.",
      "BTN_RELOGIN": "Me reconnecter",
      "IDLE_WARNING": "Vous allez être déconnecté... {{countdown}}"
    },
    "METHOD": {
      "SCRYPT_DEFAULT": "Sallage standard (par défaut)",
      "SCRYPT_ADVANCED": "Sallage avancé",
      "FILE": "Fichier de trousseau",
      "PUBKEY": "Clé publique seule"
    },
    "SCRYPT": {
      "SIMPLE": "Sallage léger",
      "DEFAULT": "Sallage standard",
      "SECURE": "Sallage sûr",
      "HARDEST": "Sallage le plus sûr",
      "EXTREME": "Sallage extrème",
      "USER": "Sallage personnalisé",
      "N": "N (Loop):",
      "r": "r (RAM):",
      "p": "p (CPU):"
    },
    "FILE": {
      "DATE" : "Date :",
      "TYPE" : "Type :",
      "SIZE": "Taille :",
      "VALIDATING": "Validation...",
      "HELP": "Format de fichier attendu : <b>.dunikey</b> (type PubSec). D'autres formats sont en cours de développement (EWIF, WIF)."
    }
  },
  "AUTH": {
    "TITLE": "<i class=\"icon ion-locked\"></i> Authentification",
    "METHOD_LABEL": "Méthode d'authentification",
    "BTN_AUTH": "S'authentifier",
    "SCRYPT_FORM_HELP": "Veuillez vous authentifier :"
  },
  "ACCOUNT": {
    "TITLE": "Mon compte",
    "BALANCE": "Solde",
    "LAST_TX": "Dernières transactions",
    "BALANCE_ACCOUNT": "Solde du compte",
    "NO_TX": "Aucune transaction",
    "SHOW_MORE_TX": "Afficher plus",
    "SHOW_ALL_TX": "Afficher tout",
    "TX_FROM_DATE": "(limite actuelle à {{fromTime|formatFromNowShort}})",
    "PENDING_TX": "Transactions en cours de traitement",
    "ERROR_TX": "Transactions non executées",
    "ERROR_TX_SENT": "Transactions envoyées en échec",
    "PENDING_TX_RECEIVED": "Transactions en attente de réception",
    "EVENTS": "Evénements",
    "WAITING_MEMBERSHIP": "Demande d'adhésion envoyée. En attente d'acceptation.",
    "WAITING_CERTIFICATIONS": "Vous devez obtenir {{needCertificationCount}} certification(s) pour devenir membre.",
    "WILL_MISSING_CERTIFICATIONS": "Vous allez bientôt <b>manquer de certification</b> (au moins {{willNeedCertificationCount}} sont requises)",
    "WILL_NEED_RENEW_MEMBERSHIP": "Votre adhésion comme membre <b>va expirer {{membershipExpiresIn|formatDurationTo}}</b>. Pensez à <a ng-click=\"doQuickFix('renew')\">renouveler votre adhésion</a> d'ici là.",
    "NEED_RENEW_MEMBERSHIP": "Vous n'êtes plus membre, car votre adhésion <b>a expiré</b>. Pensez à <a ng-click=\"doQuickFix('renew')\">renouveler votre adhésion</a>.",
    "NO_WAITING_MEMBERSHIP": "Aucune demande d'adhésion en attente. Si vous souhaitez <b>devenir membre</b>, pensez à <a ng-click=\"doQuickFix('membership')\">envoyer la demande d'adhésion</a>.",
    "CERTIFICATION_COUNT": "Certifications reçues",
    "CERTIFICATION_COUNT_SHORT": "Certifications",
    "SIG_STOCK": "Certifications envoyées",
    "BTN_RECEIVE_MONEY": "Encaisser",
    "BTN_SELECT_ALTERNATIVES_IDENTITIES": "Basculer vers une autre identité...",
    "BTN_MEMBERSHIP_RENEW": "Renouveler l'adhésion",
    "BTN_MEMBERSHIP_RENEW_DOTS": "Renouveler l'adhésion...",
    "BTN_MEMBERSHIP_OUT_DOTS": "Arrêter l'adhésion...",
    "BTN_SECURITY_DOTS": "Compte et sécurité...",
    "BTN_SHOW_DETAILS": "Afficher les infos techniques",
    "LOCKED_OUTPUTS_POPOVER": {
      "TITLE": "Montant verrouillé",
      "DESCRIPTION": "Voici les conditions de déverrouillage de ce montant :",
      "DESCRIPTION_MANY": "Cette transaction est composé de plusieurs parties, dont voici les conditions de déverrouillage :",
      "LOCKED_AMOUNT": "Conditions pour le montant :"
    },
    "NEW": {
      "TITLE": "Création de compte",
      "INTRO_WARNING_TIME": "La création d'un compte sur {{name|capitalize}} est très simple. Veuillez néanmoins prendre suffisament de temps pour faire correctement cette formalité (pour ne pas oublier les identifiants, mots de passe, etc.).",
      "INTRO_WARNING_SECURITY": "Vérifier que le matériel que vous utilisez actuellement (ordinateur, tablette, téléphone) <b>est sécurisé et digne de confiance</b>.",
      "INTRO_WARNING_SECURITY_HELP": "Anti-virus à jour, pare-feu activé, session protégée par mot de passe ou code pin, etc.",
      "INTRO_HELP": "Cliquez sur <b>{{'COMMON.BTN_START'|translate}}</b> pour débuter la création de compte. Vous serez guidé étape par étape.",
      "REGISTRATION_NODE": "Votre inscription sera enregistrée via le noeud Duniter <b>{{server}}</b>, qui le diffusera ensuite au reste du réseau de la monnaie.",
      "REGISTRATION_NODE_HELP": "Si vous ne faites pas confiance en ce noeud, veuillez en changer <a ng-click=\"doQuickFix('settings')\">dans les paramètres</a> de Cesium.",
      "SELECT_ACCOUNT_TYPE": "Choisissez le type de compte à créer :",
      "MEMBER_ACCOUNT": "Compte membre",
      "MEMBER_ACCOUNT_TITLE": "Création d'un compte membre",
      "MEMBER_ACCOUNT_HELP": "Si vous n'êtes pas encore inscrit en tant qu'individu (un seul compte possible par individu). Ce compte permet de co-produire la monnaie, en recevant un <b>dividende universel</b> chaque {{parameters.dt|formatPeriod}}.",
      "WALLET_ACCOUNT": "Simple portefeuille",
      "WALLET_ACCOUNT_TITLE": "Création d'un portefeuille",
      "WALLET_ACCOUNT_HELP": "Pour tous les autres cas, par exemple si vous avez besoin d'un compte supplémentaire.<br/>Aucun dividende universel ne sera créé par ce compte.",
      "SALT_WARNING": "Choisissez votre identifiant secret.<br/>Il vous sera demandé à chaque connexion sur ce compte.<br/><br/><b>Retenez le bien</b> : en cas de perte, plus personne ne pourra accéder à votre compte !",
      "PASSWORD_WARNING": "Choisissez un mot de passe.<br/>Il vous sera demandé à chaque connexion sur ce compte.<br/><br/><b>Retenez bien ce mot de passe</b : en cas de perte, plus personne ne pourra accéder à votre compte !",
      "PSEUDO_WARNING": "Choisissez un pseudonyme.<br/>Il sert aux autres membres, pour vous identifier plus facilement.<div class='hidden-xs'><br/>Il <b>ne pourra pas être modifié</b>, sans refaire un compte.</div><br/><br/>Il ne doit contenir <b>ni espace, ni de caractère accentué</b>.<div class='hidden-xs'><br/>Exemple : <span class='gray'>SophieDupond, MarcelChemin, etc.</span>",
      "PSEUDO": "Pseudonyme",
      "PSEUDO_HELP": "Pseudonyme",
      "SALT_CONFIRM": "Confirmation",
      "SALT_CONFIRM_HELP": "Confirmation de l'identifiant secret",
      "PASSWORD_CONFIRM": "Confirmation",
      "PASSWORD_CONFIRM_HELP": "Confirmation du mot de passe",
      "SLIDE_6_TITLE": "Confirmation :",
      "COMPUTING_PUBKEY": "Calcul en cours...",
      "LAST_SLIDE_CONGRATULATION": "Vous avez saisi toutes les informations nécessaires : Bravo !<br/>Vous pouvez maintenant <b>envoyer la demande de création</b> de compte.</b><br/><br/>Pour information, la clé publique ci-dessous identifiera votre futur compte.<br/>Elle pourra être communiquée à des tiers pour recevoir leur paiement.<br/><b>Il n'est pas obligatoire</b> de la noter ici, vous pourrez également le faire plus tard.",
      "CONFIRMATION_MEMBER_ACCOUNT": "<b class=\"assertive\">Avertissement :</b> l'identifiant secret, le mot de passe et le pseudonyme ne pourront plus être modifiés.<br/><br/><b>Assurez-vous de toujours vous en rappeller !</b><br/><br/><b>Etes-vous sûr</b> de vouloir envoyer cette demande d'inscription ?",
      "CONFIRMATION_WALLET_ACCOUNT": "<b class=\"assertive\">Avertissement :</b> l'identifiant secret et le mot de passe ne pourront plus être modifiés.<br/><br/><b>Assurez-vous de toujours vous en rappeller !</b><br/><br/><b>Etes-vous sûr</b> de vouloir continuer avec ces identifiants ?",
      "CHECKING_PSEUDO": "Vérification...",
      "PSEUDO_AVAILABLE": "Pseudonyme disponible",
      "PSEUDO_NOT_AVAILABLE": "Pseudonyme non disponible",
      "INFO_LICENSE": "Avant de créer un compte membre, <b>veuillez lire et accepter la licence</b> d'usage de la monnaie :",
      "BTN_ACCEPT": "J'accepte",
      "BTN_ACCEPT_LICENSE": "J'accepte la licence"
    },
    "POPUP_REGISTER": {
      "TITLE": "Choisissez un pseudonyme",
      "HELP": "Un pseudonyme est obligatoire pour devenir membre."
    },
    "SELECT_IDENTITY_MODAL": {
      "TITLE": "Sélection de l'identité",
      "HELP": "Plusieurs <b>identités différentes</b> ont été envoyées, pour la clé publique <span class=\"gray\"><i class=\"ion-key\"></i> {{pubkey|formatPubkey}}</span>.<br/>Veuillez sélectionner le dossier à utiliser :"
    },
    "SECURITY": {
      "ADD_QUESTION": "Ajouter une question personnalisée ",
      "BTN_CLEAN": "Vider",
      "BTN_RESET": "Réinitialiser",
      "DOWNLOAD_REVOKE": "Sauvegarder mon fichier de révocation",
      "DOWNLOAD_REVOKE_HELP": "Disposer d'un fichier de révocation est important, par exemple en cas de perte de vos identifiants. Il vous permet de <b>sortir ce compte de la toile de confiance</b>, en redevenant ainsi un simple portefeuille.",
      "MEMBERSHIP_IN": "Transformer en compte membre...",
      "MEMBERSHIP_IN_HELP": "Permet de <b>transformer</b> un compte simple portefeuille <b>en compte membre</b>, en envoyant une demande d'adhésion. Utile uniquement si vous n'avez pas déjà une autre compte membre.",
      "SEND_IDENTITY": "Publier son identité...",
      "SEND_IDENTITY_HELP": "Permet d'associer un pseudonyme à ce compte, mais <b>sans faire de demande d'adhésion</b> pour devenir membre. Cette association n'est généralement pas utile, car la validité de cette association de pseudonyme est limitée dans le temps.",
      "HELP_LEVEL": "Pour générer un fichier de sauvegarde de vos identifiants, choisissez <strong> au moins {{nb}} questions :</strong>",
      "LEVEL": "Niveau de sécurité",
      "LOW_LEVEL": "Faible <span class=\"hidden-xs\">(2 questions minimum)</span>",
      "MEDIUM_LEVEL": "Moyen <span class=\"hidden-xs\">(4 questions minimum)</span>",
      "QUESTION_1": "Comment s'appelait votre meilleur ami lorsque vous étiez adolescent ?",
      "QUESTION_2": "Comment s'appelait votre premier animal de compagnie ?",
      "QUESTION_3": "Quel est le premier plat que vous avez appris à cuisiner ?",
      "QUESTION_4": "Quel est le premier film que vous avez vu au cinéma ?",
      "QUESTION_5": "Où êtes-vous allé la première fois que vous avez pris l'avion ?",
      "QUESTION_6": "Comment s'appelait votre instituteur préféré à l'école primaire ?",
      "QUESTION_7": "Quel serait selon vous le métier idéal ?",
      "QUESTION_8": "Quel est le livre pour enfants que vous préférez ?",
      "QUESTION_9": "Quel était le modèle de votre premier véhicule ?",
      "QUESTION_10": "Quel était votre surnom lorsque vous étiez enfant ?",
      "QUESTION_11": "Quel était votre personnage ou acteur de cinéma préféré lorsque vous étiez étudiant ?",
      "QUESTION_12": "Quel était votre chanteur ou groupe préféré lorsque vous étiez étudiant ?",
      "QUESTION_13": "Dans quelle ville vos parents se sont-ils rencontrés ?",
      "QUESTION_14": "Comment s'appelait votre premier patron ?",
      "QUESTION_15": "Quel est le nom de la rue où vous avez grandi ?",
      "QUESTION_16": "Quel est le nom de la première plage où vous vous êtes baigné ?",
      "QUESTION_17": "Quel est le premier album que vous avez acheté ?",
      "QUESTION_18": "Quel est le nom de votre équipe de sport préférée ?",
      "QUESTION_19": "Quel était le métier de votre grand-père ?",
      "RECOVER_ID": "Retrouver mon mot de passe...",
      "RECOVER_ID_HELP": "Si vous disposez d'un <b>fichier de sauvegarde de vos identifiants</b>, vous pouvez les retrouver en répondant correctement à vos questions personnelles.",
      "REVOCATION_WITH_FILE": "Révoquer mon compte membre...",
      "REVOCATION_WITH_FILE_DESCRIPTION": "Si vous avez <b>définitivement perdus vos identifiants</b> de compte membre (ou que la sécurité du compte est compromise), vous pouvez utiliser <b>le fichier de révocation</b> du compte pour <b>forcer sa sortie définitive de la toile de confiance</b>.",
      "REVOCATION_WITH_FILE_HELP": "Pour <b>révoquer définitivement</b> un compte membre, veuillez glisser dans la zone ci-dessous votre fichier de révocation, ou bien cliquer dans la zone pour rechercher un fichier.",
      "REVOCATION_WALLET": "Révoquer immédiatement ce compte",
      "REVOCATION_WALLET_HELP": "Demander la révocation de votre identité entraine la <b>sortie de la toile de confiance</b> (définitive pour le pseudonyme et la clé publique associés). Le compte ne pourra plus produire de Dividende Universel.<br/>Vous pourrez toutefois encore vous y connecter, comme à un simple portefeuille.",
      "REVOCATION_FILENAME": "revocation-{{uid}}-{{pubkey|formatPubkey}}-{{currency}}.txt",
      "SAVE_ID": "Sauvegarder mes identifiants...",
      "SAVE_ID_HELP": "Création d'un fichier de sauvegarde, pour <b>retrouver votre mot de passe</b> (et l'identifiant secret) <b>en cas de d'oubli</b>. Le fichier est <b>sécurisé</b> (chiffré) à l'aide de questions personnelles.",
      "STRONG_LEVEL": "Fort <span class=\"hidden-xs \">(6 questions minimum)</span>",
      "TITLE": "Compte et sécurité"
    },
    "FILE_NAME": "{{currency}} - Relevé du compte {{pubkey|formatPubkey}} au {{currentTime|formatDateForFile}}.csv",
    "HEADERS": {
      "TIME": "Date",
      "AMOUNT": "Montant",
      "COMMENT": "Commentaire"
    }
  },
  "TRANSFER": {
    "TITLE": "Virement",
    "SUB_TITLE": "Faire un virement",
    "FROM": "De",
    "TO": "À",
    "AMOUNT": "Montant",
    "AMOUNT_HELP": "Montant",
    "COMMENT": "Commentaire",
    "COMMENT_HELP": "Commentaire",
    "BTN_SEND": "Envoyer",
    "BTN_ADD_COMMENT": "Ajouter un commentaire",
    "WARN_COMMENT_IS_PUBLIC": "Veuillez noter que <b>les commentaires sont publiques</b> (non chiffré).",
    "MODAL": {
      "TITLE": "Virement"
    }
  },
  "ERROR": {
    "POPUP_TITLE": "Erreur",
    "UNKNOWN_ERROR": "Erreur inconnue",
    "CRYPTO_UNKNOWN_ERROR": "Votre navigateur ne semble pas compatible avec les fonctionnalités de cryptographie.",
    "EQUALS_TO_PSEUDO": "Doit être différent du pseudonyme",
    "EQUALS_TO_SALT": "Doit être différent de l'identifiant secret",
    "FIELD_REQUIRED": "Champ obligatoire.",
    "FIELD_TOO_SHORT": "Valeur trop courte.",
    "FIELD_TOO_SHORT_WITH_LENGTH": "Valeur trop courte ({{minLength}} caractères min)",
    "FIELD_TOO_LONG": "Valeur trop longue",
    "FIELD_TOO_LONG_WITH_LENGTH": "Valeur trop longue ({{maxLength}} caractères max)",
    "FIELD_MIN": "Valeur minimale : {{min}}",
    "FIELD_MAX": "Valeur maximale : {{max}}",
    "FIELD_ACCENT": "Caractères accentués et virgules non autorisés",
    "FIELD_NOT_NUMBER": "Valeur numérique attendue",
    "FIELD_NOT_INT": "Valeur entière attendue",
    "FIELD_NOT_EMAIL": "Adresse email non valide",
    "PASSWORD_NOT_CONFIRMED": "Ne correspond pas au mot de passe.",
    "SALT_NOT_CONFIRMED": "Ne correspond pas à l'identifiant secret.",
    "SEND_IDENTITY_FAILED": "Echec de l'inscription.",
    "SEND_CERTIFICATION_FAILED": "Echec de la certification.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY": "Vous ne pouvez pas effectuer de certification, car votre compte n'est <b>pas membre</b>.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY_HAS_SELF": "Vous ne pouvez pas effectuer de certification, car votre compte n'est pas encore membre.<br/><br/>Il vous manque encore des certifications, ou bien celles-ci n'ont pas encore été validées.",
    "NOT_MEMBER_FOR_CERTIFICATION": "Votre compte n'est pas encore membre.",
    "IDENTITY_TO_CERTIFY_HAS_NO_SELF": "Compte non certifiable. Aucune demande d'adhésion n'a été faite, ou bien elle n'a pas été renouvellée.",
    "LOGIN_FAILED": "Erreur lors de la connexion.",
    "LOAD_IDENTITY_FAILED": "Erreur de chargement de l'identité.",
    "LOAD_REQUIREMENTS_FAILED": "Erreur de chargement des prérequis de l'identité.",
    "SEND_MEMBERSHIP_IN_FAILED": "Echec de la tentative d'entrée dans la communauté.",
    "SEND_MEMBERSHIP_OUT_FAILED": "Echec de l'arret de l'adhésion.",
    "REFRESH_WALLET_DATA": "Echec du rafraichissement du portefeuille.",
    "GET_CURRENCY_PARAMETER": "Echec de la récupération des règles de la monnaie.",
    "GET_CURRENCY_FAILED": "Chargement de la monnaie impossible. Veuillez ressayer plus tard.",
    "SEND_TX_FAILED": "Echec du virement.",
    "ALL_SOURCES_USED": "Veuillez attendre le calcul du prochain bloc (Toutes vos sources de monnaie ont été utilisées).",
    "NOT_ENOUGH_SOURCES": "Pas assez de change pour envoyer ce montant en une seule transaction.<br/>Montant maximum : {{amount}} {{unit}}<sub>{{subUnit}}</sub>.",
    "ACCOUNT_CREATION_FAILED": "Echec de la création du compte membre.",
    "RESTORE_WALLET_DATA_ERROR": "Echec du rechargement des paramètres depuis le stockage local",
    "LOAD_WALLET_DATA_ERROR": "Echec du chargement des données du portefeuille.",
    "COPY_CLIPBOARD_FAILED": "Copie de la valeur impossible.",
    "TAKE_PICTURE_FAILED": "Echec de la récupération de la photo.",
    "SCAN_FAILED": "Echec du scan de QR Code",
    "SCAN_UNKNOWN_FORMAT": "Code non reconnu.",
    "WOT_LOOKUP_FAILED": "Echec de la recherche",
    "LOAD_PEER_DATA_FAILED": "Lecture du nœud Duniter impossible. Veuillez réessayer ultérieurement.",
    "NEED_LOGIN_FIRST": "Veuillez d'abord vous connecter.",
    "AMOUNT_REQUIRED": "Le montant est obligatoire.",
    "AMOUNT_NEGATIVE": "Montant négatif non autorisé.",
    "NOT_ENOUGH_CREDIT": "Crédit insufisant.",
    "INVALID_NODE_SUMMARY": "Nœud injoignable ou adresse invalide.",
    "INVALID_USER_ID": "Le pseudonyme ne doit contenir ni espace ni caractère spécial ou accentué.",
    "INVALID_COMMENT": "Le champ 'référence' ne doit pas contenir de caractères accentués.",
    "INVALID_PUBKEY": "La clé publique n'a pas le format attendu.",
    "IDENTITY_REVOKED": "Cette identité <b>a été révoquée {{revocationTime|formatFromNow}}</b> ({{revocationTime|formatDate}}). Elle ne peut plus devenir membre.",
    "IDENTITY_PENDING_REVOCATION": "La <b>révocation de cette identité</b> a été demandée et est en attente de traitement. La certification est donc désactivée.",
    "IDENTITY_INVALID_BLOCK_HASH": "Cette demande d'adhésion n'est plus valide (car elle référence un bloc que les nœuds du réseau ont annulé) : cette personne doit renouveler sa demande d'adhésion <b>avant</b> d'être certifiée.",
    "IDENTITY_EXPIRED": "La publication de cette identité a expirée : cette personne doit effectuer une nouvelle demande d'adhésion <b>avant</b> d'être certifiée.",
    "IDENTITY_SANDBOX_FULL": "Le nœud Duniter utilisé par Cesium ne peut plus recevoir de nouvelles identités, car sa file d'attente est pleine.<br/><br/>Veuillez réessayer ultérieurement ou changer de nœud (via le menu <b>Paramètres</b>).",
    "IDENTITY_NOT_FOUND": "Identité non trouvée",
    "IDENTITY_TX_FAILED": "Echec du chargement des opérations",
    "WOT_PENDING_INVALID_BLOCK_HASH": "Adhésion non valide.",
    "WALLET_INVALID_BLOCK_HASH": "Votre demande d'adhésion n'est plus valide (car elle référence un bloc que les nœuds du réseau ont annulé).<br/>Vous devez <a ng-click=\"doQuickFix('fixMembership')\">envoyer une nouvelle demande</a> pour résoudre ce problème.",
    "WALLET_IDENTITY_EXPIRED": "La publication de <b>votre identité a expirée</b>.<br/>Vous devez <a ng-click=\"doQuickFix('fixIdentity')\">publier à nouveau votre identité</a> pour résoudre ce problème.",
    "WALLET_REVOKED": "Votre identité a été <b>révoquée</b> : ni votre pseudonyme ni votre clef publique ne pourront être utilisés à l'avenir pour un compte membre.",
    "WALLET_HAS_NO_SELF": "Votre identité doit d'abord avoir été publiée, et ne pas être expirée.",
    "AUTH_REQUIRED": "Authentification requise.",
    "AUTH_INVALID_PUBKEY": "La clef attendue est <i class=\"ion-key\"></i> {{pubkey|formatPubkey}}...",
    "AUTH_INVALID_SCRYPT": "Identifiant ou mot de passe invalide.",
    "AUTH_INVALID_FILE": "Fichier de trousseau invalide.",
    "AUTH_FILE_ERROR": "Echec de l'ouverture du fichier de trousseau",
    "IDENTITY_ALREADY_CERTIFY": "Vous avez <b>déjà certifié</b> cette identité.<br/><br/>Cette certification est encore valide (expiration {{expiresIn|formatDurationTo}}).",
    "IDENTITY_ALREADY_CERTIFY_PENDING": "Vous avez <b>déjà certifié</b> cette identité.<br/><br/>Cette certification est en attente de traitement (date limite de traitement {{expiresIn|formatDurationTo}}).",
    "UNABLE_TO_CERTIFY_TITLE": "Certification impossible",
    "LOAD_NEWCOMERS_FAILED": "Echec du chargement des nouveaux membres.",
    "LOAD_PENDING_FAILED": "Echec du chargement des inscriptions en attente.",
    "ONLY_MEMBER_CAN_EXECUTE_THIS_ACTION": "Vous devez <b>être membre</b> pour pouvoir effectuer cette action.",
    "ONLY_SELF_CAN_EXECUTE_THIS_ACTION": "Vous devez avoir <b>publié votre identité</b> pour pouvoir effectuer cette action.",
    "GET_BLOCK_FAILED": "Echec de la récupération du bloc",
    "INVALID_BLOCK_HASH": "Bloc non trouvé (hash différent)",
    "DOWNLOAD_REVOCATION_FAILED": "Echec du téléchargement du fichier de révocation.",
    "REVOCATION_FAILED": "Echec de la révocation.",
    "SALT_OR_PASSWORD_NOT_CONFIRMED": "Identifiant secret ou mot de passe incorrects",
    "RECOVER_ID_FAILED": "Echec de la récupération des identifiants",
    "LOAD_FILE_FAILED" : "Echec du chargement du fichier",
    "NOT_VALID_REVOCATION_FILE": "Fichier de revocation non valide (mauvais format de fichier)",
    "NOT_VALID_SAVE_ID_FILE": "Fichier de récupération non valide (mauvais format de fichier)",
    "NOT_VALID_KEY_FILE": "Fichier de trousseau non valide (format non reconnu)",
    "EXISTING_ACCOUNT": "Vos identifiants correspondent à un compte déjà existant, dont la <a ng-click=\"showHelpModal('pubkey')\">clef publique</a> est :",
    "EXISTING_ACCOUNT_REQUEST": "Veuillez modifier vos identifiants afin qu'ils correspondent à un compte non utilisé.",
    "GET_LICENSE_FILE_FAILED": "Récupérer du fichier de licence impossible",
    "CHECK_NETWORK_CONNECTION": "Aucun nœud ne semble accessible.<br/><br/>Veuillez <b>vérifier votre connection Internet</b>.",
    "ISSUE_524_TX_FAILED": "Echec du virement.<br/><br/>Un message a été envoyé aux développeurs pour faciliter la résolution du problème. <b>Merci de votre aide</b>."
  },
  "INFO": {
    "POPUP_TITLE": "Information",
    "CERTIFICATION_DONE": "Certification envoyée",
    "NOT_ENOUGH_CREDIT": "Crédit insuffisant",
    "TRANSFER_SENT": "Virement envoyé",
    "COPY_TO_CLIPBOARD_DONE": "Copié dans le presse-papier",
    "MEMBERSHIP_OUT_SENT": "Résiliation envoyée",
    "NOT_NEED_MEMBERSHIP": "Vous êtes déjà membre.",
    "IDENTITY_WILL_MISSING_CERTIFICATIONS": "Cette identité va bientôt manquer de certification (au moins {{willNeedCertificationCount}}).",
    "IDENTITY_NEED_MEMBERSHIP": "Cette identité n'a pas envoyée de demande d'adhésion. Elle devra si elle souhaite devenir membre.",
    "REVOCATION_SENT": "Revocation envoyée",
    "REVOCATION_SENT_WAITING_PROCESS": "La <b>révocation de cette identité</b> a été demandée et est en attente de traitement.",
    "FEATURES_NOT_IMPLEMENTED": "Cette fonctionnalité est encore en cours de développement.<br/>Pourquoi ne pas <b>contribuer à Cesium</b>, pour l'obtenir plus rapidement ? ;)",
    "EMPTY_TX_HISTORY": "Aucune opération à exporter"
  },
  "CONFIRM": {
    "POPUP_TITLE": "<b>Confirmation</b>",
    "POPUP_WARNING_TITLE": "<b>Avertissement</b>",
    "POPUP_SECURITY_WARNING_TITLE": "<i class=\"icon ion-alert-circled\"></i> <b>Avertissement de sécurité</b>",
    "CERTIFY_RULES_TITLE_UID": "Certifier {{uid}}",
    "CERTIFY_RULES": "<b class=\"assertive\">Ne PAS certifier</b> un compte si vous pensez que :<br/><br/><ul><li>1.) il ne correspond pas à une personne <b>physique et vivante</b>.<li>2.) son propriétaire <b>possède un autre compte</b> déjà certifié.<li>3.) son propriétaire viole (volontairement ou non) la règle 1 ou 2 (par exemple en certifiant des comptes factices ou en double).</ul><br/><b>Etes-vous sûr</b> de vouloir néanmoins certifier cette identité ?",
    "TRANSFER": "<b>Récapitulatif du virement</b> :<br/><br/><ul><li> - De : {{from}}</li><li> - A : <b>{{to}}</b></li><li> - Montant : <b>{{amount}} {{unit}}</b></li><li> - Commentaire : <i>{{comment}}</i></li></ul><br/><b>Etes-vous sûr de vouloir effectuer ce virement ?</b>",
    "MEMBERSHIP_OUT": "Cette opération est <b>irréversible</b>.<br/></br/>Etes-vous sûr de vouloir <b>résilier votre compte membre</b> ?",
    "MEMBERSHIP_OUT_2": "Cette opération est <b>irreversible</b> !<br/><br/>Etes-vous vraiment sûr de vouloir <b>résilier votre adhésion</b> comme membre ?",
    "LOGIN_UNUSED_WALLET_TITLE": "Erreur de saisie ?",
    "LOGIN_UNUSED_WALLET": "Le compte connecté semble <b>inactif</b>.<br/><br/>Il s'agit probablement d'une <b>erreur de saisie</b> dans vos identifiants de connexion. Veuillez recommencer, en vérifiant que <b>la clé publique est celle de votre compte</b>.",
    "FIX_IDENTITY": "Le pseudonyme <b>{{uid}}</b> va être publiée à nouveau, en remplacement de l'ancienne publication qui a expirée.<br/></br/><b>Etes-vous sûr</b> de vouloir continuer ?",
    "FIX_MEMBERSHIP": "Votre demande d'adhésion comme membre va être renvoyée.<br/></br/><b>Etes-vous sûr</b> de vouloir continuer ?",
    "MEMBERSHIP": "Votre demande d'adhésion comme membre va être envoyée.<br/></br/><b>Etes-vous sûr</b> de vouloir continuer ?",
    "RENEW_MEMBERSHIP": "Votre adhésion comme membre va être renouvellée.<br/></br/><b>Etes-vous sûr</b> de vouloir continuer ?",
    "REVOKE_IDENTITY": "Vous allez <b>revoquer définitivement cette identité</b>.<br/><br/>La clé publique et le pseudonyme associés <b>ne pourront plus jamais être utilisés</b> (pour un compte membre). <br/></br/><b>Etes-vous sûr</b> de vouloir révoquer définitivement ce compte ?",
    "REVOKE_IDENTITY_2": "Cette opération est <b>irreversible</b> !<br/><br/>Etes-vous vraiment sûr de vouloir <b>révoquer définitivement</b> ce compte ?",
    "NOT_NEED_RENEW_MEMBERSHIP": "Votre adhésion n'a pas besoin d'être renouvellée (elle n'expirera que dans {{membershipExpiresIn|formatDuration}}).<br/></br/><b>Etes-vous sûr</b> de vouloir renouveler votre adhésion ?",
    "SAVE_BEFORE_LEAVE": "Voulez-vous <b>sauvegarder vos modifications</b> avant de quitter la page ?",
    "SAVE_BEFORE_LEAVE_TITLE": "Modifications non enregistrées",
    "LOGOUT": "Etes-vous sûr de vouloir vous déconnecter ?",
    "USE_FALLBACK_NODE": "Nœud <b>{{old}}</b> injoignable ou adresse invalide.<br/><br/>Voulez-vous temporairement utiliser le nœud <b>{{new}}</b> ?",
    "ISSUE_524_SEND_LOG": "La transaction a été rejettée, à cause d'une anomalie connue (ticket #524) mais <b>non reproduite</b>.<br/><br/>Pour nous aider les développeurs à corriger cette erreur, <b>acceptez-vous la transmission de vos logs</b> par message ?<br/><small>(Aucune donnée confidentielle n'est envoyée)</small>."
  },
  "DOWNLOAD": {
    "POPUP_TITLE": "<b>Fichier de révocation</b>",
    "POPUP_REVOKE_MESSAGE": "Pour sécuriser votre compte, veuillez télécharger le <b>document de révocation de compte</b>. Il vous permettra le cas échéant d'annuler votre compte (en cas d'un vol de compte, d'un changement d'identifiant, d'un compte créé à tort, etc.).<br/><br/><b>Veuillez le stocker en lieu sûr.</b>"
  },
  "HELP": {
    "TITLE": "Aide en ligne",
    "JOIN": {
      "SECTION": "Inscription",
      "SALT": "L'identifiant secret est très important. Il sert à mélanger le mot de passe, avant qu'il ne servent à calculer la <span class=\"text-italic\">clé publique</span> de votre compte (son numéro) et la clé secrète pour y accéder.<br/><b>Veuillez à bien la mémoriser</b>, car aucun moyen n'est actuellement prévu pour la retrouver en cas de perte.<br/>Par ailleurs, il ne peut pas être modifié sans devoir créer un nouveau compte.<br/><br/>Un bon identifiant secret doit être suffisamment long (au moins 8 caractères) et le plus original possible.",
      "PASSWORD": "Le mot de passe est très important. Avec l'identifiant secret, il sert à à calculer le numéro (la clé publique) de votre compte, et la clé secrète pour y accéder.<br/><b>Veuillez à bien le mémoriser</b>, car aucun moyen n'est de le retrouver en cas de perte (sauf à générer un fichier de sauvegarde).<br/>Par ailleurs, il ne peut pas être modifié sans devoir créer un nouveau compte.<br/><br/>Un bon mot de passe contient (idéalement) au moins 8 caractères, dont au moins une majuscule et un chiffre.",
      "PSEUDO": "Le pseudonyme utilisé uniquement dans le cas d'inscription comme <span class=\"text-italic\">membre</span>. Il est toujours associé à un portefeuille (via sa <span class=\"text-italic\">clé publique</span>).<br/>Il est publié sur le réseau, afin que les autres utilisateurs puisse l'identifier, le certifier ou envoyer de la monnaie sur le compte.<br/>Un pseudonyme doit être unique au sein des membres (<u>actuels</u> et anciens)."
    },
    "LOGIN": {
      "SECTION": "Connexion",
      "PUBKEY": "Clé publique du trousseau",
      "PUBKEY_DEF": "La clef publique du trousseau est générée à partir des identifiants saisis (n'importe lesquels), sans pour autant qu'ils correspondent à un compte déjà utilisé.<br/><b>Vérifiez attentivement que la clé publique est celle de votre compte</b>. Dans le cas contraire, vous serez connecté à un compte probablement jamais utilisé, le risque de collision avec un compte existant étant infime.<br/><a href=\"https://fr.wikipedia.org/wiki/Cryptographie_asym%C3%A9trique\" target=\"_system\">En savoir plus sur la cryptographie</a> par clé publique.",
      "METHOD": "Méthodes de connexion",
      "METHOD_DEF": "Plusieurs options sont disponibles pour vous connecter à un portfeuille :<br/> - La connexion <b>par sallage (simple ou avancé)</b> mélange votre mot de passe grâce à l'identifiant secret, pour limiter les tentatives de <a href=\"https://fr.wikipedia.org/wiki/Attaque_par_force_brute\" target=\"_system\">piratage par force brute</a> (par exemple à partir de mots connus).<br/> - La connexion <b>par clé publique</b> évite de saisir vos identifiants, qui vous seront demandé seulement le moment venu lors d'une opération sur le compte.<br/> - La connexion <b>par fichier de trousseau</b> va lire les clés (publique et privée) du compte, depuis un fichier, sans besoin de saisir d'identifiants. Plusieurs formats de fichier sont possibles."
    },
    "GLOSSARY": {
      "SECTION": "Glossaire",
      "PUBKEY_DEF": "Une clé publique identifie un portefeuille de monnaie, qui peut identifier un membre ou correspondre à un portefeuille anonyme. Dans Cesium la clé publique est calculée (par défaut) grâce à l'identifiant secret et au mot de passe.<br/><a href=\"https://fr.wikipedia.org/wiki/Cryptographie_asym%C3%A9trique\" target=\"_system\">En savoir plus sur la cryptographie</a> par clé publique.",
      "MEMBER": "Membre",
      "MEMBER_DEF": "Un membre est une personne humaine physique et vivante, désireuse de participer librement à la communauté monétaire. Elle percoit un dividende universel, suivant une période et un montant tels que définis dans les <span class=\"text-italic\">règles de la monnaie</span>",
      "CURRENCY_RULES": "Règles de la monnaie",
      "CURRENCY_RULES_DEF": "Les règles de la monnaie sont définies une fois pour toute. Elle fixe le fonctionnement de la monnaie : le calcul du dividend universel, le nombre de certifications nécessaire pour être membre, le nombre de certification maximum qu'un membre peut donner, etc. <a href=\"#/app/currency\">Voir les règles actuelles</a>.<br/>La non modification des règles dans le temps est possible par l'utilisation d'une <span class=\"text-italic\">BlockChain</span> qui porte et execute ces règles, et en vérifie constamment la bonne application.",
      "BLOCKCHAIN": "Chaîne de blocs (<span class=\"text-italic\">Blockchain</span>)",
      "BLOCKCHAIN_DEF": "La BlockChain est un système décentralisé, qui, dans le cas de Duniter, sert à porter et executer les <span class=\"text-italic\">règles de la monnaie</span>.<br/><a href=\"https://duniter.org/fr/comprendre/\" target=\"_system\">En savoir plus sur Duniter</a> et le fonctionnement de sa blockchain.",
      "UNIVERSAL_DIVIDEND_DEF": "Le Dividende Universel (DU) est la quantité de monnaie co-créée par chaque membre, suivant la période et le calcul définie dans les <span class=\"text-italic\">règles de la monnaie</span>.<br/>A chaque échéance, les membres recoivent sur leur compte la meme quantité de nouvelle monnaie.<br/><br/>Le DU subit une croissance régulière, pour rester juste entre les membres (actuels et à venir), calculée en fonction de l'espérance de vie moyenne, telle que démontré dans la Thérorie Relative de la Monnaie (TRM).<br/><a href=\"http://trm.creationmonetaire.info\" target=\"_system\">En savoir plus sur la TRM</a> et les monnaies libres."
    },
    "TIP": {
      "MENU_BTN_CURRENCY": "Le menu <b>{{'MENU.CURRENCY'|translate}}</b> permet la consultation des <b>règles de la monnaie</b> et de son état.",
      "CURRENCY_WOT": "Le <b>nombre de membres</b> montre l'importance de la communauté et permet de <b>suivre son évolution</b>.",
      "CURRENCY_MASS": "Suivez ici la <b>quantité totale de monnaie</b> existante et sa <b>répartition moyenne</b> par membre.<br/><br/>Ceci permet de juger de l'<b>importance d'un montant</b>, vis à vis de ce que <b>possède les autres</b> sur leur compte (en moyenne).",
      "CURRENCY_UNIT_RELATIVE": "L'unité utilisée (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) signifie que les montants en {{currency|capitalize}} ont été divisés par le <b>Dividende Universel</b> (DU).<br/><br/><small>Cette unité relative est <b>pertinente</b>, car stable malgré la quantitié de monnaie qui augmente en permanence.</small>",
      "CURRENCY_CHANGE_UNIT": "L'option <b>{{'COMMON.BTN_RELATIVE_UNIT'|translate}}</b> permet de <b>changer d'unité</b>, pour visualiser les montants <b>directement en {{currency|capitalize}}</b> (plutôt qu'en &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;).",
      "CURRENCY_CHANGE_UNIT_TO_RELATIVE": "L'option <b>{{'COMMON.BTN_RELATIVE_UNIT'|translate}}</b> permet de <b>changer d'unité</b>, pour visualiser les montants en &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;, c'est à dire relativement au Dividende Universel (le montant co-produit par chaque membre).",
      "CURRENCY_RULES": "Les <b>règles</b> de la monnaie fixent son fonctionnement <b>exact et prévisible</b>.<br/><br/>Véritable ADN de la monnaie, elles rendent son code monétaire <b>lisible et transparent</b>.",
      "MENU_BTN_NETWORK": "Le menu <b>{{'MENU.NETWORK'|translate}}</b> permet la consultation de l'état du réseau.",
      "NETWORK_BLOCKCHAIN": "Toutes les opérations de la monnaie sont enregistrées dans un grand livre de compte <b>public et infalsifiable</b>, appellé aussi <b>chaine de blocs</b> (<em>BlockChain</em> en anglais).",
      "NETWORK_PEERS": "Les <b>nœuds</b> visibles ici correspondent aux <b>ordinateurs qui actualisent et contrôlent</b> la chaine de blocs.<br/><br/>Plus il y a de nœuds, plus la monnaie à une gestion <b>décentralisée</b> et digne de confiance.",
      "NETWORK_PEERS_BLOCK_NUMBER": "Ce <b>numéro</b> (en vert) indique le <b>dernier bloc validé</b> pour ce nœud (dernière page écrite dans le grand livre de comptes).<br/><br/>La couleur verte indique que ce bloc est également validé par <b>la plupart des autres nœuds</b>.",
      "NETWORK_PEERS_PARTICIPATE": "<b>Chaque membre</b>, équipé d'un ordinateur avec Internet, <b>peut participer en ajoutant un nœud</b>. Il suffit d'<b>installer le logiciel Duniter</b> (libre et gratuit). <a href=\"{{installDocUrl}}\" target=\"_system\">Voir le manuel d'installation &gt;&gt;</a>.",
      "MENU_BTN_ACCOUNT": "Le menu <b>{{'ACCOUNT.TITLE'|translate}}</b> permet d'accéder à la gestion de votre compte.",
      "MENU_BTN_ACCOUNT_MEMBER": "Consultez ici l'état de votre compte et les informations sur vos certifications.",
      "WALLET_CERTIFICATIONS": "Cliquez ici pour consulter le détail de vos certifications (reçues et émises).",
      "WALLET_RECEIVED_CERTIFICATIONS": "Cliquez ici pour consulter le détail de vos <b>certifications reçues</b>.",
      "WALLET_GIVEN_CERTIFICATIONS": "Cliquez ici pour consulter le détail de vos <b>certifications émises</b>.",
      "WALLET_BALANCE": "Le <b>solde</b> de votre compte s'affiche ici.",
      "WALLET_BALANCE_RELATIVE": "{{'HELP.TIP.WALLET_BALANCE'|translate}}<br/><br/>L'unité utilisée (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) signifie que le montant en {{currency|capitalize}} a été divisé par le <b>Dividende Universel</b> (DU) co-créé par chaque membre.<br/><br/>Actuellement 1 DU vaut {{currentUD|formatInteger}} {{currency|capitalize}}s.",
      "WALLET_BALANCE_CHANGE_UNIT": "Vous pourrez <b>changer l'unité</b> d'affichage des montants dans les <b><i class=\"icon ion-android-settings\"></i>&nbsp;{{'MENU.SETTINGS'|translate}}</b>.<br/><br/>Par exemple pour visualiser les montants <b>directement en {{currency|capitalize}}</b>, plutôt qu'en unité relative.",
      "WALLET_PUBKEY": "Voici la clé publique de votre compte. Vous pouvez la communiquer à un tiers afin qu'il identifie plus simplement votre compte.",
      "WALLET_SEND": "Effectuer un paiement en quelques clics",
      "WALLET_SEND_NO_MONEY": "Effectuer un paiement en quelques clics.<br/>(Votre solde ne le permet pas encore)",
      "WALLET_OPTIONS": "Ce bouton permet l'accès aux <b>actions d'adhésion</b> et de sécurité.<br/><br/>N'oubliez pas d'y jeter un oeil !",
      "WALLET_RECEIVED_CERTS": "S'affichera ici la liste des personnes qui vous ont certifié.",
      "WALLET_CERTIFY": "Le bouton <b>{{'WOT.BTN_SELECT_AND_CERTIFY'|translate}}</b> permet de sélectionner une identitié et de la certifier.<br/><br/>Seuls des utilisateurs <b>déjà membre</b> peuvent en certifier d'autres.",
      "WALLET_CERT_STOCK": "Votre stock de certification (émises) est limité à <b>{{sigStock}} certifications</b>.<br/><br/>Ce stock se renouvelle avec le temps, au fur et à mesure que les certifications s'invalident.",
      "MENU_BTN_TX_MEMBER": "Le menu <b>{{'MENU.TRANSACTIONS'|translate}}</b> permet de consulter votre solde, l'historique vos transactions et d'envoyer un paiement.",
      "MENU_BTN_TX": "Consultez ici <b>l'historique de vos transactions</b> et effectuer de nouvelles opérations.",
      "MENU_BTN_WOT": "Le menu <b>{{'MENU.WOT'|translate}}</b> permet de rechercher parmi les <b>utilisateurs</b> de la monnaie (membre ou non).",
      "WOT_SEARCH_TEXT_XS": "Pour rechercher dans l'annuaire, tapez les <b>premières lettres d'un pseudonyme</b> (ou d'une clé publique).<br/><br/>La recherche se lancera automatiquement.",
      "WOT_SEARCH_TEXT": "Pour rechercher dans l'annuaire, tapez les <b>premières lettres d'un pseudonyme</b> (ou d'une clé publique). <br/><br/>Appuyer ensuite sur <b>Entrée</b> pour lancer la recherche.",
      "WOT_SEARCH_RESULT": "Visualisez la fiche détaillée simplement en <b>cliquant</b> sur une ligne.",
      "WOT_VIEW_CERTIFICATIONS": "La ligne <b>{{'ACCOUNT.CERTIFICATION_COUNT'|translate}}</b> montre combien de membres ont validés cette identité.<br/><br/>Ces certifications attestent que le compte appartient à <b>une personne humaine vivante</b> n'ayant <b>aucun autre compte membre</b>.",
      "WOT_VIEW_CERTIFICATIONS_COUNT": "Il faut au moins <b>{{sigQty}} certifications</b> pour devenir membre et recevoir le <b>Dividende Universel</b>.",
      "WOT_VIEW_CERTIFICATIONS_CLICK": "Un clic ici permet d'ouvrir <b>la liste de toutes les certifications</b> de l'identité (reçues et émises).",
      "WOT_VIEW_CERTIFY": "Le bouton <b>{{'WOT.BTN_CERTIFY'|translate}}</b> permet d'ajouter votre certification à cette identité.",
      "CERTIFY_RULES": "<b>Attention :</b> Ne certifier que des <b>personnes physiques vivantes</b>, ne possèdant aucun autre compte membre.<br/><br/>La sécurité de la monnaie dépend de la vigilance de chacun !",
      "MENU_BTN_SETTINGS": "Les <b>{{'MENU.SETTINGS'|translate}}</b> vous permettront de configurer l'application.",
      "HEADER_BAR_BTN_PROFILE": "Cliquez ici pour accéder à votre <b>profil utilisateur</b>",
      "SETTINGS_CHANGE_UNIT": "Vous pourrez <b>changer d'unité d'affichage</b> des montants en cliquant ci-dessus.<br/><br/>- Désactivez l'option pour un affichage des montants en {{currency|capitalize}}.<br/>- Activez l'option pour un affichage relatif en {{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub> (tous les montants seront <b>divisés</b> par le Dividende Universel courant).",
      "END_LOGIN": "Cette visite guidée est <b>terminée</b> !<br/><br/>Bonne continuation à vous, dans le nouveau monde de l'<b>économie libre</b> !",
      "END_NOT_LOGIN": "Cette visite guidée est <b>terminée</b> !<br/><br/>Si vous souhaitez rejoindre la monnaie {{currency|capitalize}}, il vous suffira de cliquer sur <b>{{'LOGIN.CREATE_ACCOUNT'|translate}}</b> ci-dessous."
    }
  },
  "API" :{
    "COMMON": {
      "LINK_DOC": "documentation API",
      "LINK_DOC_HELP": "Documentation pour les développeurs",
      "LINK_STANDARD_APP": "version classique",
      "LINK_STANDARD_APP_HELP": "Ouvrir la version classique de {{'COMMON.APP_NAME'|translate}}"
    },
    "HOME": {
      "TITLE": "Documentation API {{'COMMON.APP_NAME'|translate}}",
      "MESSAGE": "Bienvenue dans la <b>documentation de l'API</b> {{'COMMON.APP_NAME'|translate}}.<br/>Connecter vos sites web à <a href=\"http://duniter.org\" target=\"_system\">Duniter</a> très simplement !",
      "MESSAGE_SHORT": "Connecter vos sites à <a href=\"http://duniter.org\" target=\"_system\">Duniter</a> très simplement !",
      "DOC_HEADER": "Services disponibles :"
    },
    "TRANSFER": {
      "TITLE": "{{'COMMON.APP_NAME'|translate}} - Paiement en ligne",
      "TITLE_SHORT": "Paiement en ligne",
      "SUMMARY": "Récapitulatif de la commande :",
      "AMOUNT": "Montant :",
      "NAME": "Nom :",
      "PUBKEY": "Clé publique du destinaire :",
      "COMMENT": "Référence de la commande :",
      "DEMO": {
        "SALT": "demo",
        "PASSWORD": "demo",
        "PUBKEY": "3G28bL6deXQBYpPBpLFuECo46d3kfYMJwst7uhdVBnD1",
        "HELP": "<b>Mode démonstration</b> : Aucun paiement ne sera réellement envoyé pendant cette simulation.<br/>Veuillez utiliser les identifiants : <b>{{'API.TRANSFER.DEMO.SALT'|translate}} / {{'API.TRANSFER.DEMO.PASSWORD'|translate}}</b>",
        "BAD_CREDENTIALS": "Vérifiez votre saisie.<br/>En mode démonstration, les identifiants sont : {{'API.TRANSFER.DEMO.SALT'|translate}} / {{'API.TRANSFER.DEMO.PASSWORD'|translate}}"
      },
      "INFO": {
        "SUCCESS_REDIRECTING_WITH_NAME": "Paiement envoyé.<br/>Redirection vers <b>{{name}}</b>...",
        "SUCCESS_REDIRECTING": "Paiement envoyé.<br/>Redirection vers le site du vendeur...",
        "CANCEL_REDIRECTING_WITH_NAME": "Paiement annulé.<br/>Redirection vers <b>{{name}}</b>...",
        "CANCEL_REDIRECTING": "Paiement annulé.<br/>Redirection vers le site du vendeur..."
      },
      "ERROR": {
        "TRANSFER_FAILED": "Echec du paiement"
      }
    },
    "DOC": {
      "DESCRIPTION_DIVIDER": "Description",
      "URL_DIVIDER": "Adresse d'appel",
      "PARAMETERS_DIVIDER": "Paramètres",
      "AVAILABLE_PARAMETERS": "Voici la liste des paramètres possibles :",
      "DEMO_DIVIDER": "Tester",
      "DEMO_HELP": "Pour tester ce service, cliquez sur le bouton ci-contre. Le résultat s'affichera en dessous.",
      "DEMO_RESULT": "Résultat retourné par l'appel :",
      "DEMO_SUCCEED": "<i class=\"icon ion-checkmark\"></i> Succès !",
      "DEMO_CANCELLED": "<i class=\"icon ion-close\"></i> Annulé par l'utilisateur",
      "INTEGRATE_DIVIDER": "Intégrer",
      "INTEGRATE_CODE": "Code :",
      "INTEGRATE_RESULT": "Prévisualisation du résultat :",
      "INTEGRATE_PARAMETERS": "Paramètres",
      "TRANSFER": {
        "TITLE": "Paiements",
        "DESCRIPTION": "Depuis un site (ex: vente en ligne) vous pouvez déléguer le paiement en monnaie libre à Cesium API. Pour cela, il vous suffit de déclencher l'ouveture d'un page sur l'adresse suivante :",
        "PARAM_PUBKEY": "Clé publique du destinataire",
        "PARAM_PUBKEY_HELP": "Clé publique du destinataire (obligatoire)",
        "PARAM_AMOUNT": "Montant",
        "PARAM_AMOUNT_HELP": "Montant de la transaction (obligatoire)",
        "PARAM_COMMENT": "Référence (ou commentaire)",
        "PARAM_COMMENT_HELP": "Référence ou commentaire. Vous permettra par exemple d'identifier le paiement dans la BlockChain.",
        "PARAM_NAME": "Nom (du destinataire ou du site web)",
        "PARAM_NAME_HELP": "Le nom du destinataire, ou du site web appellant. Cela peut-etre un nom lisible (\"Mon site en ligne\"), ou encore une pseudo-adresse web (\"MonSite.com\").",
        "PARAM_REDIRECT_URL": "Adresse web de redirection",
        "PARAM_REDIRECT_URL_HELP": "Adresse web (URL) de redirection, appellé quand le paiement a été envoyé. Peut contenir les chaines suivantes, qui seront remplacée par les valeurs de la transaction : \"{tx}\", \"{hash}\", \"{comment}\", \"{amount}\" et {pubkey}.",
        "PARAM_CANCEL_URL": "Adresse web d'annulation",
        "PARAM_CANCEL_URL_HELP": "Adresse web (URL) en cas d'annulation du paiement, par l'utilisateur. Peut contenir les chaines suivantes, qui seront remplacée dynamiquement: \"{comment}\", \"{amount}\" et {pubkey}.",
        "EXAMPLES_HELP": "Voici des exemples d'intégration :",
        "EXAMPLE_BUTTON": "Bouton HTML",
        "EXAMPLE_BUTTON_DEFAULT_TEXT": "Payer en {{currency|currencySymbol}}",
        "EXAMPLE_BUTTON_DEFAULT_STYLE": "Style personnalisé",
        "EXAMPLE_BUTTON_TEXT_HELP": "Texte du bouton",
        "EXAMPLE_BUTTON_BG_COLOR": "Couleur du fond",
        "EXAMPLE_BUTTON_BG_COLOR_HELP": "Exemple: #fbc14c, black, lightgrey, rgb(180,180,180)",
        "EXAMPLE_BUTTON_FONT_COLOR": "Couleur du texte",
        "EXAMPLE_BUTTON_FONT_COLOR_HELP": "Exemple: black, orange, rgb(180,180,180)",
        "EXAMPLE_BUTTON_TEXT_ICON": "Icône",
        "EXAMPLE_BUTTON_TEXT_WIDTH": "Largeur",
        "EXAMPLE_BUTTON_TEXT_WIDTH_HELP": "Exemple: 200px, 50%",
        "EXAMPLE_BUTTON_ICON_NONE": "Aucune",
        "EXAMPLE_BUTTON_ICON_DUNITER": "Logo Duniter",
        "EXAMPLE_BUTTON_ICON_CESIUM": "Logo Cesium",
        "EXAMPLE_BUTTON_ICON_G1_COLOR": "Logo Ğ1",
        "EXAMPLE_BUTTON_ICON_G1_BLACK": "Logo Ğ1 (noir)"
      }
    }
  }
}
);

$translateProvider.translations("nl-NL", {
  "COMMON": {
    "APP_NAME": "Cesium",
    "APP_VERSION": "v{{version}}",
    "APP_BUILD": "build {{build}}",
    "PUBKEY": "Publieke sleutel",
    "MEMBER": "Lid",
    "BLOCK": "Blok",
    "BTN_OK": "OK",
    "BTN_YES": "Ja",
    "BTN_NO": "Nee",
    "BTN_SEND": "Verzenden",
    "BTN_SEND_MONEY": "Verstuur geld",
    "BTN_SEND_MONEY_SHORT": "Versturen",
    "BTN_SAVE": "Opslaan",
    "BTN_YES_SAVE": "Ja, opslaan",
    "BTN_YES_CONTINUE": "Ja, doorgaan",
    "BTN_SHOW": "Tonen",
    "BTN_SHOW_PUBKEY": "Toon sleutel",
    "BTN_RELATIVE_UNIT": "Gebruik relatieve eenheid",
    "BTN_BACK": "Terug",
    "BTN_NEXT": "Volgende",
    "BTN_CANCEL": "Annuleer",
    "BTN_CLOSE": "Sluit",
    "BTN_LATER": "Later",
    "BTN_LOGIN": "Aanmelden",
    "BTN_LOGOUT": "Uitloggen",
    "BTN_ADD_ACCOUNT": "Nieuwe Rekening",
    "BTN_SHARE": "Delen",
    "BTN_EDIT": "Bewerken",
    "BTN_DELETE": "Wissen",
    "BTN_ADD": "Toevoegen",
    "BTN_SEARCH": "Zoeken",
    "BTN_REFRESH": "Verwezenlijken",
    "BTN_START": "Beginnen",
    "BTN_CONTINUE": "Doorgaan",
    "BTN_UNDERSTOOD": "Ik heb het begrepen",
    "BTN_OPTIONS": "Opties",
    "BTN_HELP_TOUR": "Rondleiding",
    "BTN_HELP_TOUR_SCREEN": "Ontdek dit scherm",
    "BTN_DOWNLOAD": "Downloaden",
    "BTN_DOWNLOAD_ACCOUNT_STATEMENT": "Downloaden het rekeningoverzicht",
    "BTN_MODIFY": "Bewerken",
    "DAYS": "dagen",
    "NO_ACCOUNT_QUESTION": "Nog geen lid? Registreer nu!",
    "SEARCH_NO_RESULT": "Geen resultaten",
    "LOADING": "Even geduld...",
    "SEARCHING": "Zoeken...",
    "FROM": "Van",
    "TO": "Aan",
    "COPY": "Kopieren",
    "LANGUAGE": "Taal",
    "UNIVERSAL_DIVIDEND": "Universeel dividend",
    "UD": "UD",
    "DATE_PATTERN": "DD-MM-YYYY HH:mm",
    "DATE_FILE_PATTERN": "YYYY-MM-DD",
    "DATE_SHORT_PATTERN": "DD-MM-YY",
    "DATE_MONTH_YEAR_PATTERN": "MM-YYYY",
    "EMPTY_PARENTHESIS": "(leeg)",
    "UID": "Pseudoniem",
    "ENABLE": "Geactiveerd",
    "DISABLE": "Gedeactiveerd",
    "RESULTS_LIST": "Resultaten:",
    "RESULTS_COUNT": "{{count}} uitslagen",
    "EXECUTION_TIME": "uitgevoerd in {{duration|formatDurationMs}}",
    "SHOW_VALUES": "Toon waarden openlijk?",
    "POPOVER_ACTIONS_TITLE": "Opties",
    "POPOVER_FILTER_TITLE": "Filters",
    "SHOW_MORE": "Toon meer",
    "SHOW_MORE_COUNT": "(huidig limiet op {{limit}})",
    "POPOVER_SHARE": {
      "TITLE": "Delen",
      "SHARE_ON_TWITTER": "Deel op Twitter",
      "SHARE_ON_FACEBOOK": "Deel op Facebook",
      "SHARE_ON_DIASPORA": "Deel op Diaspora*",
      "SHARE_ON_GOOGLEPLUS": "Deel op Google+"
    }
  },
  "SYSTEM": {
    "PICTURE_CHOOSE_TYPE": "Selecteer bron:",
    "BTN_PICTURE_GALLERY": "Gallerij",
    "BTN_PICTURE_CAMERA": "<b>Camera</b>"
  },
  "MENU": {
    "HOME": "Welkom",
    "WOT": "Register",
    "CURRENCY": "Valuta",
    "CURRENCIES": "Valuta's",
    "ACCOUNT": "Mijn rekening",
    "TRANSFER": "Overmaken",
    "SCAN": "Scannen",
    "SETTINGS": "Instellingen",
    "NETWORK": "Netwerk",
    "TRANSACTIONS": "Mijn transacties"
  },
  "ABOUT": {
    "TITLE": "Over",
    "LICENSE": "<b>Vrije</b> software (GNU GPLv3 licentie).",
    "CODE": "Broncode:",
    "DEVELOPERS": "Ontwikkelaars:",
    "FORUM": "Forum:",
    "DEV_WARNING": "Waarschuwing",
    "DEV_WARNING_MESSAGE": "Deze applicatie is nog in actieve onwikkeling.<br/>Meld ons elk pobleem!",
    "DEV_WARNING_MESSAGE_SHORT": "Deze App is nog instabiel (in ontwikkeling).",
    "REPORT_ISSUE": "Meld een probleem"
  },
  "HOME": {
    "TITLE": "Cesium",
    "WELCOME": "Welkom bij de Cesium Applicatie!",
    "MESSAGE": "Bekijk je {{currency|abbreviate}} portefeilles in real time.",
    "BTN_REGISTRY": "Register",
    "BTN_CURRENCY": "Verken valuta",
    "BTN_ABOUT": "over",
    "BTN_HELP": "Help",
    "REPORT_ISSUE": "Meld een probleem",
    "NOT_YOUR_ACCOUNT_QUESTION" : "Je hoeft niet de rekening <b><i class=\"ion-key\"></i> {{pubkey|formatPubkey}}</b> in uw bezit?",
    "BTN_CHANGE_ACCOUNT": "Dit account ontkoppelen",
    "CONNECTION_ERROR": "Node <b>{{server}}</b> onbereikbaar of ongeldig adres.<br/><br/>Controleer de internetverbinding, of schakel knooppunt <a class=\"positive\" ng-click=\"doQuickFix('settings')\">in parameters</a>."
  },
  "SETTINGS": {
    "TITLE": "Instellingen",
    "NETWORK_SETTINGS": "Netwerk",
    "PEER": "Duniter knooppunt adres",
    "PEER_CHANGED_TEMPORARY": "Adres tijdelijk worden gebruikt",
    "USE_LOCAL_STORAGE": "Lokale opslag inschakelen",
    "USE_LOCAL_STORAGE_HELP": "Slaat uw instellingen",
    "ENABLE_HELPTIP": "Contextgebonden hulp inschakelen",
    "ENABLE_UI_EFFECTS": "Schakel visuele effecten",
    "HISTORY_SETTINGS": "Mijn rekening",
    "DISPLAY_UD_HISTORY": "Toon geproduceerde dividenden?",
    "AUTHENTICATION_SETTINGS": "Authentificatie",
    "REMEMBER_ME": "Onthoud mij",
    "REMEMBER_ME_HELP": "Hiermee kunt u blijven altijd aangesloten (niet aanbevolen).",
    "PLUGINS_SETTINGS": "Uitbreidingen",
    "BTN_RESET": "Herstel standaardinstellingen",
    "EXPERT_MODE": "Geavanceerde modus inschakelen",
    "EXPERT_MODE_HELP": "Toon meer details",
    "POPUP_PEER": {
      "TITLE" : "Duniter Knooppunt",
      "HOST" : "Adres",
      "HOST_HELP": "Aadres: server:poort",
      "USE_SSL" : "Secure?",
      "USE_SSL_HELP" : "(SSL-encryptie)",
      "BTN_SHOW_LIST" : "Lijst van knooppunten"
    }
  },
  "BLOCKCHAIN": {
    "HASH": "Hachee : {{hash}}",
    "VIEW": {
      "HEADER_TITLE": "Blok #{{number}}-{{hash|formatHash}}",
      "TITLE_CURRENT": "Huidige blok",
      "TITLE": "Blok #{{number|formatInteger}}",
      "COMPUTED_BY": "Berekend door het knooppunt",
      "SHOW_RAW": "Bekijk RAW-bestand",
      "TECHNICAL_DIVIDER": "Technische informatie",
      "VERSION": "Format versie",
      "HASH": "Hash berekend",
      "UNIVERSAL_DIVIDEND_HELP": "Munt gecoproduceerd door elk van de {{membersCount}} ledental",
      "EMPTY": "Er zijn geen gegevens in dit blok",
      "POW_MIN": "Mminimum moeilijkheid",
      "POW_MIN_HELP": "Moeilijkheid opgelegd hash te berekenen",
      "DATA_DIVIDER": "Gegevens",
      "IDENTITIES_COUNT": "Nieuwe identiteiten",
      "JOINERS_COUNT": "Nieuwe leden",
      "ACTIVES_COUNT": "Verlengingen",
      "ACTIVES_COUNT_HELP": "Leden die hun lidmaatschap te vernieuwen",
      "LEAVERS_COUNT": "Verlaters",
      "LEAVERS_COUNT_HELP": "Leden die niet langer wenst certificering",
      "EXCLUDED_COUNT": "Uitgesloten leden",
      "EXCLUDED_COUNT_HELP": "Oud-leden uitgesloten door niet-verlenging of gebrek aan certificeringen",
      "REVOKED_COUNT": "Identiteiten ingetrokken",
      "REVOKED_COUNT_HELP": "Deze rekeningen zullen niet langer leden",
      "TX_COUNT": "Transacties",
      "CERT_COUNT": "Certificeringen",
      "TX_TO_HIMSELF": "Ruil deal",
      "TX_OUTPUT_UNLOCK_CONDITIONS": "Omstandigheden van de introductie",
      "TX_OUTPUT_OPERATOR": {
        "AND": "en",
        "OR": "of"
      },
      "TX_OUTPUT_FUNCTION": {
        "SIG": "<b>handtekening</b> ",
        "XHX": "<b>Wachtwoord</b>, wiens SHA256 =",
        "CSV": "Geblokkeerd",
        "CLTV": "Opgesloten"
      }
    },
    "LOOKUP": {
      "TITLE": "Blokken",
      "NO_BLOCK": "Geen blok",
      "LAST_BLOCKS": "Recente blokken :",
      "BTN_COMPACT": "Compact"
    }
  },
  "CURRENCY": {
    "SELECT": {
      "TITLE": "Valuta's",
      "CURRENCIES": "Bekende valuta's",
      "MEMBERS_COUNT": "{{membersCount}} leden"
    },
    "VIEW": {
      "TITLE": "Valuta",
      "TAB_CURRENCY": "Valuta",
      "TAB_WOT": "Gemeenschap",
      "TAB_NETWORK": "Netwerk",
      "CURRENCY_NAME": "Valuta naam",
      "MEMBERS": "Ledental",
      "MEMBERS_VARIATION": "Variatie since {{duration | formatDuration}}",
      "MONEY_DIVIDER": "Geld",
      "MASS": "Monetaire massa",
      "SHARE": "Aandeel per lid",
      "UD": "Universeel Dividend",
      "C_ACTUAL": "Huidige toename",
      "MEDIAN_TIME": "Blockchain tijd",
      "POW_MIN": "Algemene moeilijkheidsgraad",
      "MONEY_RULES_DIVIDER": "Monetaire regels",
      "C_RULE": "Toename",
      "UD_RULE": "Universeel dividend (formule)",
      "SIG_QTY_RULE": "Benodigd aantal certificaties om lid te worden",
      "SIG_STOCK": "Maximum aantal certificaties te versturen per lid",
      "SIG_PERIOD": "Minimum vertraging tussen 2 certificaties verzonden door één en dezelfde persoon.",
      "SIG_WINDOW": "Maximum vertraging voor een certificatie in behandeling wordt genomen",
      "STEP_MAX": "Maximum afstand tussen elk WoT lid en een nieuw lid.",
      "WOT_RULES_DIVIDER": "Lidmaatschapseisen",
      "XPERCENT":"Minimum percentage schildwachten te bereiken om de afstandsregel te respecteren"
    }
  },
  "NETWORK": {
    "VIEW": {
      "MEDIAN_TIME": "Blockchain tijd",
      "LOADING_PEERS": "Even geduld...",
      "NODE_ADDRESS": "Adres :",
      "ENDPOINTS": {
        "BMAS": "Endpoint (SSL)",
        "BMATOR": "Endpoint TOR",
        "ES_USER_API": "Knoop Cesium+"
      }
    },
    "INFO": {
      "ONLY_SSL_PEERS": "Les noeuds non SSL ont un affichage dégradé, car Cesium fonctionne en mode HTTPS."
    }
  },
  "PEER": {
    "PEERS": "Knopen",
    "SIGNED_ON_BLOCK": "Getekend op blok",
    "MIRROR": "spiegel",
    "CURRENT_BLOCK": "Blok #",
    "VIEW": {
      "TITLE": "Knoop",
      "OWNER": "Maakt deel uit van",
      "SHOW_RAW_PEERING": "Zie netwerkdocument",
      "KNOWN_PEERS": "Bekende knopen :",
      "GENERAL_DIVIDER": "Algemene informatie",
      "ERROR": {
        "LOADING_TOR_NODE_ERROR": "Kan knooppunt niet worden opgehaald. De wachttijd wordt overschreden.",
        "LOADING_NODE_ERROR": "Kan knooppunt niet worden opgehaald"
      }
    }
  },
  "WOT": {
    "SEARCH_HELP": "Zoeken (lid of publieke sleutel)",
    "SEARCH_INIT_PHASE_WARNING": "Tijdens de pre-registratiefase, het zoeken van lopende registraties <b>kan lang</b> zijn. Dank je wel geduld...",
    "REGISTERED_SINCE": "Registratie",
    "REGISTERED_SINCE_BLOCK": "Geregistreerd op blok #",
    "NO_CERTIFICATION": "Geen gevalideerde certificaties",
    "NO_GIVEN_CERTIFICATION": "Geen uitgegeven certificaties",
    "NOT_MEMBER_PARENTHESIS": "(niet-lid)",
    "IDENTITY_REVOKED_PARENTHESIS": "(ingetrokken identiteit)",
    "MEMBER_PENDING_REVOCATION_PARENTHESIS": "(en cours de révocation)",
    "EXPIRE_IN": "Verloopt",
    "NOT_WRITTEN_EXPIRE_IN": "Uiterlijke<br/>behandeling",
    "EXPIRED": "Verlopen",
    "PSEUDO": "Pseudoniem",
    "SIGNED_ON_BLOCK": "Uitgegeven op block #{{block}}",
    "WRITTEN_ON_BLOCK": "Geschreven op block #{{block}}",
    "GENERAL_DIVIDER": "Algemene informatie",
    "NOT_MEMBER_ACCOUNT": "Geen lidaccount",
    "NOT_MEMBER_ACCOUNT_HELP": "Dit is een eenvoudige account zonder te wachten lidmaatschap aanvragen.",
    "TECHNICAL_DIVIDER": "Technishe informatie",
    "BTN_CERTIFY": "Certificeren",
    "BTN_YES_CERTIFY": "Ja, Certificeren",
    "BTN_SELECT_AND_CERTIFY": "Nieuwe certificatie",
    "ACCOUNT_OPERATIONS": "Operaties op de rekening",
    "VIEW": {
      "POPOVER_SHARE_TITLE": "Identiteit {{title}}"
    },
    "LOOKUP": {
      "TITLE": "Register",
      "NEWCOMERS": "Nieuwe leden:",
      "PENDING": "Aspirant leden:",
      "REGISTERED": "Geregistreerd {{sigDate | formatFromNow}}",
      "MEMBER_FROM": "Lid sinds {{memberDate|formatFromNowShort}}",
      "BTN_NEWCOMERS": "Nieuwste leden",
      "BTN_PENDING": "Registraties in afwachting",
      "SHOW_MORE": "Toon meer",
      "SHOW_MORE_COUNT": "(huidige limiet op {{limit}})",
      "NO_PENDING": "Er zijn geen registraties in afwachting gevonden.",
      "NO_NEWCOMERS": "Er zijn geen nieuwe leden gevonden."
    },
    "MODAL": {
      "TITLE": "Zoeken"
    },
    "CERTIFICATIONS": {
      "TITLE": "{{uid}} - Certificaties",
      "SUMMARY": "Ontvangen certificaties",
      "LIST": "Details van ontvangen certificaties",
      "PENDING_LIST": "Certificaties in afwachting",
      "RECEIVED": "Ontvangen certificaties",
      "RECEIVED_BY": "Certificaties ontvanged door {{uid}}",
      "ERROR": "Ontvangen vertificaties met fout",
      "SENTRY_MEMBER": "Referent lid"
    },
    "GIVEN_CERTIFICATIONS": {
      "TITLE": "{{uid}} - Verzonden certificaties",
      "SUMMARY": "Verzonden certificaties",
      "LIST": "Details van verzonden certificaties",
      "PENDING_LIST": "Certificaties in afwachting",
      "SENT": "Verzonden certificaties",
      "SENT_BY": "Certificaties verzonden door {{uid}}",
      "ERROR": "Verzonden certificaties met fout"
    }
  },
  "LOGIN": {
    "TITLE": "<i class=\"icon ion-locked\"></i> Inloggen",
    "SALT": "Beveiligingszin",
    "SALT_HELP": "Zin ter beveiliging van uw rekening",
    "SHOW_SALT": "Toon de beveiligingszin",
    "PASSWORD": "Wachtwoord",
    "PASSWORD_HELP": "Wachtwoord ter beveiliging van uw rekening",
    "NO_ACCOUNT_QUESTION": "Nog geen rekening?",
    "CREATE_ACCOUNT": "Open een rekening",
    "FORGOTTEN_ID": "Wachtwoord vergeten?"
  },
  "ACCOUNT": {
    "TITLE": "Mijn rekening",
    "BALANCE": "Saldo",
    "LAST_TX": "Recente transacties",
    "BALANCE_ACCOUNT": "Rekeningsaldo",
    "NO_TX": "Geen transacties",
    "SHOW_MORE_TX": "Show more",
    "SHOW_ALL_TX": "Show all",
    "TX_FROM_DATE": "(huidige limiet op {{fromTime|formatFromNowShort}})",
    "PENDING_TX": "Transacties in afwachting",
    "ERROR_TX": "Niet uitgevoerde transacties",
    "ERROR_TX_SENT": "Verzonden transacties",
    "ERROR_TX_RECEIVED": "Ontvangen transacties",
    "EVENTS": "Gebeurtenissen",
    "WAITING_MEMBERSHIP": "Lidmaatschapsverzoek verzonden. In afwachting van validatie.",
    "WAITING_CERTIFICATIONS": "Je hebt {{needCertificationCount}} certificatie(s) nodig om lid te worden",
    "WILL_MISSING_CERTIFICATIONS": "Je heeft binnenkort <b>onvoldoende certificaties</b> (ten minste {{willNeedCertificationCount}} benodigd)",
    "WILL_NEED_RENEW_MEMBERSHIP": "Je lidmaatschap <b>gaat verlopen op {{membershipExpiresIn|formatDurationTo}}</b>. Vergeet niet <a ng-click=\"doQuickFix('renew')\">je lidmaatschap te vernieuwen</a> voor die tijd.",
    "CERTIFICATION_COUNT": "Aantal certificaties",
    "CERTIFICATION_COUNT_SHORT": "Certificaties",
    "SIG_STOCK": "Voorraad uit te geven certificaties",
    "BTN_RECEIVE_MONEY": "Ontvangen",
    "BTN_MEMBERSHIP_IN_DOTS": "Lidmaatschap aanvragen...",
    "BTN_MEMBERSHIP_RENEW": "Lidmaatschap verlengen",
    "BTN_MEMBERSHIP_RENEW_DOTS": "Lidmaatschap verlengen...",
    "BTN_MEMBERSHIP_OUT_DOTS": "Lidmaatschap opzeggen...",
    "BTN_SEND_IDENTITY_DOTS": "Identiteit publiceren...",
    "BTN_SECURITY_DOTS": "Account en veiligheid...",
    "BTN_SHOW_DETAILS": "Tonen technische informatie",
    "BTN_REVOKE": "Deze identiteit<span class='hidden-xs hidden-sm'> definitief</span> opzeggen...",
    "NEW": {
      "TITLE": "Registratie",
      "SLIDE_1_TITLE": "Selecteer een valuta:",
      "SLIDE_2_TITLE": "Soort rekening:",
      "MEMBER_ACCOUNT": "Persoonlijke rekening (lidmaatschap)",
      "MEMBER_ACCOUNT_HELP": "Als je nog niet als individu geregistreerd bent (één rekening per individu mogelijk).",
      "WALLET_ACCOUNT": "Eenvoudige portefeille",
      "WALLET_ACCOUNT_HELP": "Als je een onderneming, stichting etc. vertegenwoordigd of eenvoudigweg een additionele portefeille nodig hebt. Geen individueel universeel dividend zal door deze rekening gecréeerd worden.",
      "SALT_WARNING": "Kies een beveiligingszin.<br/>Deze heb je nodig voor ieder verbinding met je account.<br/><br/><b>Zorg dat je deze zin goed onthoud</b>.<br/>Eenmaal verloren, is er geen mogelijkheid om hem te achterhalen!",
      "PASSWORD_WARNING": "Kies een wachtwoord.<br/>Deze heb je nodig voor ieder verbinding met je account.<br/><br/><b>Zorg dat je dit woord goed onthoud</b>.<br/>Eenmaal verloren, is er geen mogelijkheid om hem te achterhalen!",
      "PSEUDO_WARNING": "Kies een pseudoniem.<br/>Het dient om makkelijker gevonden te worden door anderen.<br/><br/>.Gebruik van spaties, komma's en accenten is niet toegestaan.<br/><div class='hidden-xs'><br/>Voorbeeld: <span class='gray'>JulesDeelder, JohanVermeer, etc.</span>",
      "PSEUDO": "Pseudoniem",
      "PSEUDO_HELP": "joe123",
      "SALT_CONFIRM": "Bevestig",
      "SALT_CONFIRM_HELP": "Bevestig de beveiligingszin",
      "PASSWORD_CONFIRM": "Bevestig",
      "PASSWORD_CONFIRM_HELP": "Bevestig het wachtwoord",
      "SLIDE_6_TITLE": "Bevestiging:",
      "COMPUTING_PUBKEY": "Berekening...",
      "LAST_SLIDE_CONGRATULATION": "Bravo! Je hebt alle verplichte velden ingevuld.<br/>Je kunt je <b>rekeningaanvraag verzenden</b>.<br/><br/>Ter informatie, de publieke sleutel hieronder identificeert je toekomstige rekening.<br/>Je kunt deze aan derde partijen communiceren om geld te ontvangen. Zodra je rekening geopend is, kun je de sleutel terugvinden onder <b>{{'ACCOUNT.TITLE'|translate}}</b>.",
      "CONFIRMATION_MEMBER_ACCOUNT": "<b class=\"assertive\">Waarschuwing:</b> je beveiligingszin, wachtwoord en pseudoniem kunnen hierna niet gewijzigd worden.<br/><b>Zorg dat ze goed onthoudt!</b><br/><b>Weet je zeker</b> dat je je persoonlijke rekeningaanvraag wil verzenden?",
      "CONFIRMATION_WALLET_ACCOUNT": "<b class=\"assertive\">Waarschuwing:</b> je wachtwoord en pseudoniem kunnen hierna niet gewijzigd worden.<br/><b>Zorg dat ze goed onthoudt!</b><br/><b>Weet je zeker</b> dat je deze portefeilleaanvraag wil verzenden?",
      "PSEUDO_AVAILABLE": "Deze naam is beschikbaar",
      "PSEUDO_NOT_AVAILABLE": "Deze gebruikersnaam is niet beschikbaar",
      "INFO_LICENSE": "Om de valuta te sluiten, vragen wij u om te lezen en deze licentie te accepteren.",
      "BTN_ACCEPT": "Ik accepteer",
      "BTN_ACCEPT_LICENSE": "Ik ga akkoord met de licentie"
    },
    "POPUP_REGISTER": {
      "TITLE": "Voer een pseudoniem in",
      "HELP": "Een pseudoniem is nodig voor anderen om je te kunnen vinden."
    },
    "FILE_NAME": "{{currency}} - Rekeningafschrift {{pubkey|formatPubkey}} {{currentTime|formatDateForFile}}.csv",
    "HEADERS": {
      "TIME": "Datum",
      "AMOUNT": "Bedrag",
      "COMMENT": "Commentaar"
    }
  },
  "TRANSFER": {
    "TITLE": "Overboeken",
    "SUB_TITLE": "Geld overboeken",
    "FROM": "Van",
    "TO": "Aan",
    "AMOUNT": "Bedrag",
    "AMOUNT_HELP": "Bedrag",
    "COMMENT": "Opmerking",
    "COMMENT_HELP": "Opmerking (optioneel)",
    "BTN_SEND": "Verzenden",
    "BTN_ADD_COMMENT": "Opmerking toevoegen",
    "WARN_COMMENT_IS_PUBLIC": "Houd er rekening mee dat <b>reacties openbaar zijn </b> (niet-versleuteld).",
    "MODAL": {
      "TITLE": "Overboeking"
    }
  },
  "ERROR": {
    "POPUP_TITLE": "Error",
    "UNKNOWN_ERROR": "Unknown error",
    "CRYPTO_UNKNOWN_ERROR": "Your browser is not compatible with cryptographic features.",
    "FIELD_REQUIRED": "This field is required.",
    "FIELD_TOO_SHORT": "Value is too short (min {{minLength]] characters).",
    "FIELD_TOO_SHORT_WITH_LENGTH": "This field value is too short.",
    "FIELD_TOO_LONG": "Value is exceeding max length.",
    "FIELD_TOO_LONG_WITH_LENGTH": "Value is too long (max {{maxLength}} characters).",
    "FIELD_ACCENT": "Commas and accent characters not allowed",
    "FIELD_NOT_NUMBER": "Value is not a number",
    "FIELD_NOT_INT": "Value is not an integer",
    "PASSWORD_NOT_CONFIRMED": "Must match previous password.",
    "SALT_NOT_CONFIRMED": "Must match previous phrase.",
    "SEND_IDENTITY_FAILED": "Error while trying to register.",
    "SEND_CERTIFICATION_FAILED": "Could not certify identity.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY": "You could not send certification, because your account is <b>not a member account</b>.",
    "NEED_MEMBER_ACCOUNT_TO_CERTIFY_HAS_SELF": "You could not send certification now, because your are <b>not a member</b> yet.<br/><br/>You still need certification to become a member.",
    "NOT_MEMBER_FOR_CERTIFICATION": "Your account is not a member account yet.",
    "IDENTITY_TO_CERTIFY_HAS_NO_SELF": "This account could not be certified. No registration found, or need to renew.",
    "LOGIN_FAILED": "Error while sign in.",
    "LOAD_IDENTITY_FAILED": "Could not load identity.",
    "LOAD_REQUIREMENTS_FAILED": "Could not load identity requirements.",
    "SEND_MEMBERSHIP_IN_FAILED": "Error while sending registration as member.",
    "SEND_MEMBERSHIP_OUT_FAILED": "Error while sending membership revocation.",
    "REFRESH_WALLET_DATA": "Could not refresh wallet.",
    "GET_CURRENCY_PARAMETER": "Could not get currency parameters.",
    "GET_CURRENCY_FAILED": "Could not load currency.",
    "SEND_TX_FAILED": "Could not send transaction.",
    "ALL_SOURCES_USED": "Please wait the next block computation (All transaction sources has been used).",
    "NOT_ENOUGH_SOURCES": "Not enough changes to send this amount in one time.<br/>Maximum amount: {{amount}} {{unit}}<sub>{{subUnit}}</sub>.",
    "ACCOUNT_CREATION_FAILED": "Error while creating your member account.",
    "RESTORE_WALLET_DATA_ERROR": "Error while reloading settings from local storage",
    "LOAD_WALLET_DATA_ERROR": "Error while loading wallet data.",
    "COPY_CLIPBOARD_FAILED": "Could not copy to clipboard",
    "TAKE_PICTURE_FAILED": "Could not get picture.",
    "SCAN_FAILED": "Could not scan QR code.",
    "SCAN_UNKNOWN_FORMAT": "Code not recognized.",
    "WOT_LOOKUP_FAILED": "Search failed.",
    "LOAD_PEER_DATA_FAILED": "Duniter peer not accessible. Please retry later.",
    "NEED_LOGIN_FIRST": "Please sign in first.",
    "AMOUNT_REQUIRED": "Amount is required.",
    "AMOUNT_NEGATIVE": "Negative amount not allowed.",
    "NOT_ENOUGH_CREDIT": "Not enough credit.",
    "INVALID_NODE_SUMMARY": "Unreachable peer or invalid address",
    "INVALID_USER_ID": "Field 'pseudonym' must not contains spaces or special characters.",
    "INVALID_COMMENT": "Field 'reference' has a bad format.",
    "INVALID_PUBKEY": "Public key has a bad format.",
    "IDENTITY_INVALID_BLOCK_HASH": "This membership application is no longer valid (because it references a block that network peers are cancelled): the person must renew its application for membership <b>before</b> being certified.",
    "IDENTITY_EXPIRED": "This identity has expired: this person must re-apply <b>before</b> being certified.",
    "IDENTITY_SANDBOX_FULL": "Could not register, because peer's sandbox is full.<br/><br/>Please retry later or choose another Duniter peer (in <b>Settings</b>).",
    "WOT_PENDING_INVALID_BLOCK_HASH": "Membership not valid.",
    "WALLET_INVALID_BLOCK_HASH": "Your membership application is no longer valid (because it references a block that network peers are cancelled).<br/>You must <a ng-click=\"doQuickFix('renew')\">renew your application for membership</a> to fix this issue.",
    "WALLET_IDENTITY_EXPIRED": "The publication of your identity <b>has expired</b>.<br/>You must <a ng-click=\"doQuickFix('fixIdentity')\">re-issue your identity</a> to resolve this issue.",
    "WALLET_HAS_NO_SELF": "Your identity must first have been published, and not expired.",
    "IDENTITY_ALREADY_CERTIFY": "You have <b>already certified</b> that identity.<br/><br/>Your certificate is still valid (expires {{expiresIn|formatDuration}}).",
    "IDENTITY_ALREADY_CERTIFY_PENDING": "You have <b>already certified</b> that identity.<br/><br/>Your certification is still pending (Deadline for treatment {{expiresIn|formatDuration}}).",
    "UNABLE_TO_CERTIFY_TITLE": "Unable to certify",
    "LOAD_NEWCOMERS_FAILED": "Unable to load new members.",
    "LOAD_PENDING_FAILED": "Unable to load pending registrations.",
    "ONLY_MEMBER_CAN_EXECUTE_THIS_ACTION": "You must <b>be a member</b> in order to perform this action.",
    "ONLY_SELF_CAN_EXECUTE_THIS_ACTION": "You must have <b>published your identity</b> in order to perform this action.",
    "EXISTING_ACCOUNT": "Uw wachtwoord overeenkomen met een bestaande account, de <a ng-click=\"showHelpModal('pubkey')\">publieke sleutel</a> is:",
    "EXISTING_ACCOUNT_REQUEST": "Gelieve uw wachtwoord wijzigen om een ongebruikte account."
  },
  "INFO": {
    "POPUP_TITLE": "Informatie",
    "CERTIFICATION_DONE": "Identiteit succesvol getekend",
    "NOT_ENOUGH_CREDIT": "Niet genoeg krediet",
    "TRANSFER_SENT": "Verzoek tot overboeken succesvol verzonden",
    "COPY_TO_CLIPBOARD_DONE": "Kopie geslaagd",
    "MEMBERSHIP_OUT_SENT": "Opzegging lidmaatschap succesvol verzonden",
    "NOT_NEED_MEMBERSHIP": "Je bent al lid.",
    "IDENTITY_WILL_MISSING_CERTIFICATIONS": "Deze identiteit heeft binnenkort onvoldoende certificaties (ten minste {{willNeedCertificationCount}} nodig)."
  },
  "CONFIRM": {
    "POPUP_TITLE": "<b>Bevestiging</b>",
    "POPUP_WARNING_TITLE": "<b>Waarschuwing</b>",
    "CERTIFY_RULES_TITLE_UID": "Certificeer {{uid}}",
    "CERTIFY_RULES": "<b>Beveiligingswaarschuwing:</b><br/><br/><b class=\"assertive\">Certificeer een rekening niet</b> als je gelooft dat: <ul><li>1.) de aanvrager niet echt is.<li>2.) de aanvrager al een andere gecertificeerde rekening heeft.<li>3.) de aanvrager opzettelijk of door onzorgvuldigheid regel 1 of 2 overtreedt bij het verzenden van certificaten.</ul></small><br/>Weet je zeker dat je deze identieit wilt certificeren?",
    "TRANSFER": "<b>Samenvatting van de overboeking:</b><br/><br/><ul><li> - Van: <b>{{from}}</b></li><li> - Aan: <b>{{to}}</b></li><li> - Bedrag: <b>{{amount}} {{unit}}</b></li><li> - Opmerking: <i>{{comment}}</i></li></ul><br/><b>Weet je zeker dat je deze overboeking wil doen?</b>",
    "MEMBERSHIP_OUT": "<b>Waarschuwing</b>:<br/>Je staat op het punt je lidmaatschap te beëindigen. Dit kan <b>niet ongedaan</b> worden gemaakt.<br/></br/><b>Weet je zeker dat je door wil gaan?</b>",
    "LOGIN_UNUSED_WALLET_TITLE": "Typefout?",
    "LOGIN_UNUSED_WALLET": "Je bent ingelogged op een rekening die <b>inactief</b> lijkt te zijn.<br/><br/>Als deze rekening niet met de uwe overeenkomt, komt dat waarschijnlijk door een <b>typefout</b> bij het inloggen.<br/><br/><b>Wilt u toch doorgaan met deze rekening?</b>",
    "FIX_IDENTITY": "De pseudoniem <b>{{uid}}</b> zal opnieuw gepubliceerd worden, waarmee de oude verlopen publicatie wordt vervangen.<br/></br/><bWeet je zeker</b> dat je door wil gaan?",
    "FIX_MEMBERSHIP": "Je verzoek to lidmaatschap zal verstuurd worden.<br/></br/><b>Weet je het zeker?</b>",
    "RENEW_MEMBERSHIP": "Je lidmaatschap zal verlengd worden.<br/></br/><b>Weet je het zeker?</b>",
    "REVOKE_IDENTITY": "<b>Beveiligingswaarschuwing:</b><br/>You will <b>definitely revoke this identity</b>.<br/><br/>The public key and the associated nickname <b>will never be used again</b> (for a member account).<br/></br/><b>Are you sure</b> you want to continue?",
    "REVOKE_IDENTITY_2": "Deze handeling is <b>niet terug te draaien</b>!<br/><br/><b>Weet je zeker</b> dat je door wil gaan?",
    "NOT_NEED_RENEW_MEMBERSHIP": "Je lidmaatschap hoeft niet verlengd te worden (het zal pas verlopen na {{membershipExpiresIn|formatDuration}}).<br/></br/><b>Weet je zeker</b> dat je een verlengingsaanvraag wil versturen?",
    "SAVE_BEFORE_LEAVE": "Wil je <b>je wijzigingen opslaan</b> voor je de pagina verlaat?",
    "SAVE_BEFORE_LEAVE_TITLE": "Wijzigingen niet opgeslagen",
    "LICENCE": "Ik heb gelezen en geaccepteerd de voorwaarden van de vergunning G1"
  },
  "DOWNLOAD": {
    "POPUP_TITLE": "<b>Intrekking File</b>",
    "POPUP_REVOKE_MESSAGE": "Om uw account te beveiligen, het downloaden van de <b>Account intrekking document</b>. U zult indien nodig om uw account (annuleren in het geval van de rekening van diefstal, een verandering van de identifier, een ten onrechte gemaakte account, etc.).<br/><br/><b>Bewaar deze op een veilige plaats.</b>"
  },
  "HELP": {
    "TITLE": "Online help",
    "JOIN": {
      "SECTION": "Join",
      "SALT": "The protection phrase is very important. It is used to hash you password, which in turn is used to calculate your <span class=\"text-italic\">public account key</span> (its number) and the private key to access it. <b>Please remeber this phrase well</b>, because there is no way to recover it when lost. What's more, it cannot be changed without having to create a new account.<br/><br/>A good protection phrase must be sufficiently long (8 characters at the very least) and as original as possible.",
      "PASSWORD": "The password is very important. Together with the protection phrase, it is use to calculate your account number (public key) and the private key to access it. <b>Please remember it well</b>, because there is no way to recover it when lost. What's more, it cannot be changed without having to create a new account.<br/><br/>A good password is made (ideally) of at least 8 characters, with at least one capital and one number.",
      "PSEUDO": "A pseudonym is used only when joining as <span class=\"text-italic\">member</span>. It is always associated with a wallet (by its <span class=\"text-italic\">public key</span>). It is published on the network so that other users may identify it, certify or send money to the account. A pseudonym must be unique among all members (current and past)."
    },
    "GLOSSARY": {
      "SECTION": "Glossary",
      "PUBKEY_DEF": "Een publieke sleutel identificeert altijd een portemonnee. Het kan een lid identificeren. In Cesium wordt berekend met de geheime ID en wachtwoord.",
      "MEMBER": "Member",
      "MEMBER_DEF": "A member is a real and living human, wishing to participate freely to the monitary community. The member will receive universal dividend, according to the period and amount as defined in the <span class=\"text-italic\">currency parameters</span>.",
      "CURRENCY_RULES": "Currency rules",
      "CURRENCY_RULES_DEF": "The currency rules are defined only once, and for all. They set the parameters under which the currency will perform: universal dividend calculation, the amount of certifications needed to become a member, the maximum amount of certifications a member can send, etc.<br/><br/>The parameters cannot be modified because of the use of a <span class=\"text-italic\">Blockchain</span> which carries and executes these rules, and constantly verifies their correct application. <a href=\"#/app/currency\">See current parameters</a>.",
      "BLOCKCHAIN": "Blockchain",
      "BLOCKCHAIN_DEF": "The Blockchain is a decentralised system which, in case of Duniter, serves to carry and execute the <span class=\"text-italic\">currency rules</span>.<br/><a href=\"http://en.duniter.org/presentation/\" target=\"_blank\">Read more about Duniter</a> and the working of its blockchain.",
      "UNIVERSAL_DIVIDEND_DEF": "The Universal Dividend (UD) is the quantity of money co-created by each member, according to the period and the calculation defined in the <span class=\"text-italic\">currency rules</span>.<br/>Every term, the members receive an equal amount of new money on their account.<br/><br/>The UD undergoes a steady growth, to remain fair under its members (current and future), calculated by an average life expectancy, as demonstrated in the Relative Theory of Money (RTM).<br/><a href=\"http://trm.creationmonetaire.info\" target=\"_system\">Read more about RTM</a> and open money."
    },
    "TIP": {
      "MENU_BTN_CURRENCY": "Menu <b>{{'MENU.CURRENCY'|translate}}</b> allows discovery of <b>currency parameters</b> and its state.",
      "CURRENCY_WOT": "The <b>member count</b> shows the <b>community's weight and evolution</b>.",
      "CURRENCY_MASS": "Shown here is the <b>total amount</b> currently in circulation and its <b>average distribution</b> per member.<br/><br/>This allows to estimate the <b>worth of any amount</b>, in respect to what <b>others own</b> on their account (on average).",
      "CURRENCY_UNIT_RELATIVE": "The unit used here (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) signifies that the amounts in {{currency|capitalize}} have been devided by the <b>Universal Dividend</b> (UD).<br/><br/><small>This relative unit is <b>relevant</b> because it is stable in contrast to the permanently growing monitary mass.</small>",
      "CURRENCY_CHANGE_UNIT": "This button allows to <b>switch the unit</b> to show amounts in <b>{{currency|capitalize}}</b>, undevided by the Universal Dividend (instead of in &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;).",
      "CURRENCY_CHANGE_UNIT_TO_RELATIVE": "This button allows to <b>switch the unit</b> to show amounts in &ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;, which is relative to the Universal Dividend (the amount co-produced by each member).",
      "CURRENCY_RULES": "The <b>rules</b> of the currency determine its <b>exact and predictible</b> performance.<br/><br/>As a true DNA of the currency these rules make the monetary code <b>transparent and understandable</b>.",
      "NETWORK_BLOCKCHAIN": "All monetary transactions are recoded in a <b>public and tamper proof</b> ledger, generally referred to as the <b>blockchain</b>.",
      "NETWORK_PEERS": "The <b>peers</b> shown here correspond to <b>computers that update and check</b> the blockchain.<br/><br/>The more active peers there are, the more <b>decentralised</b> and therefore trustworhty the currency becomes.",
      "NETWORK_PEERS_BLOCK_NUMBER": "This <b>number</b> indicates the peer's <b>latest validated block</b> (last page written in the ledger).<br/><br/>Green indicates that the block was equally validated by the <b>majority of other peers</b>.",
      "NETWORK_PEERS_PARTICIPATE": "<b>Each member</b>, equiped with a computer with Internet, <b>can participate, adding a peer</b> simply by <b>installing the Duniter software</b> (free/libre and open source). <a href=\"{{installDocUrl}}\" target=\"_system\">Read the installation manual &gt;&gt;</a>.",
      "MENU_BTN_ACCOUNT": "<b>{{'ACCOUNT.TITLE'|translate}}</b> allows access to your account balance and transaction history.",
      "MENU_BTN_ACCOUNT_MEMBER": "Here you can consult your account status, transaction history and your certifications.",
      "WALLET_CERTIFICATIONS": "Click here to reveiw the details of your certifications (given and received).",
      "WALLET_BALANCE": "Your account <b>balance</b> is shown here.",
      "WALLET_BALANCE_RELATIVE": "{{'HELP.TIP.WALLET_BALANCE'|translate}}<br/><br/>The used unit (&ldquo;<b>{{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub></b>&rdquo;) signifies that the amount in {{currency|capitalize}} has been divided by the <b>Universal Dividend</b> (UD) co-created by each member.<br/>At this moment, 1 UD equals {{currentUD}} {{currency|capitalize}}.",
      "WALLET_BALANCE_CHANGE_UNIT": "You can <b>change the unit</b> in which amounts are shown in <b><i class=\"icon ion-android-settings\"></i>&nbsp;{{'MENU.SETTINGS'|translate}}</b>.<br/><br/>For example, to display amounts <b>directly in {{currency|capitalize}}</b> instead of relative amounts.",
      "WALLET_SEND": "Issue a payment in just a few clicks.",
      "WALLET_SEND_NO_MONEY": "Issue a payment in just a few clicks.<br/>(Your balance does not allow this yet)",
      "WALLET_OPTIONS": "Please note that this button allows access to <b>other, less used actions</b>.<br/><br/>Don't forget to take a quick look, when you have a moment!",
      "WALLET_RECEIVED_CERTS": "This shows the list of persons that certified you.",
      "WALLET_CERTIFY": "The button <b>{{'WOT.BTN_SELECT_AND_CERTIFY'|translate}}</b> allows selecting an identity and certifying it.<br/><br/>Only users that are <b>already member</b> may certify others.",
      "WALLET_CERT_STOCK": "Your supply of certifications (to send) is limited to <b>{{sigStock}} certifications</b>.<br/><br/>This supply will replete itself over time, as and when earlier certifications expire.",
      "MENU_BTN_WOT": "The menu <b>{{'MENU.WOT'|translate}}</b> allows searching <b>users</b> of the currency (member or not).",
      "WOT_SEARCH_TEXT_XS": "To search in the registry, type the <b>first letters of a users pseudonym or public key</b>.<br/><br/>The search will start automatically.",
      "WOT_SEARCH_TEXT": "To search in the registry, type the <b>first letters of a users pseudonym or public key</b>.<br/><br/>Then hit <b>Enter</b> to start the search.",
      "WOT_SEARCH_RESULT": "Simply click a user row to view the details sheet.",
      "WOT_VIEW_CERTIFICATIONS": "The row <b>{{'ACCOUNT.CERTIFICATION_COUNT'|translate}}</b> shows how many members members validated this identity.<br/><br/>These certifications testify that the account belongs to <b>a living human</b> and this person has <b>no other member account</b>.",
      "WOT_VIEW_CERTIFICATIONS_COUNT": "There are at least <b>{{sigQty}} certifications</b> needed to become a member and receive the <b>Universal Dividend</b>.",
      "WOT_VIEW_CERTIFICATIONS_CLICK": "Click here to open <b>a list of all certifications</b> given to and by this identity.",
      "WOT_VIEW_CERTIFY": "The button <b>{{'WOT.BTN_CERTIFY'|translate}}</b> allows to add your certification to this identity.",
      "CERTIFY_RULES": "<b>Attention:</b> Only certify <b>real and living persons</b> that do not own any other certified account.<br/><br/>The trust carried by the currency depends on each member's vigilance!",
      "MENU_BTN_SETTINGS": "The <b>{{'MENU.SETTINGS'|translate}}</b> allow you to configure the Cesium application.<br/><br/>For example, you can <b>change the unit</b> in which the currency will be shown.",
      "HEADER_BAR_BTN_PROFILE": "Click here to access your <b>user profile</b>",
      "SETTINGS_CHANGE_UNIT": "You can <b>change the display unit</b> of amounts by clicking here.<br/><br/>- Deactivate the option to show amounts in {{currency|capitalize}}.<br/>- Activate the option for relative amounts in {{'COMMON.UD'|translate}}<sub>{{currency|abbreviate}}</sub> (<b>divided</b> by the current Universal Dividend).",
      "END_LOGIN": "This guided visit has <b>ended</b>.<br/><br/>Welcome to the <b>free economy</b>!",
      "END_NOT_LOGIN": "This guided visit has <b>ended</b>.<br/><br/>If you wish to join the currency {{currency|capitalize}}, simply click <b>{{'LOGIN.CREATE_ACCOUNT'|translate}}</b> below."
    }
  }
}
);
}]);

// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('cesium-api', ['ionic', 'ionic-material', 'ngMessages', 'pascalprecht.translate', 'ngApi', 'angular-cache', 'angular.screenmatch',
  'cesium.filters', 'cesium.config', 'cesium.platform', 'cesium.templates', 'cesium.translations', 'cesium.directives',
  // API dependencies :
  'cesium.services', 'cesium.api.demo.services', 'cesium.login.controllers', 'cesium.help.controllers'
])

  .config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    'ngInject';
    $stateProvider

      .state('app', {
        url: "/app",
        abstract: true,
        templateUrl: "templates/api/menu.html",
        controller: 'ApiCtrl'
      })

      .state('app.home', {
        url: "/home?result&service&cancel",
        views: {
          'menuContent': {
            templateUrl: "templates/api/home.html",
            controller: 'ApiDocCtrl'
          }
        }
      })

      .state('api', {
        url: "/v1",
        abstract: true,
        templateUrl: "templates/api/menu.html",
        controller: 'ApiCtrl'
      })

      .state('api.transfer', {
        cache: false,
        url: "/payment/:pubkey?name&amount&udAmount&comment&redirect_url&cancel_url&demo",
        views: {
          'menuContent': {
            templateUrl: "templates/api/transfer.html",
            controller: 'ApiTransferCtrl'
          }
        }
      });

    // if none of the above states are matched, use this as the fallback
    $urlRouterProvider.otherwise('/app/home');
  }])

  .controller('ApiCtrl', ['$scope', '$state', '$translate', '$ionicPopover', 'Modals', 'csSettings', function ($scope, $state, $translate, $ionicPopover, Modals, csSettings){
    'ngInject';


    $scope.showAboutModal = function(e) {
      e.preventDefault(); // avoid to open link href
      Modals.showAbout();
    };

    $scope.showHome = function() {
      $state.go('app.home') ;
    };

    $scope.changeLanguage = function(langKey) {
      $translate.use(langKey);
      $scope.hideLocalesPopover();
      csSettings.data.locale = _.findWhere($scope.locales, {id: langKey});
    };

    /* -- show/hide locales popup -- */

    $scope.showLocalesPopover = function(event) {
      if (!$scope.localesPopover) {
        // Fill locales
        $scope.locales = angular.copy(csSettings.locales);

        $ionicPopover.fromTemplateUrl('templates/api/locales_popover.html', {
          scope: $scope
        }).then(function(popover) {
          $scope.localesPopover = popover;
          //Cleanup the popover when we're done with it!
          $scope.$on('$destroy', function() {
            $scope.localesPopover.remove();
          });
          $scope.localesPopover.show(event);
        });
      }
      else {
        $scope.localesPopover.show(event);
      }
    };

    $scope.hideLocalesPopover = function() {
      if ($scope.localesPopover) {
        $scope.localesPopover.hide();
      }
    };
  }])

  .controller('ApiDocCtrl', ['$scope', '$rootScope', '$state', '$translate', '$sce', 'csCurrency', function ($scope, $rootScope, $state, $translate, $sce, csCurrency){
    'ngInject';

    $scope.loading = true;
    $scope.transferData = {
      pubkey: 'G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU',
      amount: 100,
      comment: 'REFERENCE',
      name: 'www.domain.com',
      redirect_url: 'http://www.domain.com/payment?ref={comment}&tx={tx}',
      cancel_url: 'http://www.domain.com/payment?ref={comment}&cancel'
    };
    $scope.transferButton = {
      html: undefined,
      showParameters: false, // hide integration parameters, by default
      icons: [
        {
          label: 'API.DOC.TRANSFER.EXAMPLE_BUTTON_ICON_NONE'
        },
        {
          label: 'API.DOC.TRANSFER.EXAMPLE_BUTTON_ICON_DUNITER',
          filename: '../img/logo_duniter_32px.png'
        },
        {
          label: 'API.DOC.TRANSFER.EXAMPLE_BUTTON_ICON_CESIUM',
          filename: '../img/logo_32px.png'
        },
        {
          label: 'API.DOC.TRANSFER.EXAMPLE_BUTTON_ICON_G1_COLOR',
          filename: '../img/logo_g1_32px.png'
        },
        {
          label: 'API.DOC.TRANSFER.EXAMPLE_BUTTON_ICON_G1_BLACK',
          filename: '../img/logo_g1_32px_black.png'
        }
      ],
      style: {
        enable: false,
        text: 'Ğ1 pubkey',
        bgColor: '#fbc14c',
        fontColor: 'black',
        width: undefined
      }
    };
    $scope.transferButton.style.icon = $scope.transferButton.icons[1/*Duniter icon*/];
    $scope.transferDemoUrl = $rootScope.rootPath + $state.href('api.transfer', angular.merge({}, $scope.transferData, {
        demo: true,
        redirect_url: $rootScope.rootPath + '#/app/home?service=payment&result={tx}',
        cancel_url: $rootScope.rootPath + '#/app/home?service=payment&cancel'
      }));

    $scope.enter = function(e, state) {
      if (!$scope.loading) return; // already enter

      $scope.result = {};
      if (state.stateParams && state.stateParams.service) {
        $scope.result.type = state.stateParams.service;
      }
      if (state.stateParams && state.stateParams.result) {
        $scope.result.content = state.stateParams.result;
      }
      if (state.stateParams && state.stateParams.cancel) {
        $scope.result.cancelled = true;
      }

      csCurrency.get()
        .then(function(currency) {
          return $translate('API.DOC.TRANSFER.EXAMPLE_BUTTON_DEFAULT_TEXT', {currency: currency.name});
        })
        .then(function(buttonText) {
          $scope.transferButton.style.text = buttonText;
          $scope.loading = false;

          // compute HTML button
          $scope.computeTransferButtonHtml();
        });

    };
    $scope.$on('$ionicView.enter', $scope.enter);

    // watch from update
    $scope.computeTransferButtonHtml = function() {
      if ($scope.loading) return; // skip if loading

      // Compute URL
      var url = $rootScope.rootPath + $state.href('api.transfer', $scope.transferData);

      var html;
      // Compute HTML: simple button
      if (!$scope.transferButton.style.enable){
        html = '<a href="'+url+'">\n'+
          '  <img src="'+$rootScope.rootPath + '../img/duniter_button.svg">\n'+
          '</a>';
      }
      // Compute HTML: advanced button
      else {
        html = '<a href="'+url+'">\n'+
          '  <div style="border-radius: 5px; min-height: 42px; text-align: center; padding: 5px; line-height: 30px; ';
        if ($scope.transferButton.style.width) {
          html += 'max-width: '+$scope.transferButton.style.width+'; ';
        }
        if ($scope.transferButton.style.bgColor) {
          html += 'background-color: '+$scope.transferButton.style.bgColor+'; ';
        }
        if ($scope.transferButton.style.fontColor) {
          html += 'color: '+$scope.transferButton.style.fontColor+'; ';
        }
        html += '">\n';
        if ($scope.transferButton.style.icon && $scope.transferButton.style.icon.filename) {
          html += '    <img style="vertical-align: middle;" src="'+$rootScope.rootPath + $scope.transferButton.style.icon.filename+'">\n';
        }
        html += '    ' + $scope.transferButton.style.text + '\n' +
          '  </div>\n' +
          '</a>';
      }

      if ($scope.transferButton.html != html) {
        $scope.transferButton.html = html;
        $scope.$broadcast('$$rebind::transferButton'); // force rebind
      }
    };
    $scope.$watch('transferData', $scope.computeTransferButtonHtml, true);
    $scope.$watch('transferButton.style', $scope.computeTransferButtonHtml, true);
  }])

  .controller('ApiTransferCtrl', ['$scope', '$rootScope', '$timeout', '$controller', '$state', '$q', '$translate', '$filter', 'BMA', 'CryptoUtils', 'UIUtils', 'csCurrency', 'csTx', 'csWallet', 'csDemoWallet', function ($scope, $rootScope, $timeout, $controller, $state, $q, $translate, $filter,
                                           BMA, CryptoUtils, UIUtils, csCurrency, csTx, csWallet, csDemoWallet){
    'ngInject';

    // Initialize the super class and extend it.
    angular.extend(this, $controller('AuthCtrl', {$scope: $scope}));

    $scope.loading = true;
    $scope.transferData = {
      amount: undefined,
      comment: undefined,
      pubkey: undefined,
      name: undefined,
      redirect_url: undefined,
      cancel_url: undefined
    };

    $scope.enter = function(e, state) {
      if (state.stateParams && state.stateParams.amount) {
        $scope.transferData.amount  = parseFloat(state.stateParams.amount.replace(new RegExp('[.,]'), '.')).toFixed(2) * 100;
      }
      if (state.stateParams && state.stateParams.pubkey) {
        $scope.transferData.pubkey = state.stateParams.pubkey;
      }
      if (state.stateParams && state.stateParams.name) {
        $scope.transferData.name= state.stateParams.name;
      }
      if (state.stateParams && state.stateParams.comment) {
        $scope.transferData.comment = state.stateParams.comment;
      }
      if (state.stateParams && state.stateParams.redirect_url) {
        $scope.transferData.redirect_url = state.stateParams.redirect_url;
      }
      if (state.stateParams && state.stateParams.cancel_url) {
        $scope.transferData.cancel_url = state.stateParams.cancel_url;
      }
      if (state.stateParams && state.stateParams.demo) {
        $scope.demo = true;
      }
    };
    $scope.$on('$ionicView.enter', $scope.enter);

    function onLogin(authData) {

      // User cancelled
      if (!authData) return $scope.onCancel();

      // Avoid multiple click
      if ($scope.sending) return;
      $scope.sending = true;

      var wallet = $scope.demo ? csDemoWallet.instance(authData) : csWallet.instance('api', BMA);

      UIUtils.loading.show();

      wallet.start({restore: false}/*skip restore from local storage*/)
        .then(function() {
          return wallet.login({auth: true, authData: authData});
        })
        .then(function(walletData) {
          $scope.login = true;

          UIUtils.loading.hide();
          return $scope.askTransferConfirm(walletData);
        })
        .then(function(confirm) {
          if (!confirm) return;

          // sent transfer
          return UIUtils.loading.show()
            .then(function(){
              var amount = parseInt($scope.transferData.amount); // remove 2 decimals on quantitative mode
              return wallet.transfer($scope.transferData.pubkey, amount, $scope.transferData.comment, false /*always quantitative mode*/);
            })
            .then(function(txRes) {

              UIUtils.loading.hide();
              return txRes;
            })
            .catch(function(err) {
              UIUtils.onError()(err);
              return false;
            });
        })
        .then(function(txRes) {
          if (txRes) {
            return $scope.onSuccess(txRes);
          }
          else {
            $scope.sending = false;
          }
        })
        .catch(function(err){
          // when user cancel
          if (err && err === 'CANCELLED') {
            return $scope.onCancel();
          }
          // When wallet is empty
          if (err && err === 'RETRY') {
            $scope.sending = false;
            return;
          }
          $scope.sending = false;
          UIUtils.onError()(err);
        });
    }


    $scope.askTransferConfirm = function(walletData) {
      return $translate(['COMMON.UD', 'COMMON.EMPTY_PARENTHESIS'])
        .then(function(translations) {
          return $translate('CONFIRM.TRANSFER', {
            from: walletData.isMember ? walletData.uid : $filter('formatPubkey')(walletData.pubkey),
            to: $scope.transferData.name || $filter('formatPubkey')($scope.transferData.pubkey),
            amount: $scope.transferData.amount / 100,
            unit: $filter('abbreviate')($rootScope.currency.name),
            comment: (!$scope.transferData.comment || $scope.transferData.comment.trim().length === 0) ? translations['COMMON.EMPTY_PARENTHESIS'] : $scope.transferData.comment
          });
        })
        .then(UIUtils.alert.confirm);
    };

    $scope.onSuccess = function(txRes) {
      if (!$scope.transferData.redirect_url) {
        return UIUtils.toast.show('INFO.TRANSFER_SENT');
      }

      return ($scope.transferData.name ? $translate('API.TRANSFER.INFO.SUCCESS_REDIRECTING_WITH_NAME', $scope.transferData) : $translate('API.TRANSFER.INFO.SUCCESS_REDIRECTING'))
        .then(function(message){
          return UIUtils.loading.show({template: message});
        })
        .then(function() {
          var url = $scope.transferData.redirect_url;
          // Make replacements
          url = url.replace(/\{pubkey\}/g, $scope.transferData.pubkey);
          url = url.replace(/\{hash\}/g, txRes.hash||'');
          url = url.replace(/\{comment\}/g, $scope.transferData.comment||'');
          url = url.replace(/\{amount\}/g, $scope.transferData.amount.toString());
          url = url.replace(/\{tx\}/g, encodeURI(txRes.tx));

          return $scope.redirectToUrl(url, 2500);
        });
    };

    $scope.onCancel = function() {
      if (!$scope.transferData.cancel_url) {
        $scope.formData.salt = undefined;
        $scope.formData.password = undefined;
        return; // nothing to do
      }

      return ($scope.transferData.name ? $translate('API.TRANSFER.INFO.CANCEL_REDIRECTING_WITH_NAME', $scope.transferData) : $translate('API.TRANSFER.INFO.CANCEL_REDIRECTING'))
        .then(function(message){
          return UIUtils.loading.show({template: message});
        })
        .then(function() {
          var url = $scope.transferData.cancel_url;
          // Make replacements - fix #548
          url = url.replace(/\{pubkey\}/g, $scope.transferData.pubkey);
          url = url.replace(/\{comment\}/g, $scope.transferData.comment||'');
          url = url.replace(/\{amount\}/g, $scope.transferData.amount.toString());

          return $scope.redirectToUrl(url, 1500);
        });
    };

    $scope.redirectToUrl = function(url, timeout) {
      if (!url) return;

      return $timeout(function() {
        // if iframe: send to parent
        if (window.top && window.top.location) {
          window.top.location.href = url;
        }
        else if (parent && parent.document && parent.document.location) {
          parent.document.location.href = url;
        }
        else {
          window.location.assign(url);
        }
        return UIUtils.loading.hide();
      }, timeout||0);
    };

    /* -- methods need by Login controller -- */

    $scope.setForm = function(form) {
      $scope.form = form;
    };
    $scope.closeModal = onLogin;

  }])

  .run(['csPlatform', function(csPlatform) {
    'ngInject';

    csPlatform.start();
  }])
;

angular.module('cesium.directives', [])

  // Add new compare-to directive (need for form validation)
  .directive("compareTo", function() {
      return {
          require: "?ngModel",
          link: function(scope, element, attributes, ngModel) {
            if (ngModel && attributes.compareTo) {
              ngModel.$validators.compareTo = function(modelValue) {
                  return modelValue == scope.$eval(attributes.compareTo);
              };

              scope.$watch(attributes.compareTo, function() {
                  ngModel.$validate();
              });
            }
          }
      };
  })

  // Add new different-to directive (need for form validation)
  .directive("differentTo", function() {
    return {
      require: "?ngModel",
      link: function(scope, element, attributes, ngModel) {
        if (ngModel && attributes.differentTo) {
          ngModel.$validators.differentTo = function(modelValue) {
            return modelValue != scope.$eval(attributes.differentTo);
          };

          scope.$watch(attributes.differentTo, function() {
            ngModel.$validate();
          });
        }
      }
    };
  })

  .directive('numberFloat', function() {
    var NUMBER_REGEXP = new RegExp('^[0-9]+([.,][0-9]+)?$');

    return {
      require: '?ngModel',
      link: function(scope, element, attributes, ngModel) {
        if (ngModel) {
          ngModel.$validators.numberFloat = function(value) {
            return ngModel.$isEmpty(value) || NUMBER_REGEXP.test(value);
          };
        }
      }
    };
  })

  .directive('numberInt', function() {
    var INT_REGEXP = new RegExp('^[0-9]+$');
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, ngModel) {
        if (ngModel) {
          ngModel.$validators.numberInt = function (value) {
            return ngModel.$isEmpty(value) || INT_REGEXP.test(value);
          };
        }
      }
    };
  })

  .directive('email', function() {
    var EMAIL_REGEXP = new RegExp('^[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$');
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, ngModel) {
        if (ngModel) {
          ngModel.$validators.email = function (value) {
            return ngModel.$isEmpty(value) || EMAIL_REGEXP.test(value);
          };
        }
      }
    };
  })

  .directive('requiredIf', function() {
    return {
      require: '?ngModel',
      link: function(scope, element, attributes, ngModel) {
        if (ngModel && attributes.requiredIf) {
          ngModel.$validators.required = function(value) {
            return !(scope.$eval(attributes.requiredIf)) || !ngModel.$isEmpty(value);
          };

          scope.$watch(attributes.requiredIf, function() {
            ngModel.$validate();
          });
        }
      }
    };
  })

  .directive('geoPoint', function() {
    return {
      require: '?ngModel',
      link: function(scope, element, attributes, ngModel) {
        if (ngModel) {
          ngModel.$validators.geoPoint = function(value) {
            return ngModel.$isEmpty(value) || (angular.isDefined(value.lat) && angular.isDefined(value.lon));
          };
        }
      }
    };
  })

  // Add a copy-on-click directive
  .directive('copyOnClick', ['$window', '$document', 'Device', 'UIUtils', function ($window, $document, Device, UIUtils) {
    'ngInject';
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        var showCopyPopover = function (event) {
          var value = attrs.copyOnClick;
          if (value && Device.clipboard.enable) {
            // copy to clipboard
            Device.clipboard.copy(value)
              .then(function(){
                 UIUtils.toast.show('INFO.COPY_TO_CLIPBOARD_DONE');
              })
              .catch(UIUtils.onError('ERROR.COPY_CLIPBOARD'));
          }
          else if (value) {
            var rows = value && value.indexOf('\n') >= 0 ? value.split('\n').length : 1;
            UIUtils.popover.show(event, {
              scope: scope,
              templateUrl: 'templates/common/popover_copy.html',
              bindings: {
                value: attrs.copyOnClick,
                rows: rows
              },
              autoselect: '.popover-copy ' + (rows <= 1 ? 'input' : 'textarea')
            });
          }
        };
        element.bind('click', showCopyPopover);
        element.bind('hold', showCopyPopover);
      }
    };
  }])

  // Add a select-on-click directive
  .directive('selectOnClick', ['$window', function ($window) {
    'ngInject';
      return {
          restrict: 'A',
          link: function (scope, element, attrs) {
              element.bind('click', function () {
                if ($window.getSelection && !$window.getSelection().toString() && this.value) {
                  this.setSelectionRange(0, this.value.length);
                }
              });
          }
      };
  }])

  .directive('activeLink', ['$location', function ($location) {
    'ngInject';
    return {
      restrict: 'A',
      link: function(scope, element, attrs, controller) {
        var clazz = attrs.activeLink;
        var path;
        if (attrs.activeLinkPathPrefix) {
          path = attrs.activeLinkPathPrefix.substring(1); //hack because path does not return including hashbang
          scope.location = $location;
          scope.$watch('location.path()', function (newPath) {
            if (newPath && newPath.indexOf(path) === 0) {
              element.addClass(clazz);
            } else {
              element.removeClass(clazz);
            }
          });
        }
        else if (attrs.href) {
          path = attrs.href.substring(1); //hack because path does not return including hashbang
          scope.location = $location;
          scope.$watch('location.path()', function (newPath) {
            if (newPath && newPath == path) {
              element.addClass(clazz);
            } else {
              element.removeClass(clazz);
            }
          });
        }
      }
    };
  }])

  // All this does is allow the message
  // to be sent when you tap return
  .directive('input', ['$timeout', function($timeout) {
    return {
      restrict: 'E',
      scope: {
        'returnClose': '=',
        'onReturn': '&',
        'onFocus': '&',
        'onBlur': '&'
      },
      link: function(scope, element, attr) {
        element.bind('focus', function(e) {
          if (scope.onFocus) {
            $timeout(function() {
              scope.onFocus();
            });
          }
        });
        element.bind('blur', function(e) {
          if (scope.onBlur) {
            $timeout(function() {
              scope.onBlur();
            });
          }
        });
        element.bind('keydown', function(e) {
          if (e.which == 13) {
            if (scope.returnClose) element[0].blur();
            if (scope.onReturn) {
              $timeout(function() {
                scope.onReturn();
              });
            }
          }
        });
      }
    };
  }])

  .directive('trustAsHtml', ['$sce', '$compile', '$parse', function($sce, $compile, $parse){
    return {
      restrict: 'A',
      compile: function (tElement, tAttrs) {
        var ngBindHtmlGetter = $parse(tAttrs.trustAsHtml);
        var ngBindHtmlWatch = $parse(tAttrs.trustAsHtml, function getStringValue(value) {
          return (value || '').toString();
        });
        $compile.$$addBindingClass(tElement);

        return function ngBindHtmlLink(scope, element, attr) {
          $compile.$$addBindingInfo(element, attr.trustAsHtml);

          scope.$watch(ngBindHtmlWatch, function ngBindHtmlWatchAction() {
            // we re-evaluate the expr because we want a TrustedValueHolderType
            // for $sce, not a string
            element.html($sce.getTrustedHtml($sce.trustAsHtml(ngBindHtmlGetter(scope))) || '');
            $compile(element.contents())(scope);
          });
        };
      }
    };
  }])

  /**
  * Close the current modal
  */
  .directive('modalClose', ['$ionicHistory', '$timeout', function($ionicHistory, $timeout) {
    return {
      restrict: 'AC',
      link: function($scope, $element) {
        $element.bind('click', function() {
          if ($scope.closeModal) {
            $ionicHistory.nextViewOptions({
              historyRoot: true,
              disableAnimate: true,
              expire: 300
            });
            // if no transition in 300ms, reset nextViewOptions
            // the expire should take care of it, but will be cancelled in some
            // cases. This directive is an exception to the rules of history.js
            $timeout( function() {
              $ionicHistory.nextViewOptions({
                historyRoot: false,
                disableAnimate: false
              });
            }, 300);
            $scope.closeModal();
          }
        });
      }
    };
  }])

  /**
  * Plugin extension point (see services/plugin-services.js)
  */
  .directive('csExtensionPoint', ['$state', '$compile', '$controller', '$templateCache', 'PluginService', function ($state, $compile, $controller, $templateCache, PluginService) {
    var getTemplate = function(extensionPoint) {
      var template = extensionPoint.templateUrl ? $templateCache.get(extensionPoint.templateUrl) : extensionPoint.template;
      if (!template) {
        console.error('[plugin] Could not found template for extension :' + (extensionPoint.templateUrl ? extensionPoint.templateUrl : extensionPoint.template));
        return '';
      }
      if (extensionPoint.controller) {
        template = '<ng-controller ng-controller="'+extensionPoint.controller+'">' + template + '</div>';
      }
      return template;
    };

    var compiler = function(tElement, tAttributes) {

      if (angular.isDefined(tAttributes.name)) {
        var extensionPoints = PluginService.extensions.points.getActivesByName(tAttributes.name);
        if (extensionPoints.length > 0) {
          tElement.html("");
          _.forEach(extensionPoints, function(extensionPoint){
            tElement.append(getTemplate(extensionPoint));
          });
        }
      }

      return {
        pre: function(scope, iElement, iAttrs){
          PluginService.extensions.points.current.set(iAttrs.name);
        },
        post: function(){
          PluginService.extensions.points.current.set();
        }
      };
    };


    return {
      restrict: "E",
      compile: compiler,
      scope: {
          content:'='
      }
    };
  }])

  .directive('onReadFile', ['$parse', function ($parse) {
    return {
      restrict: 'A',
      scope: false,
      link: function(scope, element, attrs) {
        var fn = $parse(attrs.onReadFile);

        element.on('change', function(onChangeEvent) {
          var reader = new FileReader();
          var fileData = {
            name: this.files[0].name,
            size: this.files[0].size,
            type: this.files[0].type
          };

          reader.onload = function(onLoadEvent) {
            scope.$applyAsync(function() {
              fn(scope, {
                file: {
                  fileContent: onLoadEvent.target.result,
                  fileData : fileData}
              });
            });
          };
          reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
        });
      }
    };
  }])

.directive("dropzone", ['$parse', function($parse) {
    return {
      restrict: 'A',
      scope: false,
        link: function(scope, elem, attrs) {
          var fn = $parse(attrs.dropzone);
          elem.bind('dragover', function (e) {
            e.stopPropagation();
            e.preventDefault();
          });
          elem.bind('dragenter', function(e) {
            e.stopPropagation();
            e.preventDefault();
          });
          elem.bind('dragleave', function(e) {
            e.stopPropagation();
            e.preventDefault();
          });
          elem.bind('drop', function(e) {
            e.stopPropagation();
            e.preventDefault();
            var fileData = {
              name: e.dataTransfer.files[0].name,
              size: e.dataTransfer.files[0].size,
              type: e.dataTransfer.files[0].type
            };

            var reader = new FileReader();
            reader.onload = function(onLoadEvent) {
              scope.$apply(function () {
                fn(scope, {
                  file: {
                    fileContent: onLoadEvent.target.result,
                    fileData : fileData}
                });
              });
            };
            reader.readAsText(e.dataTransfer.files[0]);
          });
      }
    };
  }])

  // Un-authenticate when window closed
  // see: https://stackoverflow.com/questions/28197316/javascript-or-angularjs-defer-browser-close-or-tab-close-between-refresh
  .directive('windowExitUnauth', ['$window', 'csWallet', function($window, csWallet) {
    return {
      restrict: 'AE',
      link: function(element, attrs){
        var myEvent = $window.attachEvent || $window.addEventListener,
          chkevent = $window.attachEvent ? 'onunload' : 'unload'; /// make IE7, IE8 compatable

        myEvent(chkevent, function (e) { // For >=IE7, Chrome, Firefox
          return csWallet.unauth();
        });
      }
    };
  }])
;

// Cesium filters
angular.module('cesium.filters', ['cesium.config', 'cesium.platform', 'pascalprecht.translate', 'cesium.translations'
])

  .factory('filterTranslations', ['$rootScope', 'csPlatform', 'csSettings', '$translate', function($rootScope, csPlatform, csSettings, $translate) {
    'ngInject';

    var
      started = false,
      startPromise,
      that = this;

    // Update some translations, when locale changed
    function onLocaleChange() {
      console.debug('[filter] Loading translations for locale [{0}]'.format($translate.use()));
      return $translate(['COMMON.DATE_PATTERN', 'COMMON.DATE_SHORT_PATTERN', 'COMMON.UD', 'COMMON.DAYS'])
        .then(function(translations) {
          that.DATE_PATTERN = translations['COMMON.DATE_PATTERN'];
          if (that.DATE_PATTERN === 'COMMON.DATE_PATTERN') {
            that.DATE_PATTERN = 'YYYY-MM-DD HH:mm';
          }
          that.DATE_SHORT_PATTERN = translations['COMMON.DATE_SHORT_PATTERN'];
          if (that.DATE_SHORT_PATTERN === 'COMMON.DATE_SHORT_PATTERN') {
            that.DATE_SHORT_PATTERN = 'YYYY-MM-DD';
          }
          that.DATE_MONTH_YEAR_PATTERN = translations['COMMON.DATE_MONTH_YEAR_PATTERN'];
          if (that.DATE_MONTH_YEAR_PATTERN === 'COMMON.DATE_MONTH_YEAR_PATTERN') {
            that.DATE_MONTH_YEAR_PATTERN = 'MMM YY';
          }
          that.DAYS = translations['COMMON.DAYS'];
          if (that.DAYS === 'COMMON.DAYS') {
            that.DAYS = 'days';
          }
          that.UD = translations['COMMON.UD'];
          if (that.UD === 'COMMON.UD') {
            that.UD = 'UD';
          }
        });
    }

    that.ready = function() {
      if (started) return $q.when(data);
      return startPromise || that.start();
    };

    that.start = function() {
      startPromise = csPlatform.ready()
        .then(onLocaleChange)
        .then(function() {
          started = true;

          csSettings.api.locale.on.changed($rootScope, onLocaleChange, this);
        });
      return startPromise;
    };

    // Default action
    that.start();

    return that;
  }])

  .filter('formatInteger', function() {
    return function(input) {
      return !input ? '0' : (input < 10000000 ? numeral(input).format('0,0') : numeral(input).format('0,0.000 a'));
    };
  })

  .filter('formatAmount', ['csConfig', 'csSettings', 'csCurrency', '$filter', function(csConfig, csSettings, csCurrency, $filter) {
    var pattern = '0,0.0' + Array(csConfig.decimalCount || 4).join('0');
    var patternBigNumber = '0,0.000 a';
    var currencySymbol = $filter('currencySymbol');

    // Always add one decimal for relative unit
    var patternRelative = pattern + '0';
    var minValueRelative = 1 / Math.pow(10, (csConfig.decimalCount || 4) + 1 /*add one decimal in relative*/);

    function formatRelative(input, options) {
      var currentUD = options && options.currentUD ? options.currentUD : csCurrency.data.currentUD;
      if (!currentUD) {
        console.warn("formatAmount: currentUD not defined");
        return;
      }
      var amount = input / currentUD;
      if (Math.abs(input) < minValueRelative && input !== 0) {
        amount = '~ 0';
      }
      else {
        amount = numeral(amount).format(patternRelative);
      }
      if (options && options.currency) {
        return amount + ' ' + currencySymbol(options.currency, true);
      }
      return amount;
    }

    function formatQuantitative(input, options) {
      var amount = numeral(input/100).format((input < -1000000000 || input > 1000000000) ? patternBigNumber : pattern);
      if (options && options.currency) {
        return amount + ' ' + currencySymbol(options.currency, false);
      }
      return amount;
    }

    return function(input, options) {
      if (input === undefined) return;
      return (options && angular.isDefined(options.useRelative) ? options.useRelative : csSettings.data.useRelative) ?
        formatRelative(input, options) :
        formatQuantitative(input, options);
    };
  }])

  .filter('formatAmountNoHtml', ['csConfig', 'csSettings', 'csCurrency', '$filter', function(csConfig, csSettings, csCurrency, $filter) {
    var minValue = 1 / Math.pow(10, csConfig.decimalCount || 4);
    var format = '0,0.0' + Array(csConfig.decimalCount || 4).join('0');
    var currencySymbol = $filter('currencySymbolNoHtml');

    function formatRelative(input, options) {
      var currentUD = options && options.currentUD ? options.currentUD : csCurrency.data.currentUD;
      if (!currentUD) {
        console.warn("formatAmount: currentUD not defined");
        return;
      }
      var amount = input / currentUD;
      if (Math.abs(amount) < minValue && input !== 0) {
        amount = '~ 0';
      }
      else {
        amount = numeral(amount).format(format);
      }
      if (options && options.currency) {
        return amount + ' ' + currencySymbol(options.currency, true);
      }
      return amount;
    }

    function formatQuantitative(input, options) {
      var amount = numeral(input/100).format((input > -1000000000 && input < 1000000000) ? '0,0.00' : '0,0.000 a');
      if (options && options.currency) {
        return amount + ' ' + currencySymbol(options.currency, false);
      }
      return amount;
    }

    return function(input, options) {
      if (input === undefined) return;
      return (options && angular.isDefined(options.useRelative) ? options.useRelative : csSettings.data.useRelative) ?
        formatRelative(input, options) :
        formatQuantitative(input, options);
    };
  }])


  .filter('currencySymbol', ['filterTranslations', '$filter', 'csSettings', function(filterTranslations, $filter, csSettings) {
    return function(input, useRelative) {
      if (!input) return '';
      return (angular.isDefined(useRelative) ? useRelative : csSettings.data.useRelative) ?
        (filterTranslations.UD + '<sub>' + $filter('abbreviate')(input) + '</sub>') :
        $filter('abbreviate')(input);
    };
  }])

  .filter('currencySymbolNoHtml', ['filterTranslations', '$filter', 'csSettings', function(filterTranslations, $filter, csSettings) {
    return function(input, useRelative) {
      if (!input) return '';
      return (angular.isDefined(useRelative) ? useRelative : csSettings.data.useRelative) ?
        (filterTranslations.UD + ' ' + $filter('abbreviate')(input)) :
        $filter('abbreviate')(input);
    };
  }])


  .filter('formatDecimal', ['csConfig', 'csCurrency', function(csConfig, csCurrency) {
    var minValue = 1 / Math.pow(10, csConfig.decimalCount || 4);
    var format = '0,0.0' + Array(csConfig.decimalCount || 4).join('0');

    return function(input) {
      if (input === undefined) return '0';
      if (input === Infinity || input === -Infinity) {
        console.warn("formatDecimal: division by zero ? (is currentUD defined ?) = "  + csCurrency.data.currentUD);
        return 'error';
      }
      if (Math.abs(input) < minValue) return '~ 0';
      return numeral(input/*-0.00005*/).format(format);
    };
  }])

  .filter('formatNumeral', function() {
    return function(input, pattern) {
      if (input === undefined) return '0';
      // for DEBUG only
      //if (isNaN(input)) {
      //    return 'NaN';
      //}
      if (Math.abs(input) < 0.0001) return '~ 0';
      return numeral(input).format(pattern);
    };
  })

  .filter('formatDate', ['filterTranslations', function(filterTranslations) {
    return function(input) {
      return input ? moment.unix(parseInt(input)).local().format(filterTranslations.DATE_PATTERN || 'YYYY-MM-DD HH:mm') : '';
    };
  }])

  .filter('formatDateShort', ['filterTranslations', function(filterTranslations) {
    return function(input) {
      return input ? moment.unix(parseInt(input)).local().format(filterTranslations.DATE_SHORT_PATTERN || 'YYYY-MM-DD') : '';
    };
  }])

  .filter('formatDateMonth', ['filterTranslations', function(filterTranslations) {
    return function(input) {
      return input ? moment.unix(parseInt(input)).local().format(filterTranslations.DATE_MONTH_YEAR_PATTERN || 'MMM YY') : '';
    };
  }])

  .filter('formatDateForFile', ['filterTranslations', function(filterTranslations) {
    return function(input) {
      return input ? moment.unix(parseInt(input)).local().format(filterTranslations.DATE_FILE_PATTERN || 'YYYY-MM-DD') : '';
    };
  }])

  .filter('formatTime', function() {
    return function(input) {
      return input ? moment.unix(parseInt(input)).local().format('HH:mm') : '';
    };
  })

  .filter('formatFromNow', function() {
    return function(input) {
      return input ? moment.unix(parseInt(input)).fromNow() : '';
    };
  })


  .filter('formatDurationTo', function() {
    return function(input) {
      return input ? moment.unix(moment().utc().unix() + parseInt(input)).fromNow() : '';
    };
  })

  .filter('formatDuration', function() {
    return function(input) {
      return input ? moment(0).from(moment.unix(parseInt(input)), true) : '';
    };
  })


  .filter('formatDurationTime', ['filterTranslations', function(filterTranslations) {
    return function(input) {
      if (!input) return '';
      var sign = input && input < 0 ? '-' : '+';
      input = Math.abs(input);
      var day = Math.trunc(input/3600/24);
      var hour = Math.trunc(input/3600 - day*24);
      var min = Math.trunc(input/60 - day*24*60 - hour*60);
      return day > 0 ? (sign + day + ' ' + filterTranslations.DAYS + ' ' + hour + 'h ' + min + 'm') :
        (hour > 0 ? (sign + hour + 'h ' + min + 'm') : (sign + min + 'm')) ;
    };
  }])

  // Display time in ms or seconds (see i18n label 'COMMON.EXECUTION_TIME')
  .filter('formatDurationMs', function() {
    return function(input) {
      return input ? (
        (input < 1000) ?
          (input + 'ms') :
          (input/1000 + 's')
      ) : '';
    };
  })

  .filter('formatPeriod', function() {
    return function(input) {
      if (!input) {return null;}
      var duration = moment(0).from(moment.unix(parseInt(input)), true);
      return duration.split(' ').slice(-1)[0]; // keep only last words (e.g. remove "un" "a"...)
    };
  })

  .filter('formatFromNowShort', function() {
    return function(input) {
      return input ? moment.unix(parseInt(input)).fromNow(true) : '';
    };
  })

  .filter('capitalize', function() {
    return function(input) {
      if (!input) return '';
      input = input.toLowerCase();
      return input.substring(0,1).toUpperCase()+input.substring(1);
    };
  })

  .filter('abbreviate', function() {
    var _cache = {};
    return function(input) {
      var currency = input || '';
      if (_cache[currency]) return _cache[currency];
      if (currency.length > 3) {
        var unit = '', sepChars = ['-', '_', ' '];
        for (var i = 0; i < currency.length; i++) {
          var c = currency[i];
          if (i === 0) {
            unit = (c === 'g' || c === 'G') ? 'Ğ' : c ;
          }
          else if (i > 0 && sepChars.indexOf(currency[i-1]) != -1) {
            unit += c;
          }
        }
        currency = unit.toUpperCase();
      }
      else {
        currency = currency.toUpperCase();
        if (currency.charAt(0) === 'G') {
          currency = 'Ğ' + (currency.length > 1 ? currency.substr(1) : '');
        }
      }

      _cache[input] = currency;
      return currency;
    };
  })

  .filter('upper', function() {
    return function(input) {
      if (!input) return '';
      return input.toUpperCase();
    };
  })

  .filter('formatPubkey', function() {
    return function(input) {
      return input ? input.substr(0,8) : '';
    };
  })

  .filter('formatHash', function() {
    return function(input) {
      return input ? input.substr(0,4) + input.substr(input.length-4) : '';
    };
  })

  .filter('formatCategory', function() {
    return function(input) {
      return input && input.length > 28 ? input.substr(0,25)+'...' : input;
    };
  })

  // Convert to user friendly URL (e.g. "Like - This" -> "like-this")
  .filter('formatSlug', function() {
    return function(input) {
      return input ? encodeURIComponent(input
        .toLowerCase()
        .replace(/<[^>]+>/g,'') // Remove tag (like HTML tag)
        .replace(/[^\w ]+/g,'')
        .replace(/ +/g,'-'))
        : '';
    };
  })

  // Convert a URI into parameter (e.g. "http://hos/path" -> "http%3A%2F%2Fhost%2Fpath")
  .filter('formatEncodeURI', function() {
    return function(input) {
      return input ? encodeURIComponent(input): '';
    };
  })

  .filter('truncText', function() {
    return function(input, size) {
      size = size || 500;
      return !input || input.length <= size ? input : (input.substr(0, size) + '...');
    };
  })

  .filter('truncUrl', function() {
    return function(input, size) {
      size = size || 25;
      var startIndex = input.startsWith('http://') ? 7 : (input.startsWith('https://') ? 8 : 0);
      return !input || (input.length-startIndex) <= size ? input.substr(startIndex) : (input.substr(startIndex, size) + '...');
    };
  })

  .filter('trustAsHtml', ['$sce', function($sce) {
    return function(html) {
      return $sce.trustAsHtml(html);
    };
  }])
;


angular.module('cesium.platform', ['ngIdle', 'cesium.config', 'cesium.services'])

  // Translation i18n
  .config(['$translateProvider', 'csConfig', function ($translateProvider, csConfig) {
    'ngInject';

    $translateProvider
      .uniformLanguageTag('bcp47')
      .determinePreferredLanguage()
      // Cela fait bugger les placeholder (pb d'affichage des accents en FR)
      //.useSanitizeValueStrategy('sanitize')
      .useSanitizeValueStrategy(null)
      .fallbackLanguage([csConfig.fallbackLanguage ? csConfig.fallbackLanguage : 'en'])
      .useLoaderCache(true);
  }])

  .config(['$httpProvider', 'csConfig', function($httpProvider, csConfig) {
    'ngInject';

    // Set default timeout
    $httpProvider.defaults.timeout = !!csConfig.timeout ? csConfig.timeout : 300000 /* default timeout */;

    //Enable cross domain calls
    $httpProvider.defaults.useXDomain = true;

    //Remove the header used to identify ajax call  that would prevent CORS from working
    delete $httpProvider.defaults.headers.common['X-Requested-With'];

    }])


  .config(['$compileProvider', 'csConfig', function($compileProvider, csConfig) {
    'ngInject';

    $compileProvider.debugInfoEnabled(!!csConfig.debug);
  }])

  .config(['$animateProvider', function($animateProvider) {
    'ngInject';

    $animateProvider.classNameFilter( /\banimate-/ );
  }])

  // Configure cache (used by HTTP requests) default max age
  .config(['CacheFactoryProvider', 'csConfig', function (CacheFactoryProvider, csConfig) {
    'ngInject';
    angular.extend(CacheFactoryProvider.defaults, { maxAge: csConfig.cacheTimeMs || 60 * 1000 /*1min*/});
  }])

  // Configure screen size detection
  .config(['screenmatchConfigProvider', function(screenmatchConfigProvider) {
    'ngInject';

    screenmatchConfigProvider.config.rules = 'bootstrap';
  }])

  .config(['$ionicConfigProvider', function($ionicConfigProvider) {
    'ngInject';

    // JS scrolling need for iOs (see http://blog.ionic.io/native-scrolling-in-ionic-a-tale-in-rhyme/)
    var enableJsScrolling = ionic.Platform.isIOS();
    $ionicConfigProvider.scrolling.jsScrolling(enableJsScrolling);

    // Configure the view cache
    $ionicConfigProvider.views.maxCache(5);
  }])

  .config(['IdleProvider', 'csConfig', function(IdleProvider, csConfig) {
    'ngInject';

    IdleProvider.idle(csConfig.logoutIdle||10*60/*10min*/);
    IdleProvider.timeout(csConfig.logoutTimeout||15); // display warning during 15s
  }])

  .factory('$exceptionHandler', function() {
    'ngInject';

    return function(exception, cause) {
      if (cause) console.error(exception, cause);
      else console.error(exception);
    };
  })


  .factory('csPlatform', ['ionicReady', '$rootScope', '$q', '$state', '$translate', '$timeout', 'UIUtils', 'BMA', 'Device', 'csHttp', 'csConfig', 'csSettings', 'csCurrency', 'csWallet', function (ionicReady, $rootScope, $q, $state, $translate, $timeout, UIUtils,
                                   BMA, Device, csHttp, csConfig, csSettings, csCurrency, csWallet) {

    'ngInject';
    var
      fallbackNodeIndex = 0,
      defaultSettingsNode,
      started = false,
      startPromise,
      listeners,
      removeChangeStateListener;

    function disableChangeState() {
      if (removeChangeStateListener) return; // make sure to call this once

      var remove = $rootScope.$on('$stateChangeStart', function (event, next, nextParams, fromState) {
        if (!event.defaultPrevented && next.name !== 'app.home' && next.name !== 'app.settings') {
          event.preventDefault();
          if (startPromise) {
            startPromise.then(function() {
              $state.go(next.name, nextParams);
            });
          }
          else {
            UIUtils.loading.hide();
          }
        }
      });

      // store remove listener function
      removeChangeStateListener = remove;
    }

    function enableChangeState() {
      if (removeChangeStateListener) removeChangeStateListener();
      removeChangeStateListener = null;
    }

    // Alert user if node not reached - fix issue #
    function checkBmaNodeAlive(alive) {
      if (alive) return true;

      // Remember the default node
      defaultSettingsNode = defaultSettingsNode || csSettings.data.node;

      var fallbackNode = csSettings.data.fallbackNodes && fallbackNodeIndex < csSettings.data.fallbackNodes.length && csSettings.data.fallbackNodes[fallbackNodeIndex++];
      if (!fallbackNode) {
        throw 'ERROR.CHECK_NETWORK_CONNECTION';
      }
      var newServer = fallbackNode.host + ((!fallbackNode.port && fallbackNode.port != 80 && fallbackNode.port != 443) ? (':' + fallbackNode.port) : '');
      return $translate('CONFIRM.USE_FALLBACK_NODE', {old: BMA.server, new: newServer})
        .then(function(msg) {
          return UIUtils.alert.confirm(msg);
        })
        .then(function (confirm) {
          if (!confirm) return;

          // FIXME: should not change settings, but only tha BMA content
          // in UI, display data form BMA object
          csSettings.data.node = fallbackNode;

          csSettings.data.node.temporary = true;
          csHttp.cache.clear();

          // loop
          return BMA.copy(fallbackNode)
            .then(checkBmaNodeAlive);
        });
    }

    function isStarted() {
      return started;
    }


    function getLatestRelease() {
      var latestRelease = csSettings.data.latestReleaseUrl && csHttp.uri.parse(csSettings.data.latestReleaseUrl);
      if (latestRelease) {
        return csHttp.get(latestRelease.host, latestRelease.protocol == 'https:' ? 443 : latestRelease.port, "/" + latestRelease.pathname)()
          .then(function (json) {
            if (json && json.name && json.tag_name && json.html_url) {
              return {
                version: json.name,
                url: json.html_url,
                isNewer: (csHttp.version.compare(csConfig.version, json.name) < 0)
              };
            }
          })
          .catch(function(err) {
            // silent (just log it)
            console.error('[platform] Failed to get Cesium latest version', err);
          })
          ;
      }
      return $q.when();
    }

    function addListeners() {
      listeners = [
        // Listen if node changed
        BMA.api.node.on.restart($rootScope, restart, this)
      ];
    }

    function removeListeners() {
      _.forEach(listeners, function(remove){
        remove();
      });
      listeners = [];
    }

    function ready() {
      if (started) return $q.when();
      return startPromise || start();
    }

    function restart() {
      console.debug('[platform] restarting csPlatform');
      return stop()
        .then(function () {
          return $timeout(start, 200);
        });
    }

    function start() {

      // Avoid change state
      disableChangeState();

      // We use 'ionicReady()' instead of '$ionicPlatform.ready()', because this one is callable many times
      startPromise = ionicReady()

        .then($q.all([
          // Load device
          Device.ready(),

          // Start settings
          csSettings.ready()
        ]))

        // Load BMA
        .then(function(){
          return BMA.ready().then(checkBmaNodeAlive);
        })

        // Load currency
        .then(csCurrency.ready)

        // Trying to restore wallet
        .then(csWallet.ready)

        .then(function(){
          enableChangeState();
          addListeners();
          startPromise = null;
          started = true;
        })
        .catch(function(err) {
          startPromise = null;
          started = false;
          if($state.current.name !== 'app.home') {
            $state.go('app.home', {error: 'peer'});
          }
          throw err;
        });

      return startPromise;
    }

    function stop() {
      if (!started) return $q.when();
      removeListeners();

      csWallet.stop();
      csCurrency.stop();
      BMA.stop();

      return $timeout(function() {
        enableChangeState();
        started = false;
        startPromise = null;
      }, 500);
    }

    return  {
      disableChangeState: disableChangeState,
      isStarted: isStarted,
      ready: ready,
      restart: restart,
      start: start,
      stop: stop,
      version: {
        latest: getLatestRelease
      }
    };
  }])

  .run(['$rootScope', '$translate', '$state', '$window', '$urlRouter', 'ionicReady', 'Device', 'UIUtils', '$ionicConfig', 'PluginService', 'csPlatform', 'csWallet', 'csSettings', 'csConfig', 'csCurrency', function($rootScope, $translate, $state, $window, $urlRouter, ionicReady,
                Device, UIUtils, $ionicConfig, PluginService, csPlatform, csWallet, csSettings, csConfig, csCurrency) {
    'ngInject';

    // Allow access to service data, from HTML templates
    $rootScope.config = csConfig;
    $rootScope.settings = csSettings.data;
    $rootScope.currency = csCurrency.data;
    $rootScope.device = Device;

    // Compute the root path
    var hashIndex = $window.location.href.indexOf('#');
    $rootScope.rootPath = (hashIndex != -1) ? $window.location.href.substr(0, hashIndex) : $window.location.href;
    console.debug('[app] Root path is [' + $rootScope.rootPath + ']');

    // removeIf(device)
    // -- Automatic redirection to HTTPS
    if ((csConfig.httpsMode === true || csConfig.httpsMode == 'true' ||csConfig.httpsMode === 'force') &&
      $window.location.protocol != 'https:') {
      $rootScope.$on('$stateChangeStart', function (event, next, nextParams, fromState) {
        var path = 'https' + $rootScope.rootPath.substr(4) + $state.href(next, nextParams);
        if (csConfig.httpsModeDebug) {
          console.debug('[app] [httpsMode] --- Should redirect to: ' + path);
          // continue
        }
        else {
          $window.location.href = path;
        }
      });
    }
    // endRemoveIf(device)

    // We use 'ionicReady()' instead of '$ionicPlatform.ready()', because this one is callable many times
    ionicReady().then(function() {

      // Keyboard
      if (Device.keyboard.enable) {
        // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
        // for form inputs)
        Device.keyboard.hideKeyboardAccessoryBar(true);

        // iOS: do not push header up when opening keyboard
        // (see http://ionicframework.com/docs/api/page/keyboard/)
        if (ionic.Platform.isIOS()) {
          Device.keyboard.disableScroll(true);
        }
      }

      // Ionic Platform Grade is not A, disabling views transitions
      if (ionic.Platform.grade.toLowerCase() != 'a') {
        console.info('[app] Disabling UI effects, because plateform\'s grade is [' + ionic.Platform.grade + ']');
        UIUtils.setEffects(false);
      }

      // Status bar style
      if (window.StatusBar) {
        // org.apache.cordova.statusbar required
        StatusBar.styleDefault();
      }

      // Get latest release
      csPlatform.version.latest()
        .then(function(release) {
          if (release && release.isNewer) {
            console.info('[app] New release detected [{0}]'.format(release.version));
            $rootScope.newRelease = release;
          }
          else {
            console.info('[app] Current version [{0}] is the latest release'.format(csConfig.version));
          }
        });

      // Make sure platform is started
      return csPlatform.ready();
    });
  }])
;

// Workaround to add "".startsWith() if not present
if (typeof String.prototype.startsWith !== 'function') {
  console.debug("Adding String.prototype.startsWith() -> was missing on this platform");
  String.prototype.startsWith = function(prefix) {
    return this.indexOf(prefix) === 0;
  };
}

// Workaround to add "".startsWith() if not present
if (typeof String.prototype.trim !== 'function') {
  console.debug("Adding String.prototype.trim() -> was missing on this platform");
  // Make sure we trim BOM and NBSP
  var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
  String.prototype.trim = function() {
    return this.replace(rtrim, '');
  };
}

// Workaround to add Math.trunc() if not present - fix #144
if (Math && typeof Math.trunc !== 'function') {
  console.debug("Adding Math.trunc() -> was missing on this platform");
  Math.trunc = function(number) {
    return (number - 0.5).toFixed();
  };
}

// Workaround to add "".format() if not present
if (typeof String.prototype.format !== 'function') {
  console.debug("Adding String.prototype.format() -> was missing on this platform");
  String.prototype.format = function() {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function(match, number) {
      return typeof args[number] != 'undefined' ? args[number] : match;
    });
  };
}
