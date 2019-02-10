Feature: ebay items check-out 
Scenario: Add 2 items to the cart 
Given Open "https://www.ebay.com.au/" website on "firefox" browser
When Enter "the alchemist" by "paulo coelho" and "how to win friends" by "dale carnegie" items to purchase			
Then Verify "2" Items added to the cart
Then Go to checkout