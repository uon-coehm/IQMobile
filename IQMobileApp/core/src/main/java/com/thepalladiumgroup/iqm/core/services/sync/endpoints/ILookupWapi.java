package com.thepalladiumgroup.iqm.core.services.sync.endpoints;

import com.thepalladiumgroup.iqm.core.model.Lookup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface ILookupWapi {
    @GET("Lookup")
    Call<List<Lookup>> getLookups();
}
