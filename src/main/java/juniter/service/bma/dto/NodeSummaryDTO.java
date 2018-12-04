package juniter.service.bma.dto;

import java.io.Serializable;



public class NodeSummaryDTO implements Serializable {

    private Card duniter = new Card();

    private static final long serialVersionUID = -6400285527778830671L;


    public Card getDuniter() {
        return duniter;
    }

    public void setDuniter(Card duniter) {
        this.duniter = duniter;
    }



    class Card implements Serializable {

        private static final long serialVersionUID = -6400285666088830671L;


        private String software = "juniter";
        private String version = "0.1.0";
        private Integer forkWindowSize = 66;

        public String getSoftware() {
            return software;
        }

        public void setSoftware(String software) {
            this.software = software;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Integer getForkWindowSize() {
            return forkWindowSize;
        }

        public void setForkWindowSize(Integer forkWindowSize) {
            this.forkWindowSize = forkWindowSize;
        }

    }
}
