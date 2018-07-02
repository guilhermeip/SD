const express = require("express");
const router = express.Router();
const { Agenda } = require("../models");

router.get("/agenda", function(req, res) {
  Agenda.findAll()
    .then(registros => {
      if (registros) {
        console.log(registros);  
        res.status(201).json({ sucesso: true, registros: registros });
      } else {
        res
          .status(404)
          .json({ sucesso: false, msg: "nenhum registro encontrado" });
      }
    })
    .catch(ex => {
      console.error(ex);
      res.status(400).json({ sucesso: false, msg: "algo saiu errado" });
    });
});

router.post("/agenda", function(req, res) {
  Agenda.create({
    nomePaciente: req.body.nomePaciente,
    telefone: req.body.telefone,
    data: req.body.data
  })
    .then(consulta => {
      res.status(201).json({ sucesso: true, consulta: consulta });
    })
    .catch(ex => {
      console.log(ex);
      res.status(400).json({
        sucesso: false,
        msg: "Erro ao adicionar consulta " + consulta
      });
    });
});

router.delete("/agenda/:id", function(req, res) {
  let agendaId = req.params.id;

  Agenda.destroy({
    where: {
      idAgenda: agendaId
    }
  }).then(consulta => {
    if (consulta) {
      res
        .status(201)
        .json({ sucesso: true, msg: "Consulta excluida com sucesso!" });
    } else {
      res.status(404).json({
        sucesso: false,
        msg: "Não foi encontrado a consulta"
      });
    }
  });
});

module.exports = router;
