package fi.vm.sade.rajapinnat.vtj.resources;

import fi.vm.sade.auditlog.Changes;
import fi.vm.sade.auditlog.Target;
import fi.vm.sade.rajapinnat.vtj.AuditLogger;
import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.PassivoituException;
import fi.vm.sade.rajapinnat.vtj.VtjOperation;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.service.VtjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fi.vm.sade.rajapinnat.vtj.AuditHelper.getUser;

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
    private AuditLogger auditLogger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{hetu}")
    public Response teeHenkiloKysely(@PathParam("hetu") String hetu,
                                     @Context HttpServletRequest request,
                                     @QueryParam("log") Boolean logMessage) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            YksiloityHenkilo yksiloityHenkilo = vtjService.teeHenkiloKysely(
                    authentication.getName(), hetu, (logMessage != null ? logMessage : false));

            Target target = new Target.Builder().setField("hetu", hetu).build();
            Changes changes = new Changes.Builder().build();
            auditLogger.log(getUser(request), VtjOperation.HENKILOTIETO_HAKU, target, changes);

            return Response.ok(yksiloityHenkilo).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (PassivoituException e) {
            YksiloityHenkilo yksiloityHenkilo = new YksiloityHenkilo();
            yksiloityHenkilo.setPassivoitu(true);
            return Response.ok(yksiloityHenkilo).build();
        }
    }
}
