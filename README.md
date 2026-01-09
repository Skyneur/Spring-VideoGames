
# Spring VideoGames - Endpoints (bref)

Minimal : démarrage et quelques endpoints essentiels.

- Démarrer :

```bash
./mvnw spring-boot:run
```

- Authentification :
  - `POST /api/authenticate` (Body JSON : `{"username":"admin","password":"password"}`)
  - Réponse : `{"token":"..."}`

- Jeux (VideoGame) :
  - `GET /api/videogames`
  - `GET /api/videogames/{id}`
  - `POST /api/videogames` (ROLE_ADMIN, header `Authorization: Bearer <TOKEN>`)

- Avis (Review) :
  - `GET /api/reviews`
  - `POST /api/reviews?videoGameId={id}` (ROLE_ADMIN)

- Catégories (Category) :
  - `GET /api/categories`
  - `POST /api/categories` (ROLE_ADMIN)

Exemples rapides (curl) :

1) Obtenir un token :

```bash
curl -s -X POST http://localhost:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'
```

2) Lister les jeux :

```bash
curl http://localhost:8080/api/videogames
```

3) Créer un jeu (avec token) :

```bash
TOKEN=<votre_token>
curl -i -X POST http://localhost:8080/api/videogames \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Mon Jeu","description":"Desc","releaseDate":"2025-12-01"}'
```

Remarque : l'application utilise H2 en mémoire par défaut pour les tests locaux.
