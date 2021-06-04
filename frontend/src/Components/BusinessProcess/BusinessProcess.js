import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";
import toast from "react-hot-toast";
import { Modal } from "react-bootstrap";
import Select from "react-select";

export default class BusinessProcess extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      capabilities: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      capabilityId: "",
      businessProcesses: [],
      showItemModal: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  async componentDidMount() {
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/capability/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        response.data.forEach((capability) => {
          capability.label = capability.capabilityName;
          capability.value = capability.capabilityId;
        });
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could not Load Capabilities");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/businessprocess/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        this.setState({ businessProcesses: response.data });
      })
      .catch((error) => {
        toast.error("Could not Load Business Processes");
      });
  }

  edit(businessProcessId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/businessprocess/${businessProcessId}`
    );
  }
  fetchDeleteBusinessProcesses = async (businessProcessId) => {
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    await axios
      .delete(
        `${process.env.REACT_APP_API_URL}/businessprocess/${businessProcessId}`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) =>
        toast.success("Successfully Deleted Business Process")
      )
      .catch((error) => toast.error("Could not Delete Business Process"));
    //REFRESH BUSINESS PROCESSES
    await axios
      .get(`${process.env.REACT_APP_API_URL}/businessprocess/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => {
        this.setState({ businessprocesses: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Business Processes");
      });
  };

  handleSubmit = (businessProcessId) => async (e) => {
    e.preventDefault();
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    const formData = new FormData();
    formData.append("businessProcessId", businessProcessId);
    formData.append("capabilityId", this.state.capabilityId);
    await axios
      .put(
        `${process.env.REACT_APP_API_URL}/capability/link-businessprocess/`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then(toast.success("Business Process Successfully Linked"))
      .catch((error) => toast.error("Could not Link Business Process"));
  };

  delete = async (businessProcessId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this Business Process?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteBusinessProcesses(businessProcessId);
              }}
            >
              Yes!
            </button>
            <button
              className='btn btn-secondary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => toast.dismiss(t.id)}
            >
              No!
            </button>
          </div>
        </span>
      ),
      { duration: 50000 }
    );
  };
  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  render() {
    return (
      <div className='container'>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className='breadcrumb-item'>Business Processes</li>
          </ol>
        </nav>
        <MaterialTable
          title='Business Processes'
          actions={[
            {
              icon: "add",
              tooltip: "Add Business Process",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/businessprocess/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "businessProcessId" },
            { title: "Name", field: "businessProcessName" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(
                        this,
                        rowData.businessProcessId
                      )}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.businessProcessId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={() => this.handleModal()}
                      className='bi bi-chat-square'
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.businessProcesses}
          detailPanel={(rowData) => {
            return (
              <div>
                <Modal
                  show={this.state.showModal}
                  onHide={() => this.handleModal()}
                >
                  <Modal.Header closeButton>
                    <Modal.Title>Add Capability</Modal.Title>
                  </Modal.Header>
                  <Modal.Body>
                    <form
                      onSubmit={this.handleSubmit(rowData.businessProcessId)}
                    >
                      <label htmlFor='capabilityId'>Capability</label>
                      <Select
                        options={this.state.capabilities}
                        noOptionsMessage={() => "No Capabilities"}
                        onChange={(capability) => {
                          if (capability) {
                            this.setState({
                              capabilityId: capability.capabilityId,
                            });
                          } else {
                            this.setState({ capabilityId: 0 });
                          }
                        }}
                        placeholder='Optional'
                      />
                      <br></br>
                      <button className='btn btn-primary' type='sumbit'>
                        SUBMIT
                      </button>
                    </form>
                  </Modal.Body>
                  <Modal.Footer>
                    <button
                      type='button'
                      className='btn btn-secondary'
                      onClick={() => this.handleModal()}
                    >
                      Close Modal
                    </button>
                  </Modal.Footer>
                </Modal>
              </div>
            );
          }}
          onRowClick={(event, rowData, togglePanel) => {
            togglePanel();
          }}
        />
      </div>
    );
  }
}
