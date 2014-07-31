package fi.vm.sade.rajapinnat.vtj.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.service.VtjService;
import fi.vm.sade.rajapinnat.vtj.service.VtjTestData;

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
    
    @Autowired
    private VtjTestData vtjTestData;
    
    @Value("${vtj.production.env}")
    private boolean productionEnv;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{hetu}")
    public Response teeHenkiloKysely(@PathParam("hetu") String hetu) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            YksiloityHenkilo yksiloityHenkilo = null;
            if (productionEnv) {
                yksiloityHenkilo = vtjService.teeHenkiloKysely(authentication.getName(), hetu);
            }
            else {
                yksiloityHenkilo = vtjTestData.teeHakuTestidatasta(hetu);
            }
            
            return Response.ok(yksiloityHenkilo).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
