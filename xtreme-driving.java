import java.io.*;
import java.util.*;

public class Solution {
    static long MOD = 1000000007;

    static class Matrix {
        static Matrix identity() {
            Matrix result = new Matrix();
            for (int i = 0; i < 4; ++i) {
                result.vals[i][i] = 1;
            }
            return result;
        }
        static Matrix identity(int n) {
            Matrix result = new Matrix();
            result.vals[n][n] = 1;
            return result;
        }
        long[][] vals = new long[4][4];
    }

    static Matrix multiply(Matrix a, Matrix b) {
        Matrix result = new Matrix();
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                for (int m = 0; m < 4; ++m) {
                    result.vals[i][j] += (a.vals[i][m] * b.vals[m][j]) % MOD;
                }
                result.vals[i][j] %= MOD;
            }
        }
        return result;
    }
    
    static long[] multiply(Matrix a, long[] v) {
        long[] result = new long[4];
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                result[i] += (a.vals[i][j] * v[j]) % MOD;
            }
            result[i] %= MOD;
        }
        return result;
    }

    static Matrix[] powersOfA = new Matrix[64];
    static {
        powersOfA[0] = new Matrix();
        powersOfA[0].vals[0][0] = 1L;
        powersOfA[0].vals[1][0] = 1L;
        powersOfA[0].vals[2][0] = 0L;
        powersOfA[0].vals[3][0] = 0L;
        powersOfA[0].vals[0][1] = 1L;
        powersOfA[0].vals[1][1] = 1L;
        powersOfA[0].vals[2][1] = 1L;
        powersOfA[0].vals[3][1] = 0L;
        powersOfA[0].vals[0][2] = 0L;
        powersOfA[0].vals[1][2] = 1L;
        powersOfA[0].vals[2][2] = 1L;
        powersOfA[0].vals[3][2] = 1L;
        powersOfA[0].vals[0][3] = 0L;
        powersOfA[0].vals[1][3] = 0L;
        powersOfA[0].vals[2][3] = 1L;
        powersOfA[0].vals[3][3] = 1L;
        
        for (int i = 1; i < 64; ++i) {
            powersOfA[i] = multiply(powersOfA[i-1], powersOfA[i-1]);
        }
    }
    
    static long K;
    static int N;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        K = in.nextLong();
        N = in.nextInt();
        
        Map<Long, int[]> cows = new TreeMap<>();
        
        for (int i = 0; i < N; ++i) {
            int x = in.nextInt();
            long y = in.nextLong();
            int[] xx = cows.get(y-1);
            if (xx == null) {
                xx = new int[4];
                for (int j = 0; j < 4; ++j) {
                    xx[j] = 1;
                }
                xx[x-1] = 0;
                cows.put(y-1, xx);
            } else {
                xx[x-1] = 0;
            }
        }
        
        long[] v = new long[] {1L, 0L, 0L, 0L};
        long y0 = 0;
        for (long y : cows.keySet()) {
            v = multiply(computeMatrix(y-y0), v);
            int[] xx = cows.get(y);
            for (int i = 0; i < 4; ++i) {
                if (xx[i] == 0)
                    v[i] = 0L;
            }
            y0 = y;
        }
        v = multiply(computeMatrix(K-1-y0), v);
        
        System.out.println(v[0]);
    }

    static Matrix computeMatrix(long x) {
        Matrix result = Matrix.identity();
        int power = 0;
        while (x > 0) {
            if (x % 2 == 1) {
                result = multiply(powersOfA[power], result);
            }
            x >>= 1;
            ++power;
        }
        return result;
    }
}