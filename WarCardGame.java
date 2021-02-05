/** This program generates a randomly shuffled
deck of cards, and plays a game of War to its 
conclusion, while counting the number of battles.*/

public class WarCardGame {
    private static class Player {       //Stores a player's deck, adds/removes cards
        char[] playerdeck = new char[52];       //Cards in beginning of array, extra space filled by null
        int deckLength = 26;
        public Player (char[] inputdeck) {      //Constructor
            for (int i=0;i<=25;i++) {
                playerdeck[i] = inputdeck[i];
                playerdeck[26+i] = '\u0000';
            }
        }//Constructor
        public char drawCard() {            //Removes card from beginning of deck
            char drawnCard = playerdeck[0]; //and moves each other card up one spot
            if (drawnCard == '\u0000') {
                return drawnCard;
            }
            deckLength -= 1;
            for (int i=1;i<=51;i++) {
                if (playerdeck[i] == '\u0000') {
                    playerdeck[i-1] = '\u0000';
                    break;
                }
                playerdeck[i-1] = playerdeck[i];
            }
            return drawnCard;
        }//drawCard
        public void recycleCard(char recycledCard) {  //Replaces first null slot with card
            if(recycledCard != '\u0000') {
                playerdeck[deckLength] = recycledCard;
                deckLength += 1;
            }
        }//recycleCard
    }//Player class
    public static class Game {                  //Contains methods to deal the cards, hold Battles and award cards to winners
        char[] cards = {'2','3','4','5','6','7','8','9','T','J','Q','K','A'};
        char[] deck = new char[52];
        char[][] dealtdecks = new char[2][26];
        public void genDeck() {         //Creates deck of 52 cards in random order
            for (char c:cards) {
                for (int i=0;i<=3;i++) {
                    while (true) {
                        int randnum = (int)(Math.random()*52);
                        if(deck[randnum] == '\u0000') {
                            deck[randnum] = c;
                            break;
                        }
                    }
                }      
            }   
        }//genDeck
        public void dealCards() {       //Distributes cards to players, alternating
            for (int i=0;i<=51;i++) {
                if (i%2 == 0) {
                    dealtdecks[i%2][i/2] = deck[i];
                }else {
                    dealtdecks[i%2][i/2] = deck[i];
                }
            }
        }//dealCards
        public char[] getDeck(int decknum) {     //Retrieves one of two halves of the deck
            return dealtdecks[decknum-1];
        }//getDeck
        public char compareCards(char firstCard,char secondCard) {      //Compares 2 card values
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
        public void awardCards(Player player,char card,char otherCard,char[] pool) {//, char[] secondPool) {    //Gives cards from battle to victor
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
                int randnum = (int)(Math.random()*i);
                //System.out.println(cardList[randnum]);
                player.recycleCard(cardList[randnum]);
                for (int j = randnum;j<=cardList.length-2;j++) {
                    cardList[j] = cardList[j+1];
                }
            }
        }//awardCards
        public void battle(Player firstPlayer, Player secondPlayer) {   //Compare cards, give them to victor
            char card1 = firstPlayer.drawCard();
            char card2 = secondPlayer.drawCard();
            if (card1 == '\u0000' || card2 == '\u0000') {
                return;
            }
            char[] pool1 = new char[52];
            int pool1Length = 0;
            char[] pool2 = new char[52];
            char[] pool = new char[52];
            int poolLength = 0;
            int pool2Length = 0;
            char drawnCard3='\u0000';
            char drawnCard4='\u0000';
            while (card1 == card2) {           //Deals with a tie AKA "War"
                System.out.println("War");
                pool[poolLength] = card1;
                pool[poolLength+1] = card2;
                poolLength += 2;
                pool[poolLength] = firstPlayer.drawCard();
                pool[poolLength+1] = secondPlayer.drawCard();
                poolLength += 2;
                drawnCard3 = firstPlayer.drawCard();
                drawnCard4 = secondPlayer.drawCard();
                
                if (pool[poolLength-1] == '\u0000' || drawnCard3 == '\u0000') { //if player1 can't draw card
                    pool[poolLength] = drawnCard3;
                    pool[poolLength+1] = drawnCard4;
                    poolLength += 2;
                    for (int i=0;i<poolLength;i++) {
                        if (pool[i] != '\u0000') {
                            secondPlayer.recycleCard(pool[i]);
                        }
                    }
                    return;
                }
                if (pool[poolLength-1] == '\u0000' || drawnCard4 == '\u0000') {
                    pool[poolLength] = drawnCard3;
                    pool[poolLength+1] = drawnCard4;
                    poolLength += 2;
                    for (int i=0;i<poolLength;i++) {
                        if (pool[i] != '\u0000') {
                            firstPlayer.recycleCard(pool[i]);
                        }
                    }
                    return;
                }
                card1 = drawnCard3;
                card2 = drawnCard4;
               }

            if (compareCards(card1,card2) == card1) {
                awardCards(firstPlayer,card1,card2,pool);   //Gives cards to player 1
            }else {
                awardCards(secondPlayer,card1,card2,pool);  //Gives cards to player 2
            }
        showScore(firstPlayer,secondPlayer);
        }//battle
        public void showScore(Player playerOne,Player playerTwo) {         //Displays each player's deck
            for (char c:playerOne.playerdeck) {
                if (c != '\u0000') {
                    System.out.print(c);
                }
            }
            System.out.print("          ");
            for (char c:playerTwo.playerdeck) {
                if (c != '\u0000') {
                    System.out.print(c);
                }
            }
            System.out.println("");
        }
        public String checkWinner(Player firstPlayer, Player secondPlayer) {    //Checks if player has 52 cards
            if (!(firstPlayer.playerdeck[51] == '\u0000')) {
                return "Player 1";
            }else if (!(secondPlayer.playerdeck[51] == '\u0000')) {
                return "Player 2";
            }
            return "";
        }//checkWinner
        
    }
    public static void main(String[] args) {
        Game game1 = new Game();
        game1.genDeck();
        game1.dealCards();
        String check = "";

        Player player1 = new Player(game1.getDeck(1));
        Player player2 = new Player(game1.getDeck(2));

        int count = 0;
        while (check == ""){          //loops through battles, checks winner
            count += 1;
            game1.battle(player1,player2);
            check = game1.checkWinner(player1,player2);
            if (check != "") {
                System.out.println(""+check+" wins!");
            }
        }
        System.out.println(count+" battles.");
        
    }//main
}//WarSim