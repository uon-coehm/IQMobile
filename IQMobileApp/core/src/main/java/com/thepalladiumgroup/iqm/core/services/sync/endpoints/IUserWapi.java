package com.thepalladiumgroup.iqm.core.services.sync.endpoints;

import com.thepalladiumgroup.iqm.core.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface IUserWapi {
    @GET("User")
    Call<List<User>> getUsers();
}
