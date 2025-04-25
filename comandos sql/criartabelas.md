CREATE TABLE usuario (
id_usuario       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
nome             VARCHAR(100) NOT NULL,
sobrenome        VARCHAR(100) NOT NULL,
email            VARCHAR(255) NOT NULL UNIQUE,
telefone         VARCHAR(20)  NOT NULL,
senha            VARCHAR(255) NOT NULL,
id_tipo_usuario  INT          NOT NULL
);

CREATE TABLE artista (
id_artista INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
nome       VARCHAR(150) NOT NULL
);

CREATE TABLE playlist (
id_playlist  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
nome         VARCHAR(120) NOT NULL,
salvamentos  INT          NOT NULL,
is_public    BOOLEAN      NOT NULL
);

CREATE TABLE genero (
id_genero INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
nome      VARCHAR(60) NOT NULL
);

CREATE TABLE musica (
id_musica     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
nome          VARCHAR(150) NOT NULL,
duracao_ms    INT          NOT NULL,
conteudo      BYTEA        NOT NULL,
id_genero     INT          NOT NULL REFERENCES genero(id_genero)
);

CREATE TABLE playlist_usuario (
id_usuario  INT NOT NULL,
id_playlist INT NOT NULL,
PRIMARY KEY (id_usuario, id_playlist),
FOREIGN KEY (id_usuario)  REFERENCES usuario (id_usuario)  ON DELETE CASCADE,
FOREIGN KEY (id_playlist) REFERENCES playlist(id_playlist) ON DELETE CASCADE
);

CREATE TABLE playlist_musica (
id_musica   INT NOT NULL,
id_playlist INT NOT NULL,
PRIMARY KEY (id_musica, id_playlist),
FOREIGN KEY (id_musica)   REFERENCES musica  (id_musica)   ON DELETE CASCADE,
FOREIGN KEY (id_playlist) REFERENCES playlist(id_playlist) ON DELETE CASCADE
);

CREATE TABLE artista_musica (
id_musica  INT NOT NULL,
id_artista INT NOT NULL,
PRIMARY KEY (id_musica, id_artista),
FOREIGN KEY (id_musica)  REFERENCES musica  (id_musica)  ON DELETE CASCADE,
FOREIGN KEY (id_artista) REFERENCES artista (id_artista) ON DELETE CASCADE
);

CREATE TABLE usuario_historico_buscas (
busca       VARCHAR(255) NOT NULL,
data_busca  DATE         NOT NULL,
id_usuario  INT          NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE usuario_historico_musicas (
data_tocada DATE NOT NULL,
id_usuario  INT  NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
id_musica   INT  NOT NULL REFERENCES musica (id_musica)
);

CREATE TABLE usuario_fila (
id_usuario INT NOT NULL,
id_musica  INT NOT NULL,
PRIMARY KEY (id_usuario, id_musica),
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
FOREIGN KEY (id_musica)  REFERENCES musica (id_musica)
);

CREATE TABLE avaliacao_usuario (
gostou     BOOLEAN NOT NULL,
id_usuario INT     NOT NULL,
id_musica  INT     NOT NULL,
PRIMARY KEY (id_usuario, id_musica),
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
FOREIGN KEY (id_musica)  REFERENCES musica (id_musica)
);
