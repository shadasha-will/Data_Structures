//
//  main.c
//  Data Structures HW 3
//
//  Created by Shadasha Williams on 12/6/17.

#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <stdio.h>

int count = 0;
int MAX_MATRIX = 130;
#define true 1
#define false 0
// run_type will either run a naive or a oblivious
// exp_type will either run the timer or the simulator implementation
#define run_type
#define exp_type

void naive_transpose(int size, int A[size][size], int startrow, int startcol, int endrow, int endcol) {
    for(int i = startrow; i < endrow ; i++) {
        for(int j = startcol ; j < endcol; j++) {
            int tmp;
#ifdef exp_type
            printf("X %d %d %d %d \n", i, j , j, i);
#endif
            tmp = A[i][j] ;
            A[i][j] = A[j][i];
            A[j][i] = tmp;
            count++;
        }
    }
}



void transpose_and_swap(int size, int A[size][size], int startrow, int startcol, int endrow, int endcol){
    int mat_min = 2;
    if ((endrow - startrow) <= mat_min && (endcol - startcol) <= mat_min){
        //when the matrix is smaller than 2 by 2 we do naive
        naive_transpose(size, A, startrow, startcol, endrow, endcol);
    }
    else
    {
        int mid1 = (endrow + startrow) / 2;
        int mid2 = (endcol + startcol) / 2;
        // swap all the quadrants
        transpose_and_swap(size, A, startrow, startcol, mid1, mid2);
        transpose_and_swap(size, A, mid1, startcol , endrow, mid2);
        transpose_and_swap(size, A, startrow, mid2 , mid1, endcol);
        transpose_and_swap(size, A, mid1, mid2, endrow, endcol);
    }
    
}

void transpose_on_diag(int size, int A[size][size], int start, int end){
    if (end - start <= 2) {
        // if smaller than 2x2 do naive
        naive_transpose(size, A, start, start + 1, end - 1, end);
    }
    
    else {
        int mid = (start + end) / 2;
        transpose_on_diag(size, A, start, mid);
        transpose_on_diag(size, A, mid, end);
        transpose_and_swap(size, A, mid, start, end, mid);
    }
    
}

void print_matrix(int size, int *A) {
    for(int i = 0; i < size; i++) {
        for(int j = 0; j < size; j++) {
            printf(" %d "  , A[i * size + j] );
        }
        printf("\n");
    }
}

void initialize_matrix(int N, int *A){
    for(int i = 0; i < N; i++) {
        for(int j = 0; j < N; j++) {
            A[i * N + j] = rand();
        }
    }
}

void print_time(clock_t t) {
    t = clock() - t;
    // get time in nanoseconds
    double seconds = ((double)t * 1000000000) / (CLOCKS_PER_SEC * count);
    // print time in nanoseconds
    printf("%f \n", seconds);
}

int main() {
    // create a variable for time
#ifdef run_type
#ifdef exp_type
    // running cache oblivious simulator
    printf("Running cache oblivious simulator");
    for(int x = 54; x < MAX_MATRIX; x++) {
        count = 0;
        int N = ceil(pow(2, (double)(x / 9.0)));
        int *A = (int *)malloc(N * N * sizeof(int));
        initialize_matrix(N, A);
        printf("N %u \n", N);
        transpose_on_diag(N, A, 0, N);
        free(A);
        printf("E \n");}
#endif
#else
    printf("Running cache oblivous timer");
    // running cache oblivious timer
    for(int x = 54; x < MAX_MATRIX; x++) {
        count = 0;
        clock_t timer;
        int N = ceil(pow(2, (double)(x / 9.0)));
        int *A = (int *)malloc(N * N * sizeof(int));
        initialize_matrix(N, A);
        printf("%u ", N);
        timer = clock();
        transpose_on_diag(N, A, 0, N);
        print_time(timer);
        free(A);}
#endif
#ifndef run_type
#ifdef exp_type
    // running naive timer
    for(int x = 54; x < MAX_MATRIX; x++) {
        count = 0;
        clock_t timer;
        int N = ceil(pow(2, (double)(x / 9.0)));
        int *A = (int *)malloc(N * N * sizeof(int));
        printf("Running Naive Timer");
        initialize_matrix(N, A);
        printf("%u ", N);
        timer = clock();
        naive_transpose(N, A, 0, 0, N-1, N-1);
        print_time(timer);
        free(A); }
#endif
#else
    // running naive simulator
    printf("Running Naive Simulator");
    for(int x = 54; x < MAX_MATRIX; x++) {
        count = 0;
        int N = ceil(pow(2, (double)(x / 9.0)));
        int *A = (int *)malloc(N * N * sizeof(int));
        initialize_matrix(N, A);
        printf("N %u \n", N);
        naive_transpose(N, A, 0, 0, N-1, N-1);
        free(A);
        printf("E \n");}
#endif
    
    
    
    
    
    
    return 0;
}

