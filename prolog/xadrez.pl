mostrartab(T):-
  findall((P,Cl,(L,C)),p_atual(P,Cl,(L,C)),T).

/*carrega a base de fatos que será o tabuleiro durante a partida*/
iniciarJava:- retractall(p_atual(_,_,(_,_))),retractall(cemiterio(_,_)),
assert(p_atual('T','B',(1,1))),assert(p_atual('H','B',(1,2))),
assert(p_atual('B','B',(1,3))),assert(p_atual('Q','B',(1,4))),
assert(p_atual('K','B',(1,5))),assert(p_atual('B','B',(1,6))),
assert(p_atual('H','B',(1,7))),assert(p_atual('T','B',(1,8))),
assert(p_atual('T','W',(8,1))),assert(p_atual('H','W',(8,2))),
assert(p_atual('B','W',(8,3))),assert(p_atual('Q','W',(8,4))),
assert(p_atual('K','W',(8,5))),assert(p_atual('B','W',(8,6))),
assert(p_atual('H','W',(8,7))),assert(p_atual('T','W',(8,8))),
assert(p_atual('P','B',(2,1))),assert(p_atual('P','B',(2,2))),
assert(p_atual('P','B',(2,3))),assert(p_atual('P','B',(2,4))),
assert(p_atual('P','B',(2,5))),assert(p_atual('P','B',(2,6))),
assert(p_atual('P','B',(2,7))),assert(p_atual('P','B',(2,7))),
assert(p_atual('P','W',(7,1))),assert(p_atual('P','W',(7,2))),
assert(p_atual('P','W',(7,3))),assert(p_atual('P','W',(7,4))),
assert(p_atual('P','W',(7,5))),assert(p_atual('P','W',(7,6))),
assert(p_atual('P','W',(7,7))),assert(p_atual('P','W',(7,8))),
assert(p_atual(' ',' ',(_,_))).
