import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";

export default class BusinessProcess extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      businessProcesses: [],
    };
  }

  async componentDidMount() {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        console.log(error);
        this.props.history.push("/error");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/businessProcesses/`)
      .then((response) => {
        this.setState({ businessProcesses: response.data });
      })
      .catch((error) => {
        console.log(error);
      });
  }

  edit(businessProcessesId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/businessProcess/${businessProcessesId}`
    );
  }
  //DELETE CAPABILITY AND REMOVE ALL CHILD CAPABILITIES FROM STATE
  delete = async (businessProcessesId) => {
    if (
      window.confirm("Are you sure you want to delete this business process?")
    ) {
      await axios
        .delete(
          `${process.env.REACT_APP_API_URL}/businessProcess/${businessProcessesId}`
        )
        .catch((error) => console.error(error));
      //REFRESH CAPABILITIES
      await axios
        .get(`${process.env.REACT_APP_API_URL}/businessProcess/`)
        .then((response) => {
          this.setState({ businessProcesses: [] });
          this.setState({ businessProcesses: response.data });
        })
        .catch((error) => {
          console.log(error);
          this.props.history.push("/error");
        });
    }
  };

  render() {
    return (
      <div>
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
                      onClick={() => this.handleItemModal()}
                      className='bi bi-app-indicator'
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.businessProcesses}
        />
      </div>
    );
  }
}
