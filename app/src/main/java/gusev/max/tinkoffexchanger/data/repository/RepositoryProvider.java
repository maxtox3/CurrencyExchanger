package gusev.max.tinkoffexchanger.data.repository;

import android.support.annotation.NonNull;

public final class RepositoryProvider {

    private static Repository repository;

    private RepositoryProvider() {}

    @NonNull
    public static Repository provideRepository() {
        if (repository == null) {
            repository = new RepositoryImpl();
        }
        return repository;
    }

//    public static void setRepository(@NonNull Repository repo) {
//        repository = repo;
//    }

    public static void init() {
        repository = new RepositoryImpl();
    }
}
