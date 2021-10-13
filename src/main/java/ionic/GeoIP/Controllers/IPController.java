package ionic.GeoIP.Controllers;

import ionic.GeoIP.Model.IPGEO;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class IPController {

    IPGEO ipgeo = new IPGEO();

    @PostMapping("/ipinput")
    public ResponseEntity<IPGEO> getIPAddress(@RequestBody String ip) throws IOException, InterruptedException {
//        ip = "106.223.183.144";
//        ip = "";
//        JSONObject ipData = new JSONObject(ip);
//        ip = (String)ipData.get(ip);
        ip = ip.replaceAll("\"","");
        ip = ip.replaceAll(":","");
        ip = ip.replace("ip","");
        ip = ip.replace("{","");
        ip = ip.replace("}","");

        String IP_URL = "http://ip-api.com/json/" + ip + "?fields=status,message,continent,country,proxy,mobile,countryCode,region,regionName,city,district,zip,lat,lon,timezone,isp,org,as,query";   // Get IP Address
        if(ip.equals("")) {
            IP_URL = "http://ip-api.com/json/?fields=status,message,continent,country,proxy,mobile,countryCode,region,regionName,city,district,zip,lat,lon,timezone,isp,org,as,query";
        }

        InetAddressValidator validator = InetAddressValidator.getInstance();

        if(validator.isValid(ip) || ip.equals("") || ip.equals(null)) {
            fetchData(IP_URL);
            if(ipgeo.getStatus().equalsIgnoreCase("fail")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ipgeo);
            }
            else {
                return ResponseEntity.of(Optional.of(ipgeo));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ipgeo);
    }

    @GetMapping

    public void fetchData(String IP_URL) throws IOException, InterruptedException {

        List<IPGEO> ipStats = new ArrayList<>();


        // Parsing the URL
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(IP_URL)).
                build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        //parsing the JSON data
        JSONObject apiData = new JSONObject(httpResponse.body());

        String query = (String) apiData.get("query");
        String status = (String) apiData.get("status");
        String country = (String) apiData.get("country");
        String countryCode = (String) apiData.get("countryCode");
        String regionName = (String) apiData.get("regionName");
        String city = (String)apiData.get("city");
        String pinCode = (String) apiData.get("zip");
        String isp = (String) apiData.get("isp");
        boolean mobile = (Boolean) apiData.get("mobile");


        this.ipgeo.setQuery(query);
        this.ipgeo.setStatus(status);
        this.ipgeo.setCountry(country);
        this.ipgeo.setCountryCode(countryCode);
        this.ipgeo.setRegionName(regionName);
        this.ipgeo.setCity(city);
        this.ipgeo.setPinCode(pinCode);
        this.ipgeo.setIsp(isp);
        this.ipgeo.setMobile(mobile);

        ipStats.add(ipgeo);
    }
}
