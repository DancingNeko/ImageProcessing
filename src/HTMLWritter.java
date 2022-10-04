import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HTMLWritter {
    public HTMLWritter(String name, ArrayList<String> functions) throws IOException {
        File out = new File(name + ".html");
        FileWriter myWriter = new FileWriter(name + ".html");
        myWriter.write("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <script src=\"https://www.desmos.com/api/v1.7/calculator.js?apiKey=dcb31709b452b1cf9dc26972add0fda6\"></script>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <style>\n" +
                "        div {\n" +
                "        height: 100vh;\n" +
                "        }\n" +
                "    </style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"calculator\" ></div>\n" +
                "    <script>\n" +
                "    var elt = document.getElementById('calculator');\n" +
                "    var calculator = Desmos.GraphingCalculator(elt,{keypad: false, expressions: false});" +
                "calculator.setMathBounds({\n" +
                "  left: 0,\n" +
                "  right: 1080,\n" +
                "  bottom: -720,\n" +
                "  top: 0\n" +
                "});");
        int count = 0;
        for(String function: functions){
            myWriter.write("\n" +
                    "    calculator.setExpression({ id: 'graph "+count+"', latex: '"+function+"' });");
            count ++;
        }
        myWriter.write("</script>\n" +
                "</body>\n" +
                "</html>");
        myWriter.close();
    }
}
