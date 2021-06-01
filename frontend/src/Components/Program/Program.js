import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";

export default class Program extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      programs: [],
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
      .get(`${process.env.REACT_APP_API_URL}/program/`)
      .then((response) => {
        this.setState({ programs: response.data });
      })
      .catch((error) => {
        console.log(error);
      });
  }

  edit(programId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/program/${programId}`
    );
  }
  //DELETE CAPABILITY AND REMOVE ALL CHILD CAPABILITIES FROM STATE
  delete = async (programId) => {
    if (window.confirm("Are you sure you want to delete this Program?")) {
      await axios
        .delete(`${process.env.REACT_APP_API_URL}/program/${programId}`)
        .catch((error) => console.error(error));
      //REFRESH CAPABILITIES
      await axios
        .get(`${process.env.REACT_APP_API_URL}/program/`)
        .then((response) => {
          this.setState({ programs: [] });
          this.setState({ programs: response.data });
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
            <li className='breadcrumb-item'>Programs</li>
          </ol>
        </nav>
        <MaterialTable
          title='Programs'
          actions={[
            {
              icon: "add",
              tooltip: "Add Program",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/program/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "programId" },
            { title: "Name", field: "programName" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.programId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.programId)}
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
          data={this.state.programs}
        />
      </div>
    );
  }
}
