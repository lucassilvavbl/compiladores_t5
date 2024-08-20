package br.ufscar.dc.compiladores.t5;

import br.ufscar.dc.compiladores.t5.t5GramParser.ProgramaContext;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class T5 {

    public static void main(String args[]) throws IOException {

        // Leitura e escrita em arquivo
        CharStream cs = CharStreams.fromFileName(args[0]);

        // Analisador léxico
        t5GramLexer lexer = new t5GramLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Analisador sintático
        t5GramParser parser = new t5GramParser(tokens);
        ProgramaContext arvore = parser.programa();
        T5Semantico t5s = new T5Semantico();

        // Inicialização do programa
        t5s.visitPrograma(arvore);

        // Procura por erros, imprime todos os que foram identificados
        // e encerra a execução do analisador
        T5SemanticoUtils.errosSemanticos.forEach((s) -> System.out.println(s));

        // Caso não tenham erros no programa, é gerado o código em C equivalente
        if (T5SemanticoUtils.errosSemanticos.isEmpty()) {
            GeraC agc = new GeraC();
            agc.visitPrograma(arvore);

            try (PrintWriter pw = new PrintWriter(args[1])) {
                pw.print(agc.saida.toString());
            }
        }
    }
}
