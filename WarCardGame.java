/** This program generates a randomly shuffled
deck of cards, and plays a game of War to its 
conclusion, while counting the number of battles.*/

public class WarCardGame {
    private static class Player {       //Stores a player's deck, adds/removes cards
        char[] playerDeck = new char[52];       //Cards in beginning of array, extra space filled by null
        int deckLength = 26;
        public Player (char[] inputDeck) {      //Constructor
            for (int i=0;i<=25;i++) {
                playerDeck[i] = inputDeck[i];
                playerDeck[26+i] = '\u0000';
            }
        }//Constructor
        public char drawCard() {            //Removes card from beginning of deck
            char drawnCard = playerDeck[0]; //and moves each other card up one spot
            if (drawnCard == '\u0000') {
                return drawnCard;
            }
            deckLength -= 1;
            for (int i=1;i<=51;i++) {
                if (playerDeck[i] == '\u0000') {
                    playerDeck[i-1] = '\u0000';
                    break;
                }
                playerDeck[i-1] = playerDeck[i];
            }
            return drawnCard;
        }//drawCard
        public char[] getPlayerDeck() {
            return playerDeck;
        }
        public void recycleCard(char recycledCard) {  //Replaces first null slot with card
            playerDeck[deckLength] = recycledCard;
            deckLength += 1;
        }//recycleCard
    }//Player class
    public static class Game {                  //Contains methods to deal the cards, hold Battles and award cards to winners
        static char[] cards = {'2','3','4','5','6','7','8','9','T','J','Q','K','A'};
        static char[] deck = new char[52];
        static char[][] dealtDecks = new char[2][26];
        public static void genDeck() {         //Creates deck of 52 cards in random order
            for (char c:cards) {
                for (int i=0;i<=3;i++) {
                    while (true) {
                        int randNum = (int)(Math.random()*52);
                        if(deck[randNum] == '\u0000') {
                            deck[randNum] = c;
                            break;
                        }
                    }
                }      
            }   
        }//genDeck
        public static void dealCards() {       //Distributes cards to players, alternating
            for (int i=0;i<=51;i++) {
                if (i%2 == 0) {
                    dealtDecks[i%2][i/2] = deck[i];
                }else {
                    dealtDecks[i%2][i/2] = deck[i];
                }
            }
        }//dealCards
        public static char[] getDeck(int decknum) {     //Retrieves one of two halves of the deck
            return dealtDecks[decknum-1];
        }//getDeck
        public static char compareCards(char firstCard,char secondCard) {      //Compares 2 card values
            int firstCardNum=0;
            int secondCardNum=0;
            for (int i=0;i<= 12;i++) {
                if (cards[i] == firstCard) {
                    firstCardNum = i;
                }
                if (cards[i] == secondCard) {
                    secondCardNum = i;
                }    
            }
            if (firstCardNum > secondCardNum) {
                return firstCard;
            }else {
                return secondCard;
            }
        }//compareCards
        public static void awardCards(Player player,char card,char otherCard,char[] pool) {//, char[] secondPool) {    //Gives cards from battle to victor
            /**player.recycleCard(card);        //Procedural card return
            player.recycleCard(otherCard);
            for (char c:pool) {
                if(c == '\u0000') {
                    break;
                }
                player.recycleCard(c);          
            }**/

            int countNum = 0;                   //Random card return
            for (int i=0;i<=51;i++) {
                if(pool[i] == '\u0000') {
                    break;
                }
                countNum += 1;
            }
            char[] cardList = new char[countNum+2];
            cardList[0] = card;
            cardList[1] = otherCard;
            for (int i=0;i<countNum;i++) {
                if (pool[i] == '\u0000') {
                    break;
                }
                cardList[i+2] = pool[i];
            }
            countNum += 2;
            for (int i=cardList.length;i>0;i--) {
                int randNum = (int)(Math.random()*i);
                player.recycleCard(cardList[randNum]);
                for (int j = randNum;j<=cardList.length-2;j++) {
                    cardList[j] = cardList[j+1];
                }
            }
        }//awardCards
        public static void battle(Player firstPlayer, Player secondPlayer) {   //Compare cards, give them to victor
            char card1 = firstPlayer.drawCard();
            char card2 = secondPlayer.drawCard();
            char[] pool = new char[52];
            int poolLength = 0;
            char drawnCard1='\u0000';
            char drawnCard2='\u0000';
            while (card1 == card2) {           //Deals with a tie AKA "War"
                System.out.println("War");
                pool[poolLength] = card1;
                pool[poolLength+1] = card2;
                poolLength += 2;
                pool[poolLength] = firstPlayer.drawCard();
                pool[poolLength+1] = secondPlayer.drawCard();
                poolLength += 2;
                drawnCard1 = firstPlayer.drawCard();
                drawnCard2 = secondPlayer.drawCard();
                
                if (pool[poolLength-1] == '\u0000' || drawnCard1 == '\u0000') { //if player1 can't draw card
                    pool[poolLength] = drawnCard1;
                    pool[poolLength+1] = drawnCard2;
                    poolLength += 2;
                    for (int i=0;i<poolLength;i++) {
                        if (pool[i] != '\u0000') {
                            secondPlayer.recycleCard(pool[i]);
                        }
                    }
                    return;
                }
                if (pool[poolLength-1] == '\u0000' || drawnCard2 == '\u0000') {
                    pool[poolLength] = drawnCard1;
                    pool[poolLength+1] = drawnCard2;
                    poolLength += 2;
                    for (int i=0;i<poolLength;i++) {
                        if (pool[i] != '\u0000') {
                            firstPlayer.recycleCard(pool[i]);
                        }
                    }
                    return;
                }
                card1 = drawnCard1;
                card2 = drawnCard2;
            }

            if (compareCards(card1,card2) == card1) {
                awardCards(firstPlayer,card1,card2,pool);   //Gives cards to player 1
            }else {
                awardCards(secondPlayer,card1,card2,pool);  //Gives cards to player 2
            }
            showScore(firstPlayer,secondPlayer);
        }//battle
        public static void showScore(Player playerOne,Player playerTwo) {         //Displays each player's deck
            for (char c:playerOne.getPlayerDeck()) {
                if (c != '\u0000') {
                    System.out.print(c);
                }
            }
            System.out.print("          ");
            for (char c:playerTwo.getPlayerDeck()) {
                if (c != '\u0000') {
                    System.out.print(c);
                }
            }
            System.out.println("");
        }//showScore
        public static String checkWinner(Player firstPlayer, Player secondPlayer) {    //Checks if player has 52 cards
            if (!(firstPlayer.getPlayerDeck()[51] == '\u0000')) {
                return "Player 1";
            }else if (!(secondPlayer.getPlayerDeck()[51] == '\u0000')) {
                return "Player 2";
            }
            return "";
        }//checkWinner
        
    }//Game
    public static void main(String[] args) {
        Game.genDeck();
        Game.dealCards();
        String check = "";
        Player player1 = new Player(Game.getDeck(1));
        Player player2 = new Player(Game.getDeck(2));
        int count = 0;
        while (check == ""){          //loops through battles, checks winner
            count += 1;
            Game.battle(player1,player2);
            check = Game.checkWinner(player1,player2);;
        }
        System.out.println(""+check+" wins!");
        System.out.println(count+" battles.");
    }//main
}//WarSim
