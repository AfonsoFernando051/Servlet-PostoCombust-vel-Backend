package posto;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PostoServlet")
public class PostoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private double precoGasolina;
    private double precoAlcool;
    private double precoDiesel;

    public PostoServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        precoGasolina = Double.parseDouble(config.getInitParameter("precoGasolina"));
        precoAlcool = Double.parseDouble(config.getInitParameter("precoAlcool"));
        precoDiesel = Double.parseDouble(config.getInitParameter("precoDiesel"));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Validação dos parâmetros
        String distanciaStr = request.getParameter("distancia");
        String velocidadeStr = request.getParameter("velocidadeMedia");
        String consumoStr = request.getParameter("consumoMedio");
        String calcularConsumo = request.getParameter("calcularConsumo");
        String tipoCombustivel = request.getParameter("tipoCombustivel");

        try {
            double distancia = Double.parseDouble(distanciaStr);
            double velocidadeMedia = Double.parseDouble(velocidadeStr);
            double tempoViagem = distancia / velocidadeMedia;

            // Exibir tempo estimado
            StringBuilder resultado = new StringBuilder();
            resultado.append("<h1>Resultado da Viagem</h1>");
            resultado.append("<p>Tempo estimado de viagem: ").append(tempoViagem).append(" horas</p>");

            // Cálculo do custo do combustível (se solicitado)
            if ("sim".equalsIgnoreCase(calcularConsumo)) {
                double consumoMedio = Double.parseDouble(consumoStr);
                double litrosConsumidos = distancia / consumoMedio;
                double precoCombustivel = 0;

                if ("gasolina".equals(tipoCombustivel)) {
                    precoCombustivel = precoGasolina;
                } else if ("alcool".equals(tipoCombustivel)) {
                    precoCombustivel = precoAlcool;
                } else if ("diesel".equals(tipoCombustivel)) {
                    precoCombustivel = precoDiesel;
                }

                double custoCombustivel = litrosConsumidos * precoCombustivel;
                resultado.append("<p>Custo estimado com combustível: R$ ").append(custoCombustivel).append("</p>");
            }

            // Exibir link para voltar
            resultado.append("<br><a href=\"index.html\">Voltar</a>");

            // Enviar resultado para o navegador
            response.setContentType("text/html");
            response.getWriter().write(resultado.toString());

        } catch (NumberFormatException e) {
            response.getWriter().write("<p>Erro: valores inválidos fornecidos. Por favor, tente novamente.</p>");
            response.getWriter().write("<br><a href=\"index.html\">Voltar</a>");
        }
    }
}
