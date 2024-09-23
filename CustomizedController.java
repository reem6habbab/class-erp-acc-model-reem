package com.phsoft.controllers;

import com.phsoft.api.App;
import com.phsoft.db.export.ExportEntity;
import com.phsoft.db.report.Report;
import com.phsoft.services.CustomizedService;
import com.phsoft.tools.PhU;
import com.phsoft.tools.security.JWebToken;
import com.phsoft.web.results.Result;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Haytham
 */
@Path("/CC")
public class CustomizedController {

  @POST
  @Path("/getCopies")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCopies() {
    String vRet = PhU.getJSON((new CustomizedService(App.getMainDBConnection())).getCopies());
    return Response.ok(vRet).build();
  }

  @POST
  @Path("/attached/new")
  @Produces(MediaType.APPLICATION_JSON)
  public Response uploadFile(@HeaderParam("Authorization") String authorization, @HeaderParam("periodId") Integer periodId, String vParams) {
    Long userId = Long.valueOf(JWebToken.getInstanceByAuthorization(authorization).getPayload().get("jui").toString());
    String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
    HashMap hParams = PhU.stringToMap(vParams);
    String vRet = PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).uploadFile(userId, hParams));
    return Response.ok(vRet).build();
  }

  @GET
  @Path("/attached/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response attachedGet(@HeaderParam("Authorization") String authorization, @PathParam("id") Long nId) {
    String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
    String vRet = PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).getFile(nId));
    return Response.ok(vRet).build();
  }

  @DELETE
  @Path("/attached/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response attachedDelete(@HeaderParam("Authorization") String authorization, @PathParam("id") Long nId) {
    Long userId = Long.valueOf(JWebToken.getInstanceByAuthorization(authorization).getPayload().get("jui").toString());
    String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
    String vRet = PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).deleteFile(nId, userId));
    return Response.ok(vRet).build();
  }

  @GET
  @Path("/Views/{pkgName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getViews(@HeaderParam("Authorization") String authorization, @PathParam("pkgName") String pkgName) {
    Map<String, Report> hPackage;
    if (App.hReports.containsKey(pkgName)) {
      hPackage = App.hReports.get(pkgName);
      String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
      Object vRet = PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).getViews(hPackage));
      return Response.ok(vRet).build();
    }
    return Response.ok(PhU.getJSON(Result.invalid(pkgName + "/"))).build();
  }

  @GET
  @Path("/Export/{pkgName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getExportPackage(@HeaderParam("Authorization") String authorization, @PathParam("pkgName") String pkgName) {
    Map<String, ExportEntity> hPackage;
    if (App.hExports.containsKey(pkgName)) {
      hPackage = App.hExports.get(pkgName);
      String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
      Object vRet = PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).getExportPackage(hPackage));
      return Response.ok(vRet).build();
    }
    return Response.ok(PhU.getJSON(Result.invalid(pkgName + "/"))).build();
  }

  @POST
  @Path("/Export/{pkgName}/{tableName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response export(@HeaderParam("Authorization") String authorization, @HeaderParam("periodId") Integer periodId, @PathParam("pkgName") String pkgName, @PathParam("tableName") String tableName, String vParams) {
    Long userId = Long.valueOf(JWebToken.getInstanceByAuthorization(authorization).getPayload().get("jui").toString());
    String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
    Map<String, ExportEntity> hPackage;
    if (App.hExports.containsKey(pkgName)) {
      hPackage = App.hExports.get(pkgName);
      if (hPackage.containsKey(tableName)) {
        Map<String, Object> hParams = PhU.stringToMap(vParams);
        return Response.ok(PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).export(userId, periodId, (ExportEntity) hPackage.get(tableName), hParams))).build();
      }
    }
    return Response.noContent().build();
  }

  @GET
  @Path("/Import/{pkgName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getImportTables(@HeaderParam("Authorization") String authorization, @PathParam("pkgName") String pkgName) {
    String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
    Object vRet = PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).getImportTables(pkgName));
    return Response.ok(vRet).build();
  }

  @GET
  @Path("/Import/{pkgName}/{tableName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getImportTableFields(@HeaderParam("Authorization") String authorization, @PathParam("pkgName") String pkgName, @PathParam("tableName") String tableName) {
    String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
    Object vRet = PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).getImportTableFields(pkgName, tableName));
    return Response.ok(vRet).build();
  }

  @POST
  @Path("/Import/{pkgName}/{tableName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response doImport(@HeaderParam("Authorization") String authorization, @PathParam("pkgName") String pkgName, @PathParam("tableName") String tableName, String rParam) {
    String vCopy = JWebToken.getInstanceByAuthorization(authorization).getPayload().get("Copy").toString();
    Object vRet = PhU.getJSON((new CustomizedService(App.getDBConnection(vCopy))).doImport(pkgName, tableName, rParam));
    return Response.ok(vRet).build();
  }
  //jhgcxdfgyhuijkol;lkhjfgvder45tuy7h
}
