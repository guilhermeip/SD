const Sequelize = require('sequelize');
const { database } = require('../config');

const sequelize = new Sequelize('mysql://' + database.user + ':' + database.password + '@localhost:3306/' + database.databaseName, {
	define: {
		timestamps: false,
		freezeTableName: true,
	},
});

sequelize.authenticate()
	.then(() => {
		console.log('Conectado no BD com sucesso.');
	}).catch(ex => {
		console.error('Erro ao se conectar no BD:', ex);
	});

const Agenda = sequelize.define('Agenda', {
	idAgenda: {
		type: Sequelize.INTEGER,
		primaryKey: true,
		autoIncrement: true,
	},
	nomePaciente: {
		type: Sequelize.STRING(50),
		allowNull: false,
	},
	telefone: {
		type: Sequelize.STRING(20),
		allowNull: true,
	},
	data: {
		type: Sequelize.STRING(15),
		allowNull: false,
	},
});

//FIM DAS RELAÇÕES DAS TABELAS **********


// exporta o objeto criado para poder utilizar em outros lugares
module.exports = {
	Agenda,
	sequelize,
};