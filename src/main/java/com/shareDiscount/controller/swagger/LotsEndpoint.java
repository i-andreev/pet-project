package com.shareDiscount.controller.swagger;

import com.shareDiscount.controller.exception.ErrorInfo;
import com.shareDiscount.domains.LotParam;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;

@Path("/api/lot")
@Api(value = "Lost", description = "Endpoint for lots management")
@Produces({"application/json"})
public interface LotsEndpoint {
    @POST
    @ApiOperation(value = "Create Lot",
            notes = "Operation for create lot",
            position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Lot was created successfully"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<?> createLot(
            @ApiParam(value = "Created lot object", required = true) LotParam lot);

    @GET
    @Path("/{lotId}")
    @ApiOperation(value = "Returns lot details",
            notes = "Returns a complete list of lots details",
            position = 2,
            response = LotParam.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of lot detail", response = LotParam.class),
            @ApiResponse(code = 404, message = "Lot with given id does not exist", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<LotParam> getLot(@PathParam("lotId") long lotId);

    @PUT
    @Path("/{lotId}")
    @ApiOperation(value = "Update lot details",
            notes = "Update all list of lot parameters lots details",
            position = 3)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of lot detail", response = LotParam.class),
            @ApiResponse(code = 404, message = "Lot with given id does not exist", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<LotParam> updateLot(@PathParam("lotId") long lotId,
                                         @ApiParam(value = "Created lot object", required = true) LotParam lot);

    @GET
    @ApiOperation(value = "Ger all Lots",
            notes = "Return all existing in app lots",
            position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval list of Lots",
                    response = LotParam.class,
                    responseContainer = "List"),
            @ApiResponse(code = 204, message = "There no lots yet", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<?> listAllLots(Long userId);

    @DELETE
    @Path("/{lotId}")
    @ApiOperation(value = "Delete Lot",
            notes = "Fully Delete one lot",
            position = 5)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted necessary lot",
                    response = LotParam.class,
                    responseContainer = "List"),
            @ApiResponse(code = 404, message = "Lot with given id does not exist", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<?> deleteLot(@PathParam("lotId") long lotId);
}
