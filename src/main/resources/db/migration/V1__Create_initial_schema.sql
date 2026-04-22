CREATE TABLE t_usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE t_pontos_coleta (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    tipo_residuo_aceito VARCHAR(100) NOT NULL
);

CREATE TABLE t_agendamentos (
    id BIGSERIAL PRIMARY KEY,
    data_agendamento TIMESTAMP NOT NULL,
    tipo_residuo VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    usuario_id BIGINT,
    CONSTRAINT fk_agendamento_usuario FOREIGN KEY (usuario_id) REFERENCES t_usuarios(id)
);

CREATE TABLE t_notificacoes (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT,
    CONSTRAINT fk_notificacao_usuario FOREIGN KEY (usuario_id) REFERENCES t_usuarios(id)
);

CREATE TABLE t_containers (
    id BIGSERIAL PRIMARY KEY,
    localizacao VARCHAR(255) NOT NULL,
    tipo_residuo VARCHAR(100) NOT NULL,
    capacidade_maxima NUMERIC(10, 2) NOT NULL,
    nivel_atual NUMERIC(10, 2) DEFAULT 0,
    status_coleta VARCHAR(50) DEFAULT 'VAZIO'
);

INSERT INTO t_usuarios (nome, email, senha, role)
VALUES ('Admin', 'admin@rreciclagem.com', '$2a$10$8.X.yV.E/W8vQTfY8d.R.OxWn0Rrwg.sVOMd.NqHa/uT.T6k.L.zO', 'ADMIN');

INSERT INTO t_usuarios (nome, email, senha, role)
VALUES ('Usuario Comum', 'user@rreciclagem.com', '$2a$10$A5.X7.xV/kY/1iVl3v..pORy4.3sJ/e1hF/8K2s.Q/O/P.T9.L.zW', 'USER');

INSERT INTO t_pontos_coleta (nome, endereco, tipo_residuo_aceito)
VALUES ('Ecoponto Vila Mariana', 'Rua Exemplo, 123', 'PLASTICO');

INSERT INTO t_pontos_coleta (nome, endereco, tipo_residuo_aceito)
VALUES ('Coop Recicla Tatuape', 'Av. Teste, 456', 'PAPEL');

INSERT INTO t_pontos_coleta (nome, endereco, tipo_residuo_aceito)
VALUES ('Ponto Verde Pinheiros', 'Rua Ficticia, 789', 'VIDRO');
