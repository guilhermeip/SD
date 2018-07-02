import React, { Component } from "react";
import { Alert, Table, Button, Col } from "reactstrap";
import Axios from "axios";
import Modal from "./modal";

class TableRest extends Component {
  state = { getAgenda: [] };

  removerElemento = (id) => {
    Axios.delete("http://localhost:3003/api/agenda/"+id).then(consulta =>{
      <Alert color="sucess">Consulta removida com sucesso</Alert>
    })

    for(let i=0; i < this.state.getAgenda.length; i++){
      if(this.state.getAgenda[i].idAgenda === id){
        this.state.getAgenda.splice(i, 1);
        this.setState({ getAgenda: this.state.getAgenda })
        break;
      }
    }
  };
  retornarLista = () => {
    return this.state.getAgenda.map(e => {
      return (
        <tr key={e.idAgenda}>
          <td>{e.nomePaciente}</td>
          <td>{e.telefone}</td>
          <td>{e.data}</td>
          <td>
            <Button onClick={()=>this.removerElemento(e.idAgenda)} color="danger">Remover</Button>
          </td>
        </tr>
      );
    });
  };

  componentDidMount() {
    Axios.get("http://localhost:3003/api/agenda").then(result =>
      this.setState({ getAgenda: result.data.registros })
    );
  }

  render() {
    return (
      <Col sm="12" md={{ size: 8, offset: 2 }}>
        <Modal />
        <br />
        <br />

        <Table hover>
          <thead>
            <tr>
              <th>Paciente</th>
              <th>Telefone</th>
              <th>Data</th>
              <th> - </th>
            </tr>
          </thead>
          <tbody>{this.retornarLista()}</tbody>
        </Table>
      </Col>
    );
  }
}

export default TableRest;
