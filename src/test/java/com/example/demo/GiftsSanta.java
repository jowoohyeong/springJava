package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class GiftsSanta {

    public boolean canFormSumWithN(List<Integer> list, int N, int S) {

        // N+1: 0부터 N까지, S+1: 0부터 S까지
        boolean[][] dp = new boolean[N + 1][S + 1];

        // 초기 상태: 0개를 선택해서 합 0을 만드는 것은 항상 가능.
        dp[0][0] = true;

        for (int gift : list) {

            // DP 갱신은 '역순'으로 진행하여, 현재 원소(gift)가 이번 단계에서 중복 사용되는 것을 방지합니다.
            // (Knapsack 문제의 일반적인 처리 방식)

            // 1. 선택 개수 (count)를 N부터 1까지 역순으로 순회
            for (int count = N; count >= 1; count--) {

                // 2. 총 합 (sum)을 S부터 현재 선물의 값(gift)까지 역순으로 순회
                for (int sum = S; sum >= gift; sum--) {

                    // 현재 상태 dp[count][sum]은 다음 두 경우 중 하나로 갱신됩니다:
                    // A. 이전 상태(gift를 포함하지 않음)가 이미 true인 경우: dp[count][sum] = dp[count][sum] (기존 값 유지)
                    // B. 이전 상태에서 gift를 추가하여 현재 상태가 된 경우: dp[count-1][sum - gift]가 true였다면 현재도 true

                    if (dp[count - 1][sum - gift]) {
                        dp[count][sum] = true;
                    }
                }
            }
        }

        return dp[N][S];
    }


    @Test // --- 예시 사용 ---
    void GiftSanta(String[] args) {
        List<Integer> gifts = Arrays.asList(2, 4, 6, 8, 10);
        int N1 = 3;
        int S1 = 12;
        System.out.println("N=" + N1 + ", S=" + S1 + ": " + canFormSumWithN(gifts, N1, S1)); // 결과: true

        int N2 = 2; // 2개 선택
        int S2 = 13; // 합 13
        System.out.println("N=" + N2 + ", S=" + S2 + ": " + canFormSumWithN(gifts, N2, S2)); // 결과: false

        int N3 = 4; // 4개 선택
        int S3 = 30; // 합 30
        System.out.println("N=" + N3 + ", S=" + S3 + ": " + canFormSumWithN(gifts, N3, S3)); // 결과: false (실제 계산 필요)
    }
}
