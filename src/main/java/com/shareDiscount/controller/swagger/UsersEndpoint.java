package com.shareDiscount.controller.swagger;

import com.shareDiscount.controller.exception.ErrorInfo;
import com.shareDiscount.domains.UserParam;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;

/**
 * REST endpoint for user manipulation.
 */

@Path("/api/user")
@Api(value = "user", description = "Endpoint for user management")
@Produces({"application/json"})
public interface UsersEndpoint {

    @POST
    @ApiOperation(value = "Create user",
            notes = "Operation for create user",
            position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User was created successfully"),
            @ApiResponse(code = 409, message = "User with given username already exist", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<?> createUser(
            @ApiParam(value = "Created user object", required = true) UserParam user);

    @GET
    @Path("/{userId}")
    @ApiOperation(value = "Returns user details",
            notes = "Returns a complete list of users details",
            position = 2,
            response = UserParam.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of user detail", response = UserParam.class),
            @ApiResponse(code = 404, message = "User with given id does not exist", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<UserParam> getUser(@PathParam("userId") long userId);

    @PUT
    @Path("/{userId}")
    @ApiOperation(value = "Update user details",
            notes = "Update all list of user parameters users details",
            position = 3)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of user detail", response = UserParam.class),
            @ApiResponse(code = 404, message = "User with given id does not exist", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<UserParam> updateUser(@PathParam("userId") long userId,
                                         @ApiParam(value = "Created user object", required = true) UserParam user);

    @GET
    @ApiOperation(value = "Ger all Users",
            notes = "Return all existing in app users",
            position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval list of Users",
                    response = UserParam.class,
                    responseContainer = "List"),
            @ApiResponse(code = 204, message = "There no users yet", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<?> listAllUsers();

    @DELETE
    @Path("/{userId}")
    @ApiOperation(value = "Delete User",
            notes = "Fully Delete one user",
            position = 5)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted necessary user",
                    response = UserParam.class,
                    responseContainer = "List"),
            @ApiResponse(code = 404, message = "User with given id does not exist", response = ErrorInfo.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    ResponseEntity<?> deleteUser(@PathParam("userId") long userId);

}