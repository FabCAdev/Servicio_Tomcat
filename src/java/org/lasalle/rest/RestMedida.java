package org.lasalle.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.lasalle.controller.ControllerMedida;
import org.lasalle.model.Medida;

@Path("medidas")
public class RestMedida {

    private final Gson gson = new Gson();
    private final ControllerMedida controller = new ControllerMedida();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMedidas() {
        try {
            List<Medida> lista = controller.getAll();
            return Response.ok(gson.toJson(lista)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return error(Response.Status.INTERNAL_SERVER_ERROR,
                    "No se pudo obtener la lista de medidas");
        }
    }

    @GET
    @Path("getAllMedidas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMedidasAlias() {
        return getAllMedidas();
    }

    @GET
    @Path("usuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedidasByUsuarioId(@PathParam("idUsuario") int idUsuario) {
        if (idUsuario <= 0) {
            return error(Response.Status.BAD_REQUEST, "El idUsuario debe ser mayor que cero");
        }

        try {
            List<Medida> lista = controller.getByUsuarioId(idUsuario);
            return Response.ok(gson.toJson(lista)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return error(Response.Status.INTERNAL_SERVER_ERROR,
                    "No se pudieron obtener las medidas del usuario");
        }
    }

    @GET
    @Path("getMedidasByUsuarioId/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedidasByUsuarioIdAlias(@PathParam("idUsuario") int idUsuario) {
        return getMedidasByUsuarioId(idUsuario);
    }

    private Response error(Response.Status status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("result", mensaje);
        return Response.status(status).entity(gson.toJson(body)).build();
    }
}
