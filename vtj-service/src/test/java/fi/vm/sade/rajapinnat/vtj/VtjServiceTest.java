package fi.vm.sade.rajapinnat.vtj;

import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.service.impl.VtjServiceImpl;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tempuri.SoSoSoap;
import org.tempuri.TeeHenkilonTunnusKyselyResponse;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/test-context.xml"})
public class VtjServiceTest {

    @Mock
    private SoSoSoap soSoSoap;

    @InjectMocks
    @Autowired
    private VtjServiceImpl vtjService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = fi.vm.sade.rajapinnat.vtj.NotFoundException.class)
    public void testHenkilonTunnusKysely0001() {

        TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult result =
                Mockito.mock(TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult.class);
        when(result.getContent())
                .thenReturn(new ArrayList<Object>(Arrays.asList(createVastausSanomaWithPaluukoodi0001())));

        when(soSoSoap.teeHenkilonTunnusKysely("OPHREK", null, null, "", null, "111111-1111", null, null, null, null, null, null, null))
                .thenReturn(result);

        vtjService.teeHenkiloKysely("", "111111-1111", false);
    }

    @Test
    public void testHenkilonTunnusKysely0000() {

        String hetu = "111111-1111";

        TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult result =
                Mockito.mock(TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult.class);
        when(result.getContent())
                .thenReturn(new ArrayList<Object>(Arrays.asList(createVastausSanomaWithPaluukoodi0000(hetu))));

        when(soSoSoap.teeHenkilonTunnusKysely("OPHREK", null, null, "", null, hetu, null, null, null, null, null, null, null))
                .thenReturn(result);

        YksiloityHenkilo yksiloityHenkilo = vtjService.teeHenkiloKysely("", hetu, false);

        assertEquals(yksiloityHenkilo.getHetu(), "111111-1111");
    }

    private VTJHenkiloVastaussanoma createVastausSanomaWithPaluukoodi0001() {
        VTJHenkiloVastaussanoma.Paluukoodi paluukoodi = new VTJHenkiloVastaussanoma.Paluukoodi();
        paluukoodi.setKoodi("0001");

        VTJHenkiloVastaussanoma vtjHenkiloVastaussanoma = new VTJHenkiloVastaussanoma();
        vtjHenkiloVastaussanoma.setPaluukoodi(paluukoodi);

        return vtjHenkiloVastaussanoma;
    }

    private VTJHenkiloVastaussanoma createVastausSanomaWithPaluukoodi0000(String hetu) {
        VTJHenkiloVastaussanoma.Paluukoodi paluukoodi = new VTJHenkiloVastaussanoma.Paluukoodi();
        paluukoodi.setKoodi("0000");


        VTJHenkiloVastaussanoma.Henkilo.Henkilotunnus henkiloTunnus = new VTJHenkiloVastaussanoma.Henkilo.Henkilotunnus();
        henkiloTunnus.setValue(hetu);

        VTJHenkiloVastaussanoma.Henkilo.NykyisetEtunimet nykyisetEtunimet = new VTJHenkiloVastaussanoma.Henkilo.NykyisetEtunimet();
        nykyisetEtunimet.setEtunimet("");

        VTJHenkiloVastaussanoma.Henkilo.NykyinenSukunimi nykyinenSukunimi= new VTJHenkiloVastaussanoma.Henkilo.NykyinenSukunimi();
        nykyinenSukunimi.setSukunimi("");

        VTJHenkiloVastaussanoma.Henkilo.NykyinenKutsumanimi nykyinenKutsumanimi = new VTJHenkiloVastaussanoma.Henkilo.NykyinenKutsumanimi();
        nykyinenKutsumanimi.setKutsumanimi("");

        VTJHenkiloVastaussanoma.Henkilo.Turvakielto turvakielto = new VTJHenkiloVastaussanoma.Henkilo.Turvakielto();
        turvakielto.setTurvakieltoTieto("1");

        VTJHenkiloVastaussanoma.Henkilo.Sukupuoli sukupuoli = new VTJHenkiloVastaussanoma.Henkilo.Sukupuoli();
        sukupuoli.setSukupuolikoodi("");

        VTJHenkiloVastaussanoma.Henkilo.Aidinkieli aidinkieli = new VTJHenkiloVastaussanoma.Henkilo.Aidinkieli();
        aidinkieli.setKielikoodi("");

        VTJHenkiloVastaussanoma.Henkilo henkilo = new VTJHenkiloVastaussanoma.Henkilo();
        henkilo.setHenkilotunnus(henkiloTunnus);
        henkilo.setNykyisetEtunimet(nykyisetEtunimet);
        henkilo.setNykyinenSukunimi(nykyinenSukunimi);
        henkilo.setNykyinenKutsumanimi(nykyinenKutsumanimi);
        henkilo.setTurvakielto(turvakielto);
        henkilo.setSukupuoli(sukupuoli);
        henkilo.setAidinkieli(aidinkieli);

        VTJHenkiloVastaussanoma vtjHenkiloVastaussanoma = new VTJHenkiloVastaussanoma();
        vtjHenkiloVastaussanoma.setPaluukoodi(paluukoodi);
        vtjHenkiloVastaussanoma.setHenkilo(henkilo);

        return vtjHenkiloVastaussanoma;
    }
}

