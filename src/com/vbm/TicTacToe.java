package com.vbm;



import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by vbm on 29/05/2017.
 * Class for Go style game logic
 */

class TicTacToe  {

    private final char USER_CHAR = 'X';
    private final char COMP_CHAR = 'O';
    private final char EMPTY_CELL_CHAR = '.';
    private final int vector[][] = {{0, 1}, {1, 0}, {1, 1}, {-1, 1}};
    int BOARD_SIZE = 3;
    private boolean ifWithdraw;
    private int WIN_COINT = 3;
    private char[][] board;
    private boolean myMove = true;
    // Movecatcher movecatcher;
    /**
     * We assume the board is square, so we use only one dimension.
     */


    private int userMoves;
    private int compMoves;
    private int curX;
    private int curY;
    private boolean gameOver;

    TicTacToe() {

        // Initialize count of moves;
        userMoves = 0;
        compMoves = 0;
        gameOver = false;
        board = new char[BOARD_SIZE][BOARD_SIZE];
        initBoard();
        //this.movecatcher = movecatcher;
        //printBoard();

    }


    private void initBoard() {
        for (int i = 0; i < BOARD_SIZE; ++i)
            for (int j = 0; j < BOARD_SIZE; ++j)
                board[i][j] = EMPTY_CELL_CHAR;
    }



    private void userMove(int a, int b) {
        Scanner sc = new Scanner(System.in);
        curY = a;
        curX = b;
        board[curY][curX] = USER_CHAR;
        userMoves++;
    }

    private void compMove() {
        double maxMove, curMove;
        int maxX, maxY;
        maxMove = -5000;
        maxX = maxY = 0;

        if (!myMove && compMoves == 0) {
            curX = curY = BOARD_SIZE / 2;
            board[curY][curX] = COMP_CHAR;
            compMoves++;
            return;
        }
        ifWithdraw = true;
        compMoves++;

        for (int i = 0; i < BOARD_SIZE; ++i)
            for (int j = 0; j < BOARD_SIZE; ++j) {
                if (board[i][j] != EMPTY_CELL_CHAR) continue;
                if (maxMove <= (curMove = moveWeigth(j, i, COMP_CHAR))) {
                    maxMove = curMove;
                    // Check here X Y assign for i j ? Debug
                    maxX = j;
                    maxY = i;

                }
            }

        for (int i = 0; i < BOARD_SIZE; ++i)
            for (int j = 0; j < BOARD_SIZE; ++j) {
                if (board[i][j] != EMPTY_CELL_CHAR) continue;
                if (maxMove < (curMove = moveWeigth(j, i, USER_CHAR))) {
                    maxMove = curMove;
                    // Check here X Y assign for i j ? Debug
                    maxX = j;
                    maxY = i;

                }
            }

        board[maxY][maxX] = COMP_CHAR;
        curY = maxY;
        curX = maxX;
    }

    private int checkWin(char whoisChar) {
        if (userMoves + compMoves == BOARD_SIZE * BOARD_SIZE) {
            myMove = !myMove;
            return 0;
        }
        int t_x, t_y;
        int n;
        for (int[] aVector : vector) {
            n = 1;
            t_x = curX;
            t_y = curY;

            while (t_x < BOARD_SIZE && t_x >= 0 && t_y < BOARD_SIZE && t_y >= 0) {
                if (board[t_y][t_x] != whoisChar) break;
                if (t_x != curX || t_y != curY) ++n;
                t_x += aVector[0];
                t_y += aVector[1];
            }

            t_x = curX;
            t_y = curY;

            while (t_x < BOARD_SIZE && t_x >= 0 && t_y < BOARD_SIZE && t_y >= 0) {
                if (board[t_y][t_x] != whoisChar) break;
                if (t_x != curX || t_y != curY) ++n;
                t_x -= aVector[0];
                t_y -= aVector[1];
            }
            if (n >= WIN_COINT) {
                //System.out.println("User " + whoisChar + " wins! ");
                myMove = !myMove;
                return 1;
            }
        }
        return -1;
    }

    private double moveWeigth(int a, int b, char moveChar) {
        double moveWeigth, curWeigth = 0.0, elseweigth = 0.0;
        int n;
        int t_x, t_y;


        for (int[] aVector : vector) {
            // Compare line finishing
            n = 1;
            int ms = linelength(a, b, moveChar, aVector);
            if (ms < WIN_COINT ) continue;
            else
                ifWithdraw = false;
            moveWeigth = 0.0;
            for (int c = -1; c < 2; c += 2) {

                t_x = a;
                t_y = b;
                while (t_x < BOARD_SIZE && t_x >= 0 && t_y < BOARD_SIZE && t_y >= 0) {
                    if (t_x != a || t_y != b) {
                        if (board[t_y][t_x] != moveChar) {
                            if (board[t_y][t_x] != EMPTY_CELL_CHAR) moveWeigth -= 85;
                            break;
                        } else ++n;
                        moveWeigth += 100 + 10 * n;
                    }

                    t_x += c * aVector[0];
                    t_y += c * aVector[1];

                    if (t_x < 0 || t_x == BOARD_SIZE || t_y < 0 || t_y == BOARD_SIZE)
                        moveWeigth -= 55;
                }


            }
            //moveWeigth += n * 10;
            if (n >= WIN_COINT && moveChar == COMP_CHAR) return 5000;
            if (n >= WIN_COINT && moveChar == USER_CHAR) return 2000;
            if (n == WIN_COINT - 1 && moveWeigth > (WIN_COINT - 1.5) * 100) moveWeigth += 300;
            if (curWeigth < moveWeigth) curWeigth = moveWeigth;
            elseweigth += moveWeigth > 0 ? moveWeigth * 0.3 : 0;
            //elseweigth += ms*10;

        }
        elseweigth += curWeigth;
        if (a == 0 || a == BOARD_SIZE - 1 || b == 0 || b == BOARD_SIZE - 1) elseweigth -= 55;

        return elseweigth;
    }

    private int linelength(int a, int b, char moveChar, int[] aVector) {

        int t_x;
        int t_y;


        int n = 1;

        for (int k = -1; k < 2; k += 2) {
            t_x = a;
            t_y = b;
            /*t_x += aVector[0] * k;
            t_y += aVector[1] * k;*/
            while (t_x < BOARD_SIZE && t_x >= 0 && t_y < BOARD_SIZE && t_y >= 0) {
                if (board[t_y][t_x] != moveChar && board[t_y][t_x] != EMPTY_CELL_CHAR) break;
                ++n;
                t_x += aVector[0] * k;
                t_y += aVector[1] * k;
            }

        }
        --n;
        return n;
    }


    void uMove() {
        int a, b;
        do {
            if (gameOver) {
                gameOver = false;
                initBoard();
                curY = 0;
                curX = 0;
                userMoves = 0;
                compMoves = 0;

                if (myMove)
                    return;
            }
            printArray();
            do {
                Scanner sc = new Scanner((System.in));
                a = sc.nextInt()-1;
                b = sc.nextInt()-1;
            } while (board[a][b] != '.');
            printArray();

            curY = a;
            curX = b;

            userMoves++;
            board[curY][curX] = USER_CHAR;
            if (userMoves + compMoves == BOARD_SIZE * BOARD_SIZE) {
                gameOver = true;
                //myMove = !myMove;
                System.out.println("Withdraw!");
                return;
            }
            //Toast.makeText(b.getContext(), "User move number: " + (++userMoves), Toast.LENGTH_SHORT).show();
            if (checkWin(USER_CHAR) != -1) {
                gameOver = true;
                System.out.println("You win!");
                return;
            }



            if (!gameOver) {
                compMove();
                printArray();


                if (checkWin(COMP_CHAR) == 1) {
                    //myMove = !myMove;
                    gameOver = true;
                    System.out.println("i win!");
                    return;
                }
                if (ifWithdraw) {
                    gameOver = true;
                    myMove = !myMove;
                    System.out.println("Withdraw!");
                    ifWithdraw = false;
                    return;
                }
            }


        }while (true);
    }

  private void printArray(){
        for(char[] temp: board){
            System.out.println(Arrays.toString(temp));
        }
  }
}

