# AED3 - TP4: Criptografia

#### Componentes do grupo: 
	Márlon

## Descrição do que foi realizado
  Foi implementada apenas uma classe nova (Cifra) na pasta aed3, utilizada para a implementação dos métodos de criptografia. 
  A classe apresenta uma técnica de criptografia por substituição e uma por transposição. Para a substituição o método escolhido foi a cifra de Vigenère e para a cifra de tranposição foi utilizada a cifra das colunas. Ambas aplicações de cifragem utilizaram a mesma palavra chave.
  Principais métodos:  

#### Para cifrar e decifrar os dados das entidades:
 cifrarSubstuição: Método que recebe o vetor de bytes a ser criptografado e o vetor da chave em bytes replicada até o temanho do vetor recebido. A técnica usada para cifrar foi a cifra de Vigenère.
 cifrarColunas: Método que recebe o vetor já cifrado na etapa anterior e a chave.
 Para decifrar foi feito as técnicas de forma inversa, primeiro a aplicação da cifra de colunas e depois a de substituição.

#### Experiência do grupo
  A lógica das técnicas utilizadas não é complicada. Primeiramente o grupo tentou fazer a cifra por colunas sem a utilização de matrizes, mas o processo acabou ficando grande e um pouco confuso, logo, a técnica foi feita com matrizes. Observação: não se sabe se a colocação da chave na própria classe está correta.

#### Perguntas objetivas sobre o projeto(chacklist):

 * Há uma função de cifragem em todas as classes de entidades, envolvendo pelo menos duas operações diferentes e usando uma chave criptográfica?
	Sim. Substituição (Vigenère) e tranposição (Colunas)
 * Uma das operações de cifragem é baseada na substituição e a outra na transposição?
	Sim.
 * O trabalho está funcionando corretamente?
	Sim, todos os dados de todas as entidades são cifradas e decifradas sem problema.
 * O trabalho está completo?
	Sim.
 * O trabalho é original e não a cópia de um trabalho de um colega?
	Sim.

