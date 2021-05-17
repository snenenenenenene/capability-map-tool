import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import MaterialTable from 'material-table';
import './GeneralTable.css'
import axios from 'axios';

export default class Capability extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId: '',
            capabilities: [],
        };
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`)
        .then(response => this.setState({environmentId: response.data.environmentId}))
        .catch(error => {
            console.log(error)
            this.props.history.push('/error')
        })
        
        await axios.get(`${process.env.REACT_APP_API_URL}/capability/all-capabilities-by-environmentid/${this.state.environmentId}`)
        .then(response => {
                response.data[0].parentCapabilityId = null; // REMOVE WHEN PARENTID CAN BE NULL
                this.setState({capabilities: response.data});
            })
        .catch(error => {
            console.log(error)
            this.props.history.push('/error')
        })
    }

    edit(capabilityId){
        this.props.history.push(`/environment/${this.state.environmentName}/capability/${capabilityId}`)
    }
    //DELETE CAPABILITY AND REMOVE ALL CHILD CAPABILITIES FROM STATE
    delete = async(capabilityId) => {
        if (window.confirm('Are you sure you want to delete this capability?')){
            await axios.delete(`${process.env.REACT_APP_API_URL}/capability/${capabilityId}`)
            .catch(error => console.error(error))
            //REFRESH CAPABILITIES
            await axios.get(`${process.env.REACT_APP_API_URL}/capability/all-capabilities-by-environmentid/${this.state.environmentId}`)
            .then(response => {
                    response.data[0].parentCapabilityId = null; // REMOVE WHEN PARENTID CAN BE NULL
                    this.setState({capabilities: []})
                    this.setState({capabilities: response.data});
                })
            .catch(error => {
                console.log(error)
                this.props.history.push('/error')
            })
        }
    }

    render() {
        const environmentName = this.props.match.params.name;
        return(
            <div>
            <nav aria-label="breadcrumb">
            <ol className="breadcrumb">
                <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}>{environmentName}</Link></li>
                <li className="breadcrumb-item">Capabilities</li>
            </ol>
        </nav>
            <div className="jumbotron">
                <div>
                    <h1 className='display-4' style={{display: 'inline-block'}}>Capabilities</h1>
                    <Link to={`/environment/${this.state.environmentName}/capability/add`}><button className="btn btn-primary float-right">Add Capability</button></Link>
                </div>
                <br/><br/>
                <MaterialTable
            columns={[
              
            { title: "ID", field: "capabilityId" },
            { title: "Name", field: "capabilityName" },
            { title: "Parent ID", field: "parentCapabilityId" },
            { title: "Level", field: "level" },
            {
                title: '', 
                name: 'delete',
                render: rowData => <button className="btn btn-secondary"><i onClick={this.delete.bind(this, rowData.capabilityId)} className="bi bi-trash"></i></button>
            },
            {
                title: '', 
                name: 'edit',
                render: rowData => <button className="btn btn-secondary"><i onClick={this.edit.bind(this, rowData.capabilityId)} className="bi bi-pencil"></i></button>
            },
          ]}
          data={this.state.capabilities}
          parentChildData={(row, rows) => rows.find(a => a.capabilityId === row.parentCapabilityId)}
          options={{
              showTitle: false,
              search: false
          }}
        />
        </div>
        </div>
        )
    }
}