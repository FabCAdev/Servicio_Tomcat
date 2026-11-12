package org.lasalle.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.lasalle.controller.ControllerMedida;
import org.lasalle.model.Medida;

@Path("medidas")
public class RestMedida {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            ControllerMedida cm = new ControllerMedida();
            List<Medida> medidas = cm.getAll();
            String json = new Gson().toJson(medidas);
            return Response.status(Response.Status.OK).entity(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            String error = "{\"error\":\"Ocurrió un error al consultar las medidas\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response saveMedida(@FormParam("idUsuario") int idUsuario,
                               @FormParam("glucosa") int glucosa,
                               @FormParam("fechaHora") String fechaHora,
                               @FormParam("contexto") String contexto,
                               @FormParam("notas") String notas) {
        try {
            Medida medida = new Medida();
            medida.setIdUsuario(idUsuario);
            medida.setGlucosa(glucosa);
            medida.setFechaHora(fechaHora);
            medida.setContexto(contexto);
            medida.setNotas(notas);

            ControllerMedida cm = new ControllerMedida();
            Medida m = cm.save(medida);
            
            String json = "{\"result\":\"Medida registrada exitosamente\", \"medida\":" + new Gson().toJson(m) + "}";
            return Response.status(Response.Status.CREATED).entity(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            String error = "{\"result\":\"No se pudo registrar la medida\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}