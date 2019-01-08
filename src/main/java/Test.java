import com.test.sms.rest.RestClient;

public class Test {
    public static void main(String arg[]) {
        System.setProperty("javax.net.ssl.trustStore", "./keystore1.jks");
        RestClient client = new RestClient();
        String response = client.callGetAPI();
        System.out.println(response);

    }
}
