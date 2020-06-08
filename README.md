# g2a.scrapper

I use an external sh script to make the query to the website. I do this because I developed the scraper in bash. Furthermore, I am not completely sure if scrapping frameworks in kotlin can mimic a browser request as curl can do.
That is to be developed. I use the Skrape.it framework to do the work. G2A blocks connections where they detect non browser connections, which makes it harder to make http page request using the some scrapping frameworks.
