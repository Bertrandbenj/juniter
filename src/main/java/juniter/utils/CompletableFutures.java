package juniter.utils;

/*
 * #%L
 * Duniter4j :: Core Shared
 * %%
 * Copyright (C) 2014 - 2017 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Helper class on CompletableFuture and concurrent classes
 * Created by blavenie on 24/03/17.
 */
public class CompletableFutures {

    private CompletableFutures() {
    }

    public static <T> CompletableFuture<List<T>> allOfToList(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                futures.stream()
                        .map(future -> future.join())
                        .filter(peer -> peer != null) // skip
                        .collect(Collectors.toList())
        );
    }

    public static <T> CompletableFuture<List<T>> allOfToList(List<CompletableFuture<T>> futures, Predicate<? super T> filter) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                futures.stream()
                        .map(future -> future.join())
                        .filter(filter)
                        .collect(Collectors.toList())
        );
    }
}
