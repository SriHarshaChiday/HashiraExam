import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PolynomialConstant {

    // Convert a string in a given base to BigInteger
    public static BigInteger convertToBigInt(String value, int base) {
        return new BigInteger(value, base);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java PolynomialConstant <json_file>");
            return;
        }

        String jsonFile = args[0];
        try {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(jsonFile));

            JSONObject keys = (JSONObject) data.get("keys");
            int k = ((Long) keys.get("k")).intValue();

            // Extract root keys (ignore "keys") and sort numerically
            List<String> rootKeys = new ArrayList<>();
            for (Object keyObj : data.keySet()) {
                String key = (String) keyObj;
                if (!key.equals("keys")) {
                    rootKeys.add(key);
                }
            }
            Collections.sort(rootKeys);

            // Convert first k roots to BigInteger
            List<BigInteger> roots = new ArrayList<>();
            for (String key : rootKeys) {
                JSONObject rootObj = (JSONObject) data.get(key);
                String value = (String) rootObj.get("value");
                int base = Integer.parseInt((String) rootObj.get("base"));
                roots.add(convertToBigInt(value, base));
                if (roots.size() == k) break;
            }

            // Compute constant term c = (-1)^k * product of roots
            BigInteger c = BigInteger.ONE;
            for (BigInteger r : roots) {
                c = c.multiply(r);
            }
            if (k % 2 != 0) {
                c = c.negate();
            }

            System.out.println("Constant term c: " + c.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
