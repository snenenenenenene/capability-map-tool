import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import MaterialTable from 'material-table';
import './GeneralTable.css'
import axios from 'axios';

export default class ITApplication extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId: '',
            itApplications: [],
        };
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`)
        .then(response => this.setState({environmentId: response.data.environmentId}))
        .catch(error => {
            console.log(error)
            this.props.history.push('/error')
        })
        
        await axios.get(`${process.env.REACT_APP_API_URL}/itApplication/`)
        .then(response => {
                this.setState({itApplications: response.data});
            })
        .catch(error => {
            console.log(error)
        })
    }

    edit(itApplicationId){
        this.props.history.push(`/environment/${this.state.environmentName}/itApplication/${itApplicationId}`)
    }
    //DELETE CAPABILITY AND REMOVE ALL CHILD CAPABILITIES FROM STATE
    delete = async(itApplicationId) => {
        if (window.confirm('Are you sure you want to delete this IT Application?')){
            await axios.delete(`${process.env.REACT_APP_API_URL}/itApplication/${itApplicationId}`)
            .catch(error => console.error(error))
            //REFRESH CAPABILITIES
            await axios.get(`${process.env.REACT_APP_API_URL}/itApplication/`)
            .then(response => {
                    this.setState({itApplications: []})
                    this.setState({itApplications: response.data});
                })
            .catch(error => {
                console.log(error)
                this.props.history.push('/error')
            })
        }
    }

    render() {
        return(
            <div>
            <nav aria-label="breadcrumb">
            <ol className="breadcrumb">
                <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}`}>{this.state.environmentName}</Link></li>
                <li className="breadcrumb-item">IT Applications</li>
            </ol>
        </nav>
            <div className="jumbotron">
                <div>
                    <h1 className='display-4' style={{display: 'inline-block'}}>IT Applications</h1>
                    <Link to={`/environment/${this.state.environmentName}/itApplication/add`}><button className="btn btn-primary float-right">Add IT Application</button></Link>
                </div>
                <br/><br/>
                <MaterialTable
            columns={[
              
            { title: "ID", field: "itApplicationId" },
            { title: "Name", field: "itApplicationName" },
            {
                title: '', 
                name: 'delete',
                render: rowData => <button className="btn btn-secondary"><i onClick={this.delete.bind(this, rowData.itApplicationId)} className="bi bi-trash"></i></button>
            },
            {
                title: '', 
                name: 'edit',
                render: rowData => <button className="btn btn-secondary"><i onClick={this.edit.bind(this, rowData.itApplicationId)} className="bi bi-pencil"></i></button>
            },
          ]}
          data={this.state.itApplications}
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