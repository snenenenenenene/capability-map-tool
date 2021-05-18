import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import MaterialTable from 'material-table';
import './GeneralTable.css'
import axios from 'axios';

export default class StrategyItem extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId: '',
            strategyItems: [],
            reload: false
        };
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`)
        .then(response => this.setState({environmentId: response.data.environmentId}))
        .catch(error => {
            console.log(error)
            this.props.history.push('/error')
        })
        
        await axios.get(`${process.env.REACT_APP_API_URL}/strategyitem/`)
        .then(response => {
                this.setState({strategyItems: response.data});
            })
        .catch(error => {
            console.log(error)
            // this.props.history.push('/error')
        })
    }

    edit(strategyItemId){
        this.props.history.push(`/environment/${this.state.environmentName}/strategyItem/${strategyItemId}/edit`)
    }
    //DELETE strategyItem AND REMOVE ALL CHILD strategyItems FROM STATE
    delete = async(strategyItemId) => {
        if (window.confirm('Are you sure you want to delete this strategyItem?')){
            await axios.delete(`${process.env.REACT_APP_API_URL}/strategyItem/${strategyItemId}`)
            .catch(error => console.error(error))
            //REFRESH strategyItems
            await axios.get(`${process.env.REACT_APP_API_URL}/strategyItem/`)
            .then(response => {
                    this.setState({strategyItems: []})
                    this.setState({strategyItems: response.data});
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
                <li className="breadcrumb-item">Strategy Items</li>
            </ol>
        </nav>
            <div className="jumbotron">
                <div>
                    <h1 className='display-4' style={{display: 'inline-block'}}>Strategy Items</h1>
                    <Link to={`/environment/${this.state.environmentName}/strategyItem/add`}><button className="btn btn-primary float-right">Add Strategy Item</button></Link>
                </div>
                <br/><br/>
                <MaterialTable
            columns={[
              
            { title: "ID", field: "strategyItemId" },
            { title: "Name", field: "strategyItemName" },
            {
                title: '', 
                name: 'delete',
                render: rowData => <button className="btn btn-secondary"><i onClick={this.delete.bind(this, rowData.strategyItemId)} className="bi bi-trash"></i></button>
            },
            {
                title: '', 
                name: 'edit',
                render: rowData => <button className="btn btn-secondary"><i onClick={this.edit.bind(this, rowData.strategyItemId)} className="bi bi-pencil"></i></button>
            },
          ]}
          data={this.state.strategyItems}
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