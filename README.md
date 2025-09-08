# Polynomial Constant Term Calculator (Multiple JSON Files)

This project computes the constant term (`c`) of a polynomial given its roots in one or more JSON files. It supports very large numbers using `BigInteger` in Java.

## Folder Structure

```
hello/
 ├─ PolynomialConstant.java   # Java source code
 ├─ exam1.json               # JSON file with roots for example 1
 ├─ exam2.json               # JSON file with roots for example 2
 └─ json-simple-1.1.1.jar    # JSON parsing library (optional if using standard Java version)
```

## JSON File Format

Each JSON file should have this structure:

```json
{
  "keys": {
    "n": 4,
    "k": 3
  },
  "1": {"base": "10", "value": "4"},
  "2": {"base": "2", "value": "111"},
  "3": {"base": "10", "value": "12"},
  "6": {"base": "4", "value": "213"}
}
```

* `n`: Total roots provided.
* `k`: Number of roots to use to determine the polynomial (degree = k-1).
* Each root has a `base` and a `value`.

## Compilation (with JSON-Simple)

Navigate to the folder containing the files and run:

### PowerShell

```powershell
javac -cp ".;.\json-simple-1.1.1.jar" PolynomialConstant.java
```

### CMD

```cmd
javac -cp .;json-simple-1.1.1.jar PolynomialConstant.java
```

## Running the Program

### Single JSON file

```powershell
java -cp ".;.\json-simple-1.1.1.jar" PolynomialConstant exam1.json
```

### Multiple JSON files at once

```powershell
java -cp ".;.\json-simple-1.1.1.jar" PolynomialConstant exam1.json exam2.json
```

The program will print the constant term `c` for each JSON file consecutively.

## Output Example

```
Processing file: exam1.json
Constant term c: -336
Processing file: exam2.json
Constant term c: 1234567890123456789
```

## Notes

* Ensure all JSON files are in the same folder as the Java file or provide the absolute path.
* Python users can also run the Python version of this program for simplicity.
* Very large roots are handled using `BigInteger`.
* For Windows PowerShell, use quotes around classpath: `".;.\json-simple-1.1.1.jar"`.

## Troubleshooting

* **FileNotFoundException**: Check that the JSON file path is correct.
* **ClassNotFoundException**: Ensure `json-simple-1.1.1.jar` is included in the classpath.
* **Path issues in PowerShell**: Prefix local files with `.\` and use quotes around classpath.

## Alternative (No External Libraries)

If you want to avoid `json-simple.jar`, a version using only standard Java libraries can parse JSON manually, requiring no external JARs.
