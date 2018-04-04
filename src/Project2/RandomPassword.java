package Project2;

public class RandomPassword {
    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static String randomPassword() {
        StringBuilder str = new StringBuilder(8);
        int randomNums = (int) (Math.random() * 9999);
        str.append(String.valueOf(randomNums));

        for (int i = 0; i < 4; i++) {
            int randomNum = (int) (Math.random() * 26);
            str.append(String.valueOf(alphabet.substring(randomNum, randomNum + 1)));
        }

        return str.toString();
    }
}    