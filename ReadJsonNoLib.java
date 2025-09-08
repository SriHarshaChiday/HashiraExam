import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

public class ReadJsonNoLib {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter JSON filename: ");
        String fileName = sc.nextLine().trim();
        sc.close();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line.trim());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String json = sb.toString();

        // --- Robust extraction of n ---
        int nIndex = json.indexOf("\"n\"");
        if (nIndex == -1) throw new RuntimeException("n not found in JSON");
        int colonN = json.indexOf(":", nIndex) + 1;
        while (colonN < json.length() && !Character.isDigit(json.charAt(colonN))) colonN++;
        int nEnd = colonN;
        while (nEnd < json.length() && Character.isDigit(json.charAt(nEnd))) nEnd++;
        int n = Integer.parseInt(json.substring(colonN, nEnd));

        // --- Robust extraction of k ---
        int kIndex = json.indexOf("\"k\"");
        if (kIndex == -1) throw new RuntimeException("k not found in JSON");
        int colonK = json.indexOf(":", kIndex) + 1;
        while (colonK < json.length() && !Character.isDigit(json.charAt(colonK))) colonK++;
        int kEnd = colonK;
        while (kEnd < json.length() && Character.isDigit(json.charAt(kEnd))) kEnd++;
        int k = Integer.parseInt(json.substring(colonK, kEnd));

        System.out.println("n = " + n + ", k = " + k);

        // Arrays to store x and y values
        int[] xs = new int[k];
        BigDecimal[] ys = new BigDecimal[k];
        int idx = 0;

        // --- Parse points robustly ---
        for (int i = 1; i <= k; i++) {
            String key = "\"" + i + "\":";
            int keyIndex = json.indexOf(key);
            if (keyIndex == -1) continue;

            int start = json.indexOf("{", keyIndex);
            int end = json.indexOf("}", start);
            String obj = json.substring(start + 1, end);

            // Extract base robustly
            int baseIndex = obj.indexOf("\"base\"");
            int colonBase = obj.indexOf(":", baseIndex) + 1;
            while (colonBase < obj.length() && !Character.isDigit(obj.charAt(colonBase))) colonBase++;
            int baseEnd = colonBase;
            while (baseEnd < obj.length() && Character.isDigit(obj.charAt(baseEnd))) baseEnd++;
            int base = Integer.parseInt(obj.substring(colonBase, baseEnd));

            // Extract value robustly
            int valueIndex = obj.indexOf("\"value\"");
            int colonValue = obj.indexOf(":", valueIndex) + 1;
            while (colonValue < obj.length() && (obj.charAt(colonValue) == ' ' || obj.charAt(colonValue) == '\"')) colonValue++;
            int valueEnd = colonValue;
            while (valueEnd < obj.length() && obj.charAt(valueEnd) != '\"') valueEnd++;
            String valueStr = obj.substring(colonValue, valueEnd);

            BigDecimal y = new BigDecimal(new BigInteger(valueStr, base));
            xs[idx] = i;
            ys[idx] = y;
            idx++;
        }

        // --- Lagrange interpolation at x=0 using BigDecimal ---
        MathContext mc = new MathContext(50); // high precision
        BigDecimal c = BigDecimal.ZERO;

        for (int i = 0; i < k; i++) {
            BigDecimal term = ys[i];
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term = term.multiply(BigDecimal.valueOf(-xs[j]))
                               .divide(BigDecimal.valueOf(xs[i] - xs[j]), mc);
                }
            }
            c = c.add(term);
        }

        // Round to nearest integer
        c = c.setScale(0, RoundingMode.HALF_UP);
        System.out.println("Constant term c = " + c.toBigInteger());
    }
}
