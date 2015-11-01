# sandbox programs
Small experimental programs quickly hacked together. I wanted to have these projects available but I didn't think any were substantial enough for their own repository.

## cost_to_go
 - Simple api to calculate the cost for me to drive from point a to b
 - Built with ruby/sinatra

## file_sharing
 - Website built originally to allow for easy file sharing. Uses websockets to transport data.
 - Currently only saves files to the server.
 - Built with node.js socket.io

## roulette_bot
 - Java bot that plays online roulette. Built for a specific roulette game but can be modified to work with others.
 - The bot uses a strategy where it only bets on red or black and will double its bet on the same colour every loss. 
  - This strategy has two major flaws. Firstly if the online casino has a low maximum bet and a high minimum bet than the number of times you can double your bet is lower. If for example the minimum bet was $25 and the max bet was $500 the bot would only be able to double its bet 4 times where ideally the bot should be able to double its bet at least 7 times. The second flaw is that this strategy requires a significant amount of money available to bet since bets are increases exponentially.
  - Despite these flaws the bot has performed very successfully testing with free accounts and fake money. I'm not so sure I'd want to try this with real money though.

## three_js project
 - Experimenting with three_js. This app produces a 3d representation of the S&P500 for the last year.
 
![alt text](https://github.com/mitchvoll/sandbox/blob/master/threejs/stocks.png "Example")

## get_notes.py
 - Small python script to save class notes from a number of Queen's Computing course websites.
 - Uses beautiful soup to scrape sites for pdfs
 
