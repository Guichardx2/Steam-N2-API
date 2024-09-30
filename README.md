# Projeto para Avaliação 2 de Backend #

#### Este projeto tem como objetivo retornar dados de contas da plataforma de jogos STEAM utilizando uma chave de acesso da API e os parâmetros que serão descritos logo abaixo.


## Como consigo minha API Key? ##
Primeiramente, eu coloquei minha API Key num arquivo .env apenas para manter as boas práticas, porém, não coloquei o .env no gitignore, justamente para fins de teste e uso externo. 

Entretanto, caso queira sua chave para usar:
 
 🔗 [Acesso API KEY Steam](https://steamcommunity.com/dev)

 Faça seu cadastro e apenas substitua sua chave no arquivo .env! Não esqueça de ter o Steam Guard ativo para isso.

 #### OBS: Alguns endpoints colocam limite de requisições por chave, e um dos meus já se esgotou nos testes. Por isso, recomendo adquirir a sua chave.

## Como testo o projeto?
Clone este repositório em sua máquina e utilize algum software para gerenciar as requisições. Sugestões:

1. [Insomnia](https://insomnia.rest/download) (Usado por mim)
2. [Postman](https://www.postman.com/downloads/)

## Descrição e Detalhes ##
Após feito o que foi citado acima, vamos ao projeto em si.

 O projeto conta com **5 rotas** que devem ser acessadas pela rota "geral" **/steam**. Todas as requisições devem ser do tipo POST, exceto a /sobre, esta é do tipo GET.

OBS: Todas as rotas do tipo POST devem conter parâmetros passados no body através de um JSON. Os parâmetros são steamId ou gameId, conforme será explicado.
<hr>

## ROTAS ##
1. /userInfo

   Esta rota retorna as informações do perfil do usuário setadas por mim. Para isso, deve ser passado um JSON no body da requisição contendo 
        
        {
            "steamId": "<qualquer id>"
        }
        
  ### Os dados de resposta devem ser: #### 
  
  -SteamId
  
  -Nome de usuário
  
  -Nome verdadeiro (Se houver)
  
  -Tempo de conta
  
  -Local

</hr>

### Imagem de exemplo: ###
   
  ![image](https://github.com/user-attachments/assets/f8f5d4f3-4828-4a97-931e-cbb177851f92)

<hr>

2. /userFamilyInfo

    Esta rota retorna a lista de amigos do usuário passo pela steamId.

       {
           "steamId": "<qualquer id>"
       }
   
   ### Os dados de resposta devem ser: #### 
  
  -Id Amigo Steam
  
  -Tempo amigos
  
  -Tipo de relação

  ### Imagem de exemplo: ###
  
  ![image](https://github.com/user-attachments/assets/2c6c23ad-118e-4c82-aa71-a20870063410)

  <hr>

  3. /gameInfoAchievementsGlobal

  Esta rota retorna todas as conquistas de um jogo e suas respectivas porcentagens de players conquistadores. O parâmetro passado deve ser o gameId
    

       {
           "gameId": "<qualquer id>"
       }
   
   ### Os dados de resposta devem ser: #### 
  
  -Nome da conquista : porcentagem de jogadores que conquistaram

  ### Imagem de exemplo: ###
  
  ![image](https://github.com/user-attachments/assets/e280fdfc-2531-4ab3-98e0-cf66f514c130)

  <hr>
  
  4. /gamerStatsAchievements (Rota esgotada por mim)

  Esta rota retorna todas as conquistas do perfil do usuário. O parâmetro passado deve ser a steamId
    

       {
           "steamId": "<qualquer id>"
       }
   
   ### Os dados de resposta devem ser: #### 
   
  -Conta privada (Sem dados a ser exibidos se a conta for privada)
  -Nome da conquista
  -Conquista desbloqueada
  -Tempo de desbloqueio

  ### Imagem de exemplo: ###

  Nesta imagem, tratei o erro para melhor entendimento.
  
 ![image](https://github.com/user-attachments/assets/ba0b0ef1-9b55-4c7a-bc84-dd7716d5c4db)

<hr>

5. /sobre

   Rota do tipo GET que retorna o nome do desenvolvedor e o nome do projeto para avaliação do professor.

   ![image](https://github.com/user-attachments/assets/ea0cad52-b3ad-48b5-b89d-1b9601c388fd)



