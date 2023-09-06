
public class Main {
    public static void main(String[] args) {
        try{
            Methods.printFactorial(5);
            Methods.printFirstThousandPrimes();
            String palindrome = "mom";
            System.out.println("Is '"+palindrome+"' a palindrome? "+Methods.isPalindrome(palindrome));
            int n = 5;
            System.out.println("Identity Matrix of '"+n+"' : "+Methods.getIdentityMatrix(n));
            Methods.getChange(17.35, 20.0);
            Methods.cesarCipher("cbobob", 1);
            Methods.vigenereCipher("LBMCOCJMSSDCX", "LIMAO");
            Methods.approveStudents();
            Methods.threeThreadsPlusOneMore();
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}