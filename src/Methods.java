import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Methods{
    //Creating a logger to print some outputs
    private static final Logger logger = Logger.getLogger(Methods.class.getName());
    //Creating a CountDownLatch, to count the number of task that finished
    private static final CountDownLatch latch = new CountDownLatch(3);

    /* Question 1)
     *  Implemente um método que receba um número e imprima o resultado do fatorial deste número.
     * */
    public static void printFactorial(int n){
        System.out.println(printFactorialR(n));
    }
    private static int printFactorialR(int n){
        return (n == 1) ? n : n * printFactorialR(n - 1);
    }

    /* Question 2)
    *   Imprima todos os números primos de 0 a 1000.
    * */
    public static void printFirstThousandPrimes(){
        for (int i = 0; i < 1000; i++) {
            if(prime(i, 2)) {
                System.out.println(i);
            }
        }
    }
    private static boolean prime(int n, int f){
        if(n <= 2 ) return n == 2;
        if(n % f == 0) return false;
        if(f * f > n) return true;
        return prime(n, f+ 1);
    }

    /* Question 3)
     *   Crie um método que receba como parâmetro um texto do tipo String e retorne se este texto é palíndromo..
     * */
    public static boolean isPalindrome(String s){
        return isPalindromeR(s, 0);
    }
    private static boolean isPalindromeR(String s, int i){
        if(i > s.length()/2) return true;
        if(s.charAt(i) != s.charAt((s.length()-1)-i)) return false;
        return isPalindromeR(s, i+1);
    }

    /* Question 4)
     *   Crie um método que receba um número inteiro e gere uma matriz identidade a partir deste número
     *      Exemplo:
     *          Número: 3
     *          Matriz: 1 0 0
     *                  0 1 0
     *                  0 0 1
     * */
    public static List<List<Integer>> getIdentityMatrix(int n){
        List<List<Integer>> identityMatrix = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            identityMatrix.add(createRow(i, n));
        }
        return identityMatrix;
    }
    private static List<Integer> createRow(int i, int n){
        List<Integer> row = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            row.add(j, (j == i) ? 1 : 0);
        }
        return row;
    }

    /* Question 5)
     *  Crie um método que receba o custo e o pagamento, ele deve imprimir no console as seguintes informações:
     *      - Valor do troco
     *      - Troco em menor quantidade de notas e moedas possiveis
     *      Exemplo:
     *          custo: R$ 17,35
     *          pagamento: R$ 20,00
     *          troco: R$ 2,65
     *          menor troco: 1 - nota de 2
     *                       1 - moeda de 50
     *                       1 - moeda de 10
     *                       1 - moeda de 5
     * */
    public static void getChange(double cost, double pay){
        System.out.println("Cost : R$ "+round(cost));
        System.out.println("Payment : R$ "+round(pay));
        double change = round(pay-cost);
        System.out.println("Change : R$ "+(change));
        System.out.println("Smaller change possible : ");
        getSmallerChange(change).forEach((s, l) -> {
            if(l.get(1) > 0) System.out.println("\t"+s+l.get(1));
        });
    }
    private static LinkedHashMap<String,  List<Integer>> getSmallerChange(double change){
        LinkedHashMap<String, List<Integer>> change_dict = createChangeDict();
        int changeI = (int) (change*100);
        for (Map.Entry<String, List<Integer>> entry: change_dict.entrySet()) {
            change_dict.put(entry.getKey(), List.of(entry.getValue().get(0), changeI / entry.getValue().get(0)));
            changeI = changeI - (entry.getValue().get(0) * entry.getValue().get(1));
        }
        return change_dict;
    }
    private static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    private static LinkedHashMap<String, List<Integer>> createChangeDict() {
        LinkedHashMap<String, List<Integer>> change_dict = new LinkedHashMap<>();
        change_dict.put("Notas de 200 : ",List.of(20000, 0));
        change_dict.put("Notas de 100 : ",List.of(10000, 0));
        change_dict.put("Notas de 50 : ",List.of(5000, 0));
        change_dict.put("Notas de 20 : ",List.of(2000, 0));
        change_dict.put("Notas de 10 : ",List.of(1000, 0));
        change_dict.put("Notas de 5 : ",List.of(500, 0));
        change_dict.put("Notas de 2 : ",List.of(200, 0));
        change_dict.put("Moedas de 1 : ",List.of(100, 0));
        change_dict.put("Moedas de 0.50 : ",List.of(50, 0));
        change_dict.put("Moedas de 0.25 : ",List.of(25, 0));
        change_dict.put("Moedas de 0.10 : ",List.of(10, 0));
        change_dict.put("Moedas de 0.05 : ",List.of(5, 0));
        change_dict.put("Moedas de 0.01 : ",List.of(1, 0));
        return change_dict;
    }

    /* Question 6)
     *   - Implemente a cifra de César para criptografar e descriptografar uma mensagem.
     *      teoria: https://pt.wikipedia.org/wiki/Cifra_de_C%C3%A9sar
     *      A aplicação deve:
     *          - COLETAR DADOS:
     *              • Receber um texto
     *              • Receber um número
     *          - OFERECER OPÇÕES:
     *              1 - para criptografar
     *              2- para descriptografar
     *          - REALIZAR A AÇÃO SELECIONADA E IMPRIMIR O RESULTADO NA TELA:
     * */
    public static void cesarCipher(String word, int n) {
        Scanner in = new Scanner(System.in);
        System.out.println("Press 1 to cipher or any other key to decipher: ");
        String s = in.nextLine();
        boolean cipher = s.equals("1");
        char[] wordC = word.toCharArray();
        for (int i = 0; i < wordC.length; i++) {
            if (cipher) wordC[i] += (char) n;
            else wordC[i] -= (char) n;
        }
        word = new String(wordC);
        System.out.println(word);
    }

    /* Question 7)
     *   Implemente a cifra de Vigenère para criptografar e descriptografar uma mensagem.
     *      teoria: https://pt.wikipedia.org/wiki/Cifra_de_Vigen%C3%A8re
     *      A aplicação deve:
     *          - COLETAR DADOS:
     *              • Receber um texto
     *              • Receber um número
     *          - OFERECER OPÇÕES:
     *              1 - para criptografar
     *              2- para descriptografar
     *          - REALIZAR A AÇÃO SELECIONADA E IMPRIMIR O RESULTADO NA TELA:
     * */
    public static void vigenereCipher(String word, String key){
        key = generateKey(word, key);
        Scanner in = new Scanner(System.in);
        System.out.println("Press 1 to cipher or any other key to decipher: ");
        String s = in.nextLine();
        boolean cipher = s.equals("1");
        char[] wordC = word.toUpperCase().toCharArray();
        for (int i = 0; i < wordC.length; i++) {
            if (cipher) wordC[i] = (char) ((wordC[i] - 65) + (key.charAt(i) - 65) % 26 + 65);
            else wordC[i] = (char) ((wordC[i] - key.charAt(i) + 26) % 26 + 65);
        }
        word = new String(wordC);
        System.out.println(word);
    }
    private static String generateKey(String word, String key) {
        if(word.length() == key.length())
            return key.toUpperCase();
        key = key.repeat(word.length()/key.length()+1);
        return key.substring(0, word.length()).toUpperCase();
    }

    /* Question 8)
     *   Crie um arquivo na raiz do seu projeto chamado "notas.txt"
     *   Neste arquivo terá as seguintes informações separadas pelo caracter ";"
     *      (nome, % de presença nas aulas, nota1, nota2, nota3)
     *      Exemplo de dados no arquivo:
     *          Pedro;99;8;9;7
     *          Julia;50;10;9;8
     *          Marcos;100;4;1;2
     *          Ana;90;6;6;7
     *      Condições para os alunos serem aprovados:
     *          • 70% de presença nas aulas;
     *          • Média das notas a partir de 7;
     *      Condições para os alunos poderem tentar uma prova de recuperação:
     *          • 70% de presença nas aulas;
     *          • Média das notas a partir de 4;
     *      Condições para os alunos serem reprovados (possuindo pelo menos uma, já reprova):
     *          • menos de 70% de presença nas aulas;
     *          • Média das notas menor que 4;
     *      Crie um arquivo chamado "resultado.txt", escreva neste arquivo o nome dos alunos e seu
     * status (Aprovado, Recuperação, Reprovado) após a análise dos dados;
     *          Exemplo:
     *              Fulano;Aprovado
     *              Beltrano;Reprovado
     *              Ciclano;Recuperação
     * */
    public static void approveStudents(){
       try{
           writeFile(readFile());
       }catch (Exception e){
           throw new RuntimeException(e);
       }
    }
    private static void writeFile(HashMap<String, String> students) {
        try{
            BufferedWriter bf = new BufferedWriter(new FileWriter("resultado.txt"));
            for(Map.Entry<String, String> entry: students.entrySet()){
                bf.write(entry.getKey().concat(":").concat(entry.getValue()));
                bf.newLine();
            }
            bf.close();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    private static HashMap<String, String> readFile() {
        HashMap<String, String> students;
        try {
            FileInputStream is = new FileInputStream("notas.txt");
            Scanner sc = new Scanner(is, StandardCharsets.UTF_8);
            int average;
            students = new HashMap<>();
            while (sc.hasNextLine()) {
                List<String> line = List.of(sc.nextLine().split(";"));
                if (Integer.parseInt(line.get(1)) >= 90) {
                    average = Integer.parseInt(line.get(2)) + Integer.parseInt(line.get(3)) + Integer.parseInt(line.get(4)) / 3;
                    if ((average >= 7)) students.put(line.get(0), "Aprovado");
                    else if ((average >= 4)) students.put(line.get(0), "Recuperação");
                    else students.put(line.get(0), "Reprovado");
                } else {
                    students.put(line.get(0), "Reprovado");
                }
            }
            is.close();
            sc.close();
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    /* Question 9)
     *  Crie 3 tarefas a serem executadas paralelamente, cada tarefa vai imprimir seu nome durante a execução (tarefa_a, tarefa_b, tarefa_c).
     *  Gere um tempo randômico para execução de cada tarefa (Você pode utilizar a função Thread.sleep() para isso).
     *  Crie uma nova tarefa (tarefa_fim), esta tarefa deve imprimir seu nome somente quando a tarefa 1, 2 e 3 forem concluídas
     * */
    public static synchronized void threeThreadsPlusOneMore() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(3);
        ExecutorService finalEs = es;
        IntStream.range(0, 3).forEach(i -> finalEs.submit(new Task("Task_"+i)));
        es.shutdown();
        latch.await();
        es = Executors.newFixedThreadPool(1);
        es.submit(new Task("Final_Task"));
        es.shutdown();
    }
    private static void log(String message){
        logger.info(Thread.currentThread()+" : "+message);
    }
    private static void sleep(Duration duration) throws InterruptedException {
        Thread.sleep(duration);
    }
    private record Task(String name) implements Runnable {
        @Override
            public void run() {
                int time = new Random().nextInt(10000 - 1000 + 1);
                try {
                    sleep(Duration.ofMillis(time));
                    log("Hello, I'm thread " + this.name + " and I took " + time + " millis to run");
                    latch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
}
