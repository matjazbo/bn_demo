insert into movie(id, title, year, description) values ('tt0083658', 'Blade Runner', 1982, 'A blade runner must pursue and terminate four replicants who stole a ship in space, and have returned to Earth to find their creator.')
insert into movie(id, title, year, description) values ('tt1856101', 'Blade Runner 2049', 2017, 'A young blade runner`s discovery of a long-buried secret leads him to track down former blade runner Rick Deckard, who`s been missing for thirty years.')
insert into movie(id, title, year, description) values ('tt0163651', 'American Pie', 1999, 'Four teenage boys enter a pact to lose their virginity by prom night.')

insert into actor(id, firstName, lastName, dateOfBirth) values (10000, 'Harrison', 'Ford', '1942-07-13')
insert into actor(id, firstName, lastName, dateOfBirth) values (10001, 'Ryan', 'Gosling', '1980-11-12')
insert into actor(id, firstName, lastName, dateOfBirth) values (10002, 'Seann', 'William Scott', '1976-10-03')

insert into movies_actors (movie_id, actor_id) values ('tt0083658', 10000)
insert into movies_actors (movie_id, actor_id) values ('tt1856101', 10000)
insert into movies_actors (movie_id, actor_id) values ('tt1856101', 10001)
insert into movies_actors (movie_id, actor_id) values ('tt0163651', 10002)
