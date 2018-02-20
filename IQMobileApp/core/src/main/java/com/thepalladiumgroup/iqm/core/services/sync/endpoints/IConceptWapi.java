package com.thepalladiumgroup.iqm.core.services.sync.endpoints;

import com.thepalladiumgroup.iqm.core.model.MConcept;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface IConceptWapi {
    @GET("Concept/{encounterTypeId}")
    Call<List<MConcept>> getConcepts(@Path("encounterTypeId") int encounterTypeId);
}
