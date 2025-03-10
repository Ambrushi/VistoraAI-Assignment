package com.example.dbcrawler.util;

import com.example.dbcrawler.model.TableMetadata;
import javassist.*;

import java.util.Map;

public class ModelGenerator {

    /**
     * Generates a Java class for the given table metadata.
     *
     * @param metadata The table metadata.
     * @return A message indicating the class was generated.
     * @throws Exception If an error occurs during class generation.
     */
    public static String generateClass(TableMetadata metadata) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(metadata.getTableName());

        // Add fields
        for (Map.Entry<String, TableMetadata.ColumnMetadata> entry : metadata.getColumns().entrySet()) {
            String fieldName = entry.getKey();
            String fieldType = mapSqlTypeToJavaType(entry.getValue().getColumnType());
            
            CtClass fieldClass = pool.get(fieldType);
            CtField field = new CtField(fieldClass, fieldName, cc);
            field.setModifiers(Modifier.PRIVATE);
            cc.addField(field);

            // Add getter and setter methods
            cc.addMethod(generateGetter(cc, fieldName, fieldType));
            cc.addMethod(generateSetter(cc, fieldName, fieldType));
        }

        // Write the class to a file (default directory)
        cc.writeFile();
        return "Model generated for table: " + metadata.getTableName();
    }

    /**
     * Maps SQL data types to Java data types.
     *
     * @param sqlType The SQL data type.
     * @return The corresponding Java data type.
     */
    private static String mapSqlTypeToJavaType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "INT":
            case "TINYINT":
            case "SMALLINT":
                return "int";
            case "BIGINT":
                return "long";
            case "VARCHAR":
            case "CHAR":
            case "TEXT":
                return "java.lang.String";
            case "DATE":
            case "DATETIME":
            case "TIMESTAMP":
                return "java.util.Date";
            case "FLOAT":
            case "DOUBLE":
                return "double";
            case "BOOLEAN":
            case "BIT":
                return "boolean";
            default:
                return "java.lang.Object";
        }
    }

    /**
     * Generates a getter method for a field.
     *
     * @param cc        The CtClass object.
     * @param fieldName The name of the field.
     * @param fieldType The type of the field.
     * @return The generated getter method.
     * @throws CannotCompileException If the method cannot be compiled.
     */
    private static CtMethod generateGetter(CtClass cc, String fieldName, String fieldType) throws CannotCompileException {
        String methodName = "get" + capitalize(fieldName);
        String methodBody = "public " + fieldType + " " + methodName + "() { return this." + fieldName + "; }";
        return CtMethod.make(methodBody, cc);
    }

    /**
     * Generates a setter method for a field.
     *
     * @param cc        The CtClass object.
     * @param fieldName The name of the field.
     * @param fieldType The type of the field.
     * @return The generated setter method.
     * @throws CannotCompileException If the method cannot be compiled.
     */
    private static CtMethod generateSetter(CtClass cc, String fieldName, String fieldType) throws CannotCompileException {
        String methodName = "set" + capitalize(fieldName);
        String methodBody = "public void " + methodName + "(" + fieldType + " " + fieldName + ") { this." + fieldName + " = " + fieldName + "; }";
        return CtMethod.make(methodBody, cc);
    }

    /**
     * Capitalizes the first letter of a string.
     *
     * @param fieldName The field name.
     * @return The capitalized field name.
     */
    private static String capitalize(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return fieldName;
        }
        return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }
}
