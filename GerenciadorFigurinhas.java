public class GerenciadorFigurinhas {

    public static final int MAX = 1000; // quantidade máxima de figurinhas
    public static final String ARQ_BASE = "figurinhas_completo.csv"; // arquivo com todas as figurinhas
    public static final String ARQ_OBTIDAS = "figurinhas_obtidas.csv"; // arquivo com as figurinhas obtidas

    public static void main(String[] args) {

        String[] codigo = new String[MAX]; // vetor de códigos
        String[] jogador = new String[MAX]; // vetor de jogadores
        String[] selecao = new String[MAX]; // vetor de seleções
        int[] quantidade = new int[MAX]; // quantidade obtida de cada figurinha

        int total = carregarBase(codigo, jogador, selecao); // carrega todas as figurinhas
        carregarObtidas(codigo, quantidade, total); // carrega o progresso salvo

        char op; // opção do menu

        do {
            op = menu(); // exibe o menu e recebe a opção

            if(op == '1'){
                registrarObtida(codigo, quantidade, total); // registra uma figurinha
                salvarObtidas(codigo, quantidade, total); // salva alterações
            }

            if(op == '2')
                listar(codigo, jogador, selecao, quantidade, total); // lista as obtidas

            if(op == '3')
                pesquisar(codigo, jogador, selecao, quantidade, total); // pesquisa figurinha

            if(op == '4'){
                removerRepetida(codigo, quantidade, total); // remove uma repetida
                salvarObtidas(codigo, quantidade, total); // salva alterações
            }

            if(op == '5')
                relatorio(quantidade, total); // mostra relatório

        } while(op != '0'); // repete até o usuário escolher sair

        salvarObtidas(codigo, quantidade, total); // salva antes de encerrar
    }

    public static char menu() {

        String m =
                "GERENCIADOR DE FIGURINHAS\n" +
                "1-Registrar Obtida\n" +
                "2-Listar Obtidas\n" +
                "3-Pesquisar por Codigo\n" +
                "4-Remover Repetida\n" +
                "5-Relatorio\n" +
                "0-Sair";

        return Entrada.leiaChar(m,' '); // retorna a opção escolhida
    }

    public static void registrarObtida(String[] c, int[] q, int total){

        String cod = Entrada.leiaString("Codigo:"); // lê o código digitado
        int p = buscarCodigo(c,total,cod); // procura a figurinha

        if(p >= 0)
            q[p]++; // aumenta a quantidade obtida
        else
            Entrada.leiaChar("Figurinha não encontrada",' '); // mensagem de erro
    }

    public static void listar(String[] c,String[] j,String[] s,int[] q,int total){

        int obtidas = 0; // contador

        System.out.println("\n===== FIGURINHAS OBTIDAS =====\n");

        for(int i = 0; i < total; i++){ // percorre todas as figurinhas

            if(q[i] > 0){ // verifica se foi obtida

                System.out.println(
                        c[i] + " - " +
                        j[i] + " - " +
                        s[i] + " - Qt:" + q[i]
                ); // exibe os dados

                obtidas++; // soma no contador
            }
        }

        Entrada.leiaChar("Total de figurinhas obtidas: " + obtidas, ' ');
    }

    public static void pesquisar(String[] c, String[] j, String[] s, int[] q, int total){

        String cod = Entrada.leiaString("Código:"); // lê o código
        int p = buscarCodigo(c,total,cod); // procura no vetor

        if(p >= 0){

            String txt = c[p] + " - " + j[p] + " - " + s[p] + " - Quant:" + q[p]; // monta o texto

            Entrada.leiaChar(txt,' '); // mostra os dados

        } else {

            Entrada.leiaChar("Nenhuma figurinha encontrada",' '); // não encontrou
        }
    }

    public static void removerRepetida(String[] c, int[] q, int total){

        String cod = Entrada.leiaString("Código:"); // lê o código
        int p = buscarCodigo(c,total,cod); // procura a figurinha

        if(p < 0){
            Entrada.leiaChar("Figurinha não encontrada",' ');
            return; // encerra o método
        }

        if(q[p] > 1){ // verifica se possui repetidas
            q[p]--; // remove uma unidade
            Entrada.leiaChar("Repetida removida",' ');
        }
        else{
            Entrada.leiaChar("Não há repetidas desta figurinha",' ');
        }
    }

    public static void relatorio(int[] q, int total){

        int obtidas = 0; // contador de obtidas
        int faltantes = 0; // contador de faltantes

        for(int i=0;i<total;i++){

            if(q[i] > 0)
                obtidas++; // possui a figurinha
            else
                faltantes++; // ainda falta
        }

        double perc = (obtidas * 100.0) / total; // calcula porcentagem

        Entrada.leiaChar("Obtidas: " + obtidas + "\nFaltantes: " + faltantes + "\nProgresso: " + perc + "%", ' ');
    }

    public static int buscarCodigo(String[] c, int total, String cod){

        for(int i=0; i < total; i++){ // percorre o vetor

            if(c[i].equalsIgnoreCase(cod)) // compara os códigos
                return i; // retorna a posição encontrada
        }

        return -1; // não encontrou
    }

    public static int carregarBase(String[] c, String[] j, String[] s) {

        int total = 0; // contador de registros

        Arquivo arq = new Arquivo(ARQ_BASE); // abre arquivo principal

        if(arq.abrirLeitura()){

            String reg = arq.lerLinha(); // lê primeira linha

            while(reg != null){ // enquanto houver linhas

                String[] p = reg.split(";"); // separa os campos

                if(p.length >= 3){

                    c[total] = p[0]; // código
                    j[total] = p[1]; // jogador
                    s[total] = p[2]; // seleção

                    total++; // próximo registro
                }

                reg = arq.lerLinha(); // lê próxima linha
            }

            arq.fecharArquivo(); // fecha arquivo
        }

        return total; // retorna quantidade carregada
    }

    public static void carregarObtidas(String[] codigo, int[] quantidade, int total){

        Arquivo arq = new Arquivo(ARQ_OBTIDAS); // abre arquivo de progresso

        if(arq.abrirLeitura()){

            String reg = arq.lerLinha();

            while(reg != null){

                String[] p = reg.split(";"); // separa código e quantidade

                if(p.length >= 2){

                    int pos = buscarCodigo(codigo, total, p[0]); // procura código

                    if(pos >= 0)
                        quantidade[pos] = Integer.parseInt(p[1]); // grava quantidade
                }

                reg = arq.lerLinha();
            }

            arq.fecharArquivo(); // fecha arquivo
        }
    }

    public static void salvarObtidas(String[] codigo, int[] quantidade, int total){

        Arquivo arq = new Arquivo(ARQ_OBTIDAS); // abre arquivo de saída

        arq.abrirEscrita(false); // sobrescreve o arquivo

        for(int i = 0; i < total; i++){

            if(quantidade[i] > 0){ // salva apenas as obtidas

                arq.escreverLinha(codigo[i] + ";" + quantidade[i]);
            }
        }

        arq.fecharArquivo(); // fecha arquivo
    }
}