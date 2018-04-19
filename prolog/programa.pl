 % gerar_regioes(-3,4,1,1,1,2,2,5,5).

gerar_regioes(Xmin,Xmax,Intervalo,F1estrela,F2estrela,F1min,F2min,F1max,F2max):-
	inicializar(Xmin,F1estrela,F2estrela,F1min,F2min,F1max,F2max,[A1,A2,Teta1,Teta2]),
	regioes(Xmax,Intervalo,[A1,A2,Teta1,Teta2]),
	resultados.

inicializar(Xmin,F1estrela,F2estrela,F1min,F2min,F1max,F2max,[A1,A2,Teta1,Teta2]):-
	asserta(x(Xmin)),
	asserta(primeira),
	A1 is (F2min-F2estrela)/(F1max-F1estrela),write('A1 ==> '),write(A1),nl,
	A2 is (F2max-F2estrela)/(F1min-F1estrela),write('A2 ==> '),write(A2),nl,
	B is F2estrela,write('B ==> '),write(B),nl,
	C is F1estrela,write('C ==> '),write(C),nl,
	Teta1 is B - A1*C,write('Teta1 ==> '),write(Teta1),nl,
	Teta2 is B - A2*C,write('Teta2 ==> '),write(Teta2),nl.


/*****************************************/
regioes(Xmax,Intervalo,[A1,A2,Teta1,Teta2]):-
	repeat,
	  gerar(Intervalo),
	  avaliar([A1,A2,Teta1,Teta2]),
	  testar(Xmax),!.

/*****************************************/
% gerar(-3,0.1).

gerar(_):-
	retract(primeira),!.
gerar(Intervalo):-
	retract(x(Xanterior)),
	X is Xanterior + Intervalo,
	asserta(x(X)).

/*****************************************/
% avaliar(1,1,2,2,5,5).

avaliar([A1,A2,Teta1,Teta2]):-
	x(X),
	F1 is X*X + 1,
	F2 is (X-1)*(X-1) + 1,
	W11 is -A1,
	W12 is 1,
	W21 is -A2,
	W22 is 1,
	neuronio([F1,F2],[W11,W12],Teta1,YTico),
	neuronio([F1,F2],[W21,W22],Teta2,YTeco),
	regiao_interesse(X,F1,F2,YTico,YTeco).

/*****************************************/
testar(Xmax):-
	x(X),
	X>Xmax.

/*****************************************/
resultados:-
	repeat,
	  retract(saida(X,F1,F2,Y1,Y2,Regiao)),
	  write(saida(X,F1,F2,Y1,Y2,Regiao)),nl,
	  not(saida(_,_,_,_,_,_)),!,
	retract(x(_)).

/*****************************************/
% neuronio([1,1],[-1,-1],-1,Y).

neuronio(F,W,Teta,Y):-
	escalar(F,W,V),
	g(V,Teta,Y).

g(V,Teta,1):-
	V >= Teta,!.
g(_,_,0).

/*****************************************/


regiao_interesse(X,F1,F2,0,0):-
	!,assertz(saida(X,F1,F2,0,0,regiao1)).
regiao_interesse(X,F1,F2,1,0):-
	!,assertz(saida(X,F1,F2,1,0,regiaoCONE)).
regiao_interesse(X,F1,F2,1,1):-
	assertz(saida(X,F1,F2,1,1,regiao2)).

/*****************************************/
zipper([ ], [ ], [ ]):- !.
zipper([X1| Y1], [X2| Y2], [[X1, X2]| Y]):-
	zipper(Y1, Y2, Y).

ap(_, [ ], [ ]):- !.
ap(Op, [X | Y], [OpX | OpY]):-
	Predicado =.. [Op, X, OpX],
	call(Predicado),
	ap(Op, Y, OpY).

reduz(Op, [X, Y], R):-
	!, Predicado =.. [Op, [X, Y], R],
	call(Predicado).
reduz(Op, [X | Y], R):-
	reduz(Op, Y, RY),
	Predicado =.. [Op, [X, RY], R],
	call(Predicado).

escalar(L1, L2, E):-
		zipper(L1, L2, L),
		ap(produto, L, ListadeProdutos),
		reduz(soma, ListadeProdutos, E).

soma([X,Y],S):-
	S is X+Y.

produto([X,Y],P):-
	P is X*Y.



