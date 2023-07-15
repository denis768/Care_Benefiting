package com.example.myapplication.data;

import com.example.myapplication.data.users.UsersApi;

public class UsersApiService {
    private static UsersApi usersApi;

    private static UsersApi create() {
        return RetrofitService.getInstance().create(UsersApi.class);
    }

    public static UsersApi getInstance() {
        if (usersApi == null) {
            usersApi = create();
        }
        return usersApi;
    }
}
