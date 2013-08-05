package fi.vm.sade.rajapinnat.vtj.resources;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.service.VtjService;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * User: tommiha
 * Date: 8/5/13
 * Time: 1:00 PM
 */
@Component
@Path("vtj")
@PreAuthorize("isAuthenticated()")
public class VtjResource {

    @Autowired
    private VtjService vtjService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{hetu}")
    public Response teeHenkiloKysely(@PathParam("hetu") String hetu) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            VTJHenkiloVastaussanoma vastaussanoma = vtjService.teeHenkiloKysely(authentication.getName(), hetu);
            return Response.ok(vastaussanoma).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
