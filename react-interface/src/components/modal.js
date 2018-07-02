import React from "react";
import {
  Label,
  Input,
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter
} from "reactstrap";
import Axios from "axios";

class ModalAddTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modal: false
    };

    this.toggle = this.toggle.bind(this);
  }

  toggle() {
    this.setState({
      modal: !this.state.modal
    });
  }
  adicionarBanco = () => {
    Axios.post("http://localhost:3003/api/agenda", {
      nomePaciente: this.state.nomePaciente,
      telefone: this.state.telefone,
      data: this.state.data
    });
    this.toggle();
  };

  render() {
    return (
      <div>
        <Button className="float-right" color="info" onClick={this.toggle}>
          Adicionar
        </Button>
        <Modal
          isOpen={this.state.modal}
          toggle={this.toggle}
          className={this.props.className}
        >
          <ModalHeader toggle={this.toggle}>Adicionar Paciente</ModalHeader>
          <ModalBody>
            <Label>Nome Paciente</Label>
            <Input
              onChange={event =>
                this.setState({ nomePaciente: event.target.value })
              }
              placeholder="Ex: Rogério"
            />
            <br />
            <Label>Telefone</Label>
            <Input
              onChange={event =>
                this.setState({ telefone: event.target.value })
              }
              placeholder="(44)99755-6699"
            />
            <br />
            <Label>Data</Label>
            <Input
              onChange={event => this.setState({ data: event.target.value })}
              placeholder="Ex: 16/05/2012"
            />
          </ModalBody>
          <ModalFooter>
            <Button color="secondary" onClick={this.toggle}>
              Cancel
            </Button>
            <Button color="sucess" onClick={this.adicionarBanco}>
              Confirmar
            </Button>
          </ModalFooter>
        </Modal>
      </div>
    );
  }
}

export default ModalAddTable;
