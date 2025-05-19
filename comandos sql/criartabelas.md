DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE playlist (
id_playlist SERIAL PRIMARY KEY,
nome VARCHAR(120) NOT NULL,
data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE genero (
id_genero SERIAL PRIMARY KEY,
nome VARCHAR(60) NOT NULL UNIQUE,
data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE musica (
id_musica SERIAL PRIMARY KEY,
nome VARCHAR(150) NOT NULL,
duracao_ms INT NOT NULL,
audio BYTEA,
data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
id_genero INT NOT NULL REFERENCES genero(id_genero)
);

CREATE TABLE usuario (
id_usuario SERIAL PRIMARY KEY,
nome VARCHAR(100) NOT NULL,
sobrenome VARCHAR(100) NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE,
telefone VARCHAR(20) NOT NULL,
senha VARCHAR(255) NOT NULL,
data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE artista (
id_artista INT PRIMARY KEY NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
nome_artistico VARCHAR(150)
);

CREATE TABLE administrador (
id_administrador INT PRIMARY KEY NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
ultimo_login TIMESTAMP
);

CREATE TABLE playlist_usuario (
id_usuario INT NOT NULL,
id_playlist INT NOT NULL,
PRIMARY KEY (id_usuario, id_playlist),
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
FOREIGN KEY (id_playlist) REFERENCES playlist(id_playlist) ON DELETE CASCADE
);

CREATE TABLE playlist_musica (
id_musica_playlist SERIAL PRIMARY KEY,
id_musica INT NOT NULL,
id_playlist INT NOT NULL,
posicao INT NOT NULL,
FOREIGN KEY (id_musica) REFERENCES musica(id_musica) ON DELETE CASCADE,
FOREIGN KEY (id_playlist) REFERENCES playlist(id_playlist) ON DELETE CASCADE,
UNIQUE (id_playlist, posicao)
);

CREATE TABLE artista_musica (
id_musica INT NOT NULL,
id_artista INT NOT NULL,
PRIMARY KEY (id_musica, id_artista),
FOREIGN KEY (id_musica) REFERENCES musica(id_musica) ON DELETE CASCADE,
FOREIGN KEY (id_artista) REFERENCES artista(id_artista) ON DELETE CASCADE
);

CREATE TABLE usuario_historico_buscas (
id_historico_busca SERIAL PRIMARY KEY,
busca VARCHAR(255) NOT NULL,
data_busca TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE usuario_historico_musicas (
id_historico_musica SERIAL PRIMARY KEY,
data_tocada TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
id_musica INT REFERENCES musica(id_musica) ON DELETE SET NULL
);

CREATE TABLE usuario_fila (
id_musica_fila SERIAL PRIMARY KEY,
posicao INT NOT NULL,
id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
id_musica INT REFERENCES musica(id_musica) ON DELETE SET NULL,
UNIQUE (id_usuario, posicao)
);

	CREATE TABLE avaliacao_usuario (
    gostou BOOLEAN NOT NULL,
    data_avaliacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    id_musica INT NOT NULL REFERENCES musica(id_musica),
    PRIMARY KEY (id_usuario, id_musica)
);

CREATE TABLE usuario_playlists_salvas (
id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
id_playlist INT NOT NULL REFERENCES playlist(id_playlist) ON DELETE CASCADE,
data_salvamento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id_usuario, id_playlist)
);